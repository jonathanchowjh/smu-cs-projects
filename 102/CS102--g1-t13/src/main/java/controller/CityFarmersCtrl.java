package controller;

import dao.*;
import java.util.*;
import helpers.*;
import entities.*;
import entities.farmers.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class CityFarmersCtrl {
  private UserDAO udao;
  private PostDAO pdao;
  private FarmerDAO fdao;
  public CityFarmersCtrl() {
    this.udao = new UserDAO();
    this.pdao = new PostDAO();
    this.fdao = new FarmerDAO();
  }
  // --- Check Lands ---
  // Get Number of plots to add to user (Num of Plots supposed to have VS Number of Lands User currently has)
  // If (plots > 0) Add Num of plots
  // Run everytime user object is called.
  public boolean checkLands(User user) {
    if (user == null) return false;
    String username = user.getUsername();
    try {
      ArrayList<Map<String, String>> lands = fdao.getLands(username);
      Rank r = Data.getRank(user.getXp());
      int plots = r.getPlots() - lands.size();
      if (plots > 0) {
        for (int i = 0; i < plots; i++) {
          ArrayList<Map<String, String>> createLand = fdao.createLand(username, "NULL");
          if (!M.isSet(createLand, "rows_inserted", 1, 1)) throw new SqlNotSetException("");
        }
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return false;
  }
  // --- Send Gifts ---
  // Dont send gift (gifter=giftee / same crop was given to the same user in the same day / invalid usernames / giftee is not friend)
  // Set Gift ()
  public boolean sendGifts(String gifter, String giftee, Crop crop) {
    // System.out.println("GIFTER: " + gifter + "// GIFTEE: " + giftee + "// CROP: " + crop);
    if (gifter == null || giftee == null || crop == null) return false;
    if (gifter.equals(giftee)) return false;
    try {
      // check if usernames are valid
      if (udao.getUser(gifter) == null || udao.getUser(giftee) == null) return false;
      // check if giftee is friend
      boolean isFriend = false;
      ArrayList<Map<String, String>> friends = udao.getFriends(gifter);
      for (Map<String, String> friend : friends) {
        if (friend.get("username").equals(giftee)) {
          isFriend = true;
          break;
        }
      }
      if (!isFriend) return false;
      // check if (same crop was given to the same user in the same day)
      boolean haveSent = false;
      ArrayList<Map<String, String>> gifts = pdao.getGiftsGifter(gifter);
      for (Map<String, String> gift : gifts) {
        if (!gift.get("giftee").equals(giftee)) continue;
        if (!gift.get("crop").equals(crop.getName())) continue;
        String dt = gift.get("dt");
        Timestamp t = M.toTimestamp(dt);
        long daysElapsed = t.toInstant().until(Instant.now(),ChronoUnit.DAYS);
        if (daysElapsed < 1) return false;
      }
      ArrayList<Map<String, String>> createGifts = pdao.createGifts(gifter, giftee, crop.getName());
      if (!M.isSet(createGifts, "rows_inserted", 1) && !M.isSet(createGifts, "rows_edited", 1)) throw new SqlNotSetException("");
    } catch (DAOException e) {
      return false;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
  // --- Buy Seeds ---
  // Dont Buy Seeds (User not enough gold for Crop)
  // Set User (MINUS gold - Crop cost)
  // Set Seed (ADD quantity)
  public boolean buySeeds(User user, Crop crop, ArrayList<Seed> seeds, int quantity) {
    if (crop == null || user ==  null) return false;
    if (user.getGold() - crop.getCost() < 0) return false;
    try {
      int gold = user.getGold() - crop.getCost();
      // Get seed quantity
      Seed seed = null;
      for (Seed s : seeds) {
        if (s.getCrop().getName().equals(crop.getName())) {
          seed = s;
          break;
        }
      }
      int amount = seed == null ? quantity : quantity + seed.getQuantity();
      ArrayList<Map<String, String>> updateUser = udao.updateUser(user.getUsername(), gold, user.getXp());
      if (!M.isSet(updateUser, "rows_edited", 1, 1)) throw new SqlNotSetException("");
      ArrayList<Map<String, String>> setSeeds = fdao.setSeeds(user.getUsername(), crop.getName(), amount);
      if (!M.isSet(setSeeds, "rows_edited", 1) && !M.isSet(setSeeds, "rows_inserted", 1)) throw new SqlNotSetException("");
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
  // --- Clear ---
  // Dont clear (Land has no crop / Crop not wilted / User gold < 5)
  // Set User (MINUS gold - 5)
  // Set Land (SET land=NULL)
  public boolean clear(User user, Land land) {
    if (land == null || user ==  null) return false;
    if (land.getCrop() == null) return false;
    if (land.getPercentCompleted() >= 200 && user.getGold() < 5) return false;
    try {
      int gold = land.getPercentCompleted() >= 200 ? user.getGold() - 5 : user.getGold();
      ArrayList<Map<String, String>> updateUser = udao.updateUser(user.getUsername(), gold, user.getXp());
      if (!M.isSet(updateUser, "rows_edited", 1, 1)) throw new SqlNotSetException("");
      ArrayList<Map<String, String>> setLand = fdao.setLand(land.getLid(), land.getUsername(), "NULL");
      if (!M.isSet(setLand, "rows_edited", 1)) throw new SqlNotSetException("");
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
  // --- Plant ---
  // Dont plant (land has crop / seed quantity <= 0)
  // Set Seed (MINUS quantity - 1)
  // Set Land (SET Crop=seed)
  public boolean plant(Seed seed, Land land) {
    if (seed == null || land == null) return false;
    if (land.getCrop() != null) return false;
    if (seed.getQuantity() <= 0) return false;

    try {
      ArrayList<Map<String, String>> setSeeds = fdao.setSeeds(seed.getUsername(), seed.getCrop().getName(), seed.getQuantity() - 1);
      if (!M.isSet(setSeeds, "rows_inserted", 1) && !M.isSet(setSeeds, "rows_edited", 1)) throw new SqlNotSetException("");
      ArrayList<Map<String, String>> setLand = fdao.setLand(land.getLid(), land.getUsername(), seed.getCrop().getName());
      if (!M.isSet(setLand, "rows_edited", 1)) throw new SqlNotSetException("");
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
  // --- Harvest ---
  // Dont harvest (land has no crop / 100 > percent grown >= 200)
  // Set User (ADD Gold, Xp)
  // Set Land (SET Crop=NULL)
  public int harvest(User user, Land land) {
    if (user == null || land == null) return 0;
    if (land.getCrop() == null) return 0;
    long percent = land.getPercentCompleted();
    if (percent < 100 || percent >= 200) return 0;
    // get gold value = rand yield * salePrice
    Crop c = land.getCrop();
    int yield = c.getRandYield();
    int gold = user.getGold() + (c.getSalePrice() * yield);
    // get xp = rand yield * xp
    int xp = user.getXp() + (c.getXp() * yield);
    // plus gold, plus xp
    try {
      ArrayList<Map<String, String>> updateUser = udao.updateUser(user.getUsername(), gold, xp);
      if (!M.isSet(updateUser, "rows_edited", 1, 1)) throw new SqlNotSetException("");
      ArrayList<Map<String, String>> setLand = fdao.setLand(land.getLid(), land.getUsername(), "NULL");
      if (!M.isSet(setLand, "rows_edited", 1)) throw new SqlNotSetException("");
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
    return yield;
  }
  // Return array of lands
  public ArrayList<Land> getLands(String username) {
    ArrayList<Land> lands = new ArrayList<>();
    try {
      ArrayList<Map<String, String>> mappedLands = fdao.getLands(username);
      
      for (Map<String, String> land : mappedLands) {
        lands.add(new Land(land));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return lands;
  }
  // Return array of seeds
  public ArrayList<Seed> getSeeds(String username) {
    ArrayList<Seed> seeds = new ArrayList<>();
    try {
      ArrayList<Map<String, String>> mappedSeeds =  fdao.getSeeds(username);
      for (Map<String, String> seed : mappedSeeds) {
        seeds.add(new Seed(seed));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return seeds;
  }
}
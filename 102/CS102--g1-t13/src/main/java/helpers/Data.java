package helpers;

import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.security.InvalidParameterException;

import entities.farmers.*;

// Class calls data from csv file
public class Data {
  // Returns Crop from Crop Name
  public static Crop getCrop(String cropName) {
    ArrayList<Crop> crops = cropTypes();
    Crop crop = null;
    for (Crop c : crops) {
      if (c.getName().equals(cropName)) {
        crop = c;
        break;
      }
    }
    return crop;
  }
  // Returns Rank from XP
  public static Rank getRank(int xp) {
    ArrayList<Rank> ranks = rankTypes();
    Rank rank = null;
    for (int i = ranks.size() - 1; i >= 0; i--) {
      Rank r = ranks.get(i);
      if (xp >= r.getXp()) {
        rank = r;
        break;
      }
    }
    return rank;
  }
  // Returns Array of Crops
  public static ArrayList<Crop> cropTypes() throws InvalidParameterException {
    ArrayList<Crop> crops = new ArrayList<>();
    try{
      Scanner sc = new Scanner(new File("data/crop.csv"));
      sc.nextLine();
      while (sc.hasNextLine()) {
        String line = sc.nextLine();
        String[] ln = line.split(",");
        if (ln.length != 7) throw new InvalidParameterException("Crop (CSV Error) - Wrong number of parameters -> " + ln.length);
        Crop crop = new Crop(ln[0], Integer.parseInt(ln[1]), Integer.parseInt(ln[2]), Integer.parseInt(ln[3]), Integer.parseInt(ln[4]), Integer.parseInt(ln[5]), Integer.parseInt(ln[6]));
        crops.add(crop);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return crops;
  }
  // Returns Array of Ranks
  public static ArrayList<Rank> rankTypes() throws InvalidParameterException {
    ArrayList<Rank> ranks = new ArrayList<>();
    try{
      Scanner sc = new Scanner(new File("data/rank.csv"));
      sc.nextLine();
      while (sc.hasNextLine()) {
        String line = sc.nextLine();
        String[] ln = line.split(",");
        if (ln.length != 3) throw new InvalidParameterException("Rank (CSV Error) - Wrong number of parameters -> " + ln.length);
        Rank rank = new Rank(ln[0], Integer.parseInt(ln[1]), Integer.parseInt(ln[2]));
        ranks.add(rank);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ranks;
  }
}
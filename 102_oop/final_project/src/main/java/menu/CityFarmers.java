package menu;

import java.util.*;
import helpers.*;
import controller.*;
import entities.*;
import entities.farmers.*;

public class CityFarmers {
  private CityFarmersCtrl ctrl;
  private UserController uCtrl;
  private User user;
  private ArrayList<Land> lands;
  private ArrayList<Seed> seeds;
  
  public CityFarmers(User user) {
    this.ctrl = new CityFarmersCtrl();
    this.uCtrl = new UserController();
    this.user = user;
    this.lands = ctrl.getLands(this.user.getUsername());
    this.seeds = ctrl.getSeeds(this.user.getUsername());
  }

  public void startGame() {
    String op = "";
    while (M.chAt(op, 0) != 'M') {
      this.printTitle("");
      System.out.print("1. My Farmland\r\n2. My Store\r\n3. My Inventory\r\n4. Visit Friend\r\n5. Send Gift\r\n");
      System.out.print("[M]ain | Enter your choice > ");
      Scanner sc = new Scanner(System.in);
      op = sc.nextLine();
      if (M.chAt(op, 0) == '1') op = this.farmland();
      if (M.chAt(op, 0) == '2') op = this.store();
      if (M.chAt(op, 0) == '3') op = this.inventory();
      if (M.chAt(op, 0) == '4') op = this.visitFriend();
      if (M.chAt(op, 0) == '5') op = this.sendGift();
    }
    System.out.println();
  }

  private void printTitle(String location) {
    if (location.equals("")) {
      System.out.println("\r\n\r\n== Social Magnet :: City Farmers ==");
    } else {
      System.out.printf("\r\n\r\n== Social Magnet :: City Farmers :: %s ==\r\n", location);
    }
    System.out.printf("Welcome, %s!\r\nTitle: %s\tGold: %d\r\n\r\n", this.user.getName(), Data.getRank(this.user.getXp()).getName(), this.user.getGold());
  }

  private void refresh() {
    this.user = uCtrl.checkUser(this.user.getUsername());
    this.lands = ctrl.getLands(this.user.getUsername());
    this.seeds = ctrl.getSeeds(this.user.getUsername());
  }

  // ==================================
  // ============ FARMLAND ============
  // ==================================
  private String farmland() {
    String op = "";
    Scanner sc = new Scanner(System.in);
    try {
      do {
        this.printTitle("My Farmland");
        this.printLands(this.lands);
        System.out.printf("You have %d plots of land.\r\n", this.lands.size());
        System.out.print("[M]ain | City [F]armers | [P]lant | [C]lear | [H]arvest > ");
        op = sc.nextLine();
        if (M.chAt(op, 0) == 'P' && op.length() >= 2) this.plant(Integer.parseInt("" + op.charAt(1)) - 1);
        if (M.chAt(op, 0) == 'C' && op.length() >= 2) this.clear(Integer.parseInt("" + op.charAt(1)) - 1);
        if (M.chAt(op, 0) == 'H' && op.length() >= 2) this.harvest(Integer.parseInt("" + op.charAt(1)) - 1);
      } while (M.chAt(op, 0) != 'M' && M.chAt(op, 0) != 'F');
    } catch (Exception e) {
      e.printStackTrace();
    }
    return op;
  }

  private void printLands(ArrayList<Land> lands) {
    for (int i = 0; i < lands.size(); i++) {
      Land l = lands.get(i);
      if (l.getCrop() == null) {
        System.out.printf("%d. <empty>\r\n", i + 1);
        continue;
      }
      long percent = l.getPercentCompleted();
      long percentCap = percent > 100 ? 100 : percent;
      String percentCapString = percent >= 200 ? "" : "" + percentCap + "%";
      System.out.printf("%d. %s\t%s %s\r\n", i + 1, l.getCrop().getName(), l.percentMeter(percent), percentCapString);
    }
  }

  private void clear(int landIndex) {
    if (landIndex < 0 || landIndex >= this.lands.size()) {
      System.out.println("Invalid input - invalid Land");
      return;
    }
    if (ctrl.clear(this.user, this.lands.get(landIndex))) {
      System.out.printf("Plot %d cleared\r\n", landIndex + 1);
    } else {
      System.out.printf("Plot %d not cleared\r\n", landIndex + 1);
    }
    this.refresh();
  }

  private void harvest(int landIndex) {
    if (landIndex < 0 || landIndex >= this.lands.size()) {
      System.out.println("Invalid input - invalid Land");
      return;
    }
    Land land = this.lands.get(landIndex);
    int yield = ctrl.harvest(this.user, land);
    if (yield != 0) {
      Crop c = land.getCrop();
      System.out.printf("You have harvested %d %s for %d XP and %d gold.\r\n", yield, c.getName(), c.getXp() *  yield, c.getSalePrice() * yield);
    } else {
      System.out.printf("Plot %d not harvested\r\n", landIndex + 1);
    }
    this.refresh();
  }

  private String plant(int landIndex) {
    if (landIndex < 0 || landIndex >= this.lands.size()) {
      System.out.println("Invalid input - invalid Land");
      return "F";
    }
    System.out.println("Select the crop:");
    int counter = 1;
    for (int i = 0; i < this.seeds.size(); i++) {
      Seed s = this.seeds.get(i);
      if (s.getQuantity() <= 0) continue;
      System.out.printf("%d. %s\r\n", counter++, s.getCrop().getName());
    }
    System.out.print("[M]ain | City [F]armers | Select Choice > ");
    Scanner sc = new Scanner(System.in);
    String op = sc.nextLine();
    if (M.chAt(op, 0) == 'M' || M.chAt(op, 0) == 'F') return op;
    Seed s = this.seeds.get(Integer.parseInt("" + op.charAt(0)) - 1);
    Land l = this.lands.get(landIndex);
    if (ctrl.plant(s, l)) {
      System.out.printf("%s planted on plot %d.\r\n", s.getCrop().getName(), landIndex + 1);
    } else {
      System.out.println("not planted");
    }
    this.refresh();
    return "F";
  }
  // ===============================
  // ============ STORE ============
  // ===============================
  private String store() {
    String op = "";
    Scanner sc = new Scanner(System.in);
    this.printTitle("My Store");
    System.out.println("Seeds Available:");
    ArrayList<Crop> crops = Data.cropTypes();
    for (int i = 0; i < crops.size(); i++) {
      Crop c = crops.get(i);
      System.out.printf("%d. %s\t- %d gold\r\nHarvest in: %d mins\r\nXP Gained: %d\r\n", i + 1, c.getName(), c.getCost(), c.getTime(), c.getXp());
    }
    System.out.print("\r\n[M]ain | City [F]armers | Select choice > ");
    op = sc.nextLine();
    if (op.length() == 1 && Character.isDigit(op.charAt(0))) {
      System.out.print("Enter quantity > ");
      int option = sc.nextInt();
      Crop crop = crops.get(Integer.parseInt("" + op.charAt(0)) - 1);
      if (ctrl.buySeeds(this.user, crop, this.seeds, option)) {
        System.out.printf("%d bags of seeds purchased for %d gold.", option, option * crop.getCost());
      } else {
        System.out.print("Seeds not bought");
      }
    }
    this.refresh();
    if (op.contains("M")) return op;
    return "F";
  }

  // ===================================
  // ============ INVENTORY ============
  // ===================================

  private String inventory() {
    String op = "";
    Scanner sc = new Scanner(System.in);
    this.printTitle("My Inventory");
    System.out.println("My Seeds:");
    int counter = 1;
    for (int i = 0; i < this.seeds.size(); i++) {
      Seed seed = this.seeds.get(i);
      if (seed.getQuantity() <= 0) continue;
      System.out.printf("%d. %d bags of %s\r\n", counter++, seed.getQuantity(), seed.getCrop().getName());
    }
    System.out.print("[M]ain | City [F]armers | Select choice > ");
    op = sc.nextLine();
    if (op.contains("M")) return op;
    return "F";
  }

  // ======================================
  // ============ VISIT FRIEND ============
  // ======================================
  private String visitFriend() {
    String op = "";
    do {
      Scanner sc = new Scanner(System.in);
      this.printTitle("Visit Friend");
      System.out.println("My Friends:");
      ArrayList<User> friends = uCtrl.getFriends(this.user.getUsername());
      if (friends != null) {
        for (int i = 0; i < friends.size(); i++) {
          User friend = friends.get(i);
          System.out.printf("%d. %s (%s)\r\n", i + 1, friend.getName(), friend.getUsername());
        }
      }

      System.out.print("\r\n[M]ain | City [F]armers | Select Choice > ");
      op = sc.nextLine();
      if (Character.isDigit(op.charAt(0))) {
        User friend = friends.get(Integer.parseInt("" + op.charAt(0)) - 1);
        ArrayList<Land> lands = ctrl.getLands(friend.getUsername());
        System.out.printf("Name: %s\r\nTitle: %s\r\nGold: %d\r\n", friend.getName(), Data.getRank(friend.getXp()).getName(), friend.getGold());
        this.printLands(lands);
        System.out.print("\r\n[M]ain | City [F]armers | [S]teal > ");
        op = sc.nextLine();
        if (op.length() == 2 && M.chAt(op, 0) == 'S' && Character.isDigit(M.chAt(op, 1))) {
          Land land = lands.get(Integer.parseInt("" + M.chAt(op, 1)) - 1);
          int yield = ctrl.harvest(this.user, land);
          if (yield == 0) {
            System.out.println("Crop not Stolen");
          } else {
            Crop c = land.getCrop();
            System.out.printf("You have successfully stolen %d %s for %d XP, and %d gold.", yield, c.getName(), yield * c.getXp(), yield * c.getSalePrice());
          }
        }
      }
    } while (M.chAt(op, 0) != 'M' && M.chAt(op, 0) != 'F');
    return op;
  }

  // ===================================
  // ============ SEND GIFT ============
  // ===================================

  private String sendGift() {
    String op = "";
    do {
      Scanner sc = new Scanner(System.in);
      this.printTitle("Send a Gift");
      System.out.println("Gifts Available:");
      ArrayList<Crop> crops = Data.cropTypes();
      for (int i = 0; i < crops.size(); i++) {
        Crop c = crops.get(i);
        System.out.printf("%d. 1 Bag of %s Seeds\r\n", i + 1, c.getName());
      }
      System.out.print("[R]eturn to main | Select Choice > ");
      op = sc.nextLine();
      if (Character.isDigit(M.chAt(op, 0))) {
        Crop crop = crops.get(Integer.parseInt("" + M.chAt(op, 0)) - 1);
        System.out.print("Send to > ");
        String option = sc.nextLine();
        boolean allSent = true;
        for (String username : option.split(",")) {
          if (!ctrl.sendGifts(this.user.getUsername(), username.trim(), crop)) allSent = false;
        }
        if (allSent) {
          System.out.println("Gift posted to your friends' wall.");
        } else {
          System.out.println("Gift not posted.");
        }
      }
    } while (M.chAt(op, 0) != 'R');
    if (M.chAt(op, 0) == 'R') return "M";
    return "F";
  }
}

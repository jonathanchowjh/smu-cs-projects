package controller;

import dao.*;
import entities.User;
import java.util.*;

public class UserController {
  private UserDAO user_dao;
  private CityFarmersCtrl fctrl;

  public UserController() {
    this.user_dao = new UserDAO();
    this.fctrl = new CityFarmersCtrl();
  }

  public ArrayList<User> getFriends(String username) {
    ArrayList<User> friends = new ArrayList<>();
    try {
      ArrayList<Map<String, String>> users = user_dao.getFriends(username);
      for (Map<String, String> f : users) {
        // String name, String username, String password, int gold, int xp
        int gold = Integer.parseInt(f.get("gold"));
        int xp = Integer.parseInt(f.get("gold"));
        User user = new User(f.get("name"), f.get("username"), f.get("password"), gold, xp);
        friends.add(user);
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return friends;
  }

  public User checkUser(String username) {
    User user = null;
    Map<String, String> userMap = null;
    try {
      userMap = user_dao.getUser(username);
    }
    catch (DAOException e) {
    }
    if (userMap == null) return user;
    user = new User(userMap.get("name"), userMap.get("username"), userMap.get("password"), Integer.parseInt(userMap.get("gold")), Integer.parseInt(userMap.get("xp")));
    if (fctrl.checkLands(user)) System.out.println("Created Lands");
    return user;
  }

  public User getUser(String username) {
    User user = null;
    try {
      Map<String, String> userMap = user_dao.getUser(username);
      user = new User(userMap.get("name"), userMap.get("username"), userMap.get("password"), Integer.parseInt(userMap.get("gold")), Integer.parseInt(userMap.get("xp")));
      if (fctrl.checkLands(user)) System.out.println("Created Lands");
    }
    catch (DAOException e) {
      System.out.println("Unable to retrieve user.");
    }
    // String name, String username, String password, int gold, int xp
    return user;
  }
  
  public ArrayList<Map<String, String>> addUser(String username, String name, String password){
    ArrayList<Map<String, String>> arr = null;
    try {
      arr = user_dao.register(username, name, password);
      if (fctrl.checkLands(this.getUser(username))) System.out.println("Created Lands");
    }
    catch (DAOException e){
      System.out.println("User already added!\n");
    }
    return arr;
  }

}
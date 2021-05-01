package dao;

import helpers.M;
import entities.User;
import java.util.*;

// API Documentation
// url = localhost:9999
// Set User = /user/set?username=<string>&name=<string>&password=<string>&gold=<int>&xp=<int>
// Set Friend = /user/friends/set?username1=<string>&username2=<string>
// Set Friend Req = /user/friendsreq/set?sender=<string>&receipient=<string>
// Get User = /user?username=<string>
// Get Friends = /user/friends?username=<string>
// Get Friends Sender = /user/friendreqs?username=<sender>
// Get Friends Receipient = /user/friendreqr?username=<receipient>

public class UserDAO {
  private Connection conn;
  public UserDAO() {
    this.conn = new Connection();
  }

  public ArrayList<Map<String, String>> register(String username, String name, String password) throws DAOException {
    ArrayList<Map<String, String>> arr = null;
    try {
      String cString = "/user/set?username=" + username.replace(" ", "%20") + "&name=" + name.replace(" ", "%20") + "&password=" + password.replace(" ", "%20") + "&gold=50&xp=0";
      String json = conn.connect(cString);
      if (json == null) throw new DAOException("register - connection error");

      // Processing Array of Objects
      ArrayList<Map<String, String>> arrOfObj = M.jsonAofObj(json);
      arr = arrOfObj;
    } catch (DAOException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return arr;
  }

  public ArrayList<Map<String, String>> updateUser(String username, int gold, int xp) throws DAOException {
    ArrayList<Map<String, String>> arr = null;
    try {
      String cString = "/user/set?username=" + username.replace(" ", "%20") + "&gold=" + gold + "&xp=" + xp;
      String json = conn.connect(cString);
      if (json == null) throw new DAOException("updateUser - connection error");

      // Processing Array of Objects
      ArrayList<Map<String, String>> arrOfObj = M.jsonAofObj(json);
      arr = arrOfObj;
    } catch (DAOException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return arr;
  }

  public ArrayList<Map<String, String>> setFriends(String username1, String username2) throws DAOException {
    ArrayList<Map<String, String>> arr = null;
    try {
      String cString = "/user/friends/set?username1=" + username1.replace(" ", "%20") + "&username2=" + username2.replace(" ", "%20");
      String json = conn.connect(cString);
      if (json == null) throw new DAOException("setFriends - connection error");

      // Processing Array of Objects
      ArrayList<Map<String, String>> arrOfObj = M.jsonAofObj(json);
      arr = arrOfObj;
    } catch (DAOException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return arr;
  }

  // if (unique sender/receipient) {add} else {delete}
  public ArrayList<Map<String, String>> setFriendsReq(String sender, String receipient) throws DAOException {
    ArrayList<Map<String, String>> arr = null;
    try {
      String cString = "/user/friendsreq/set?sender=" + sender.replace(" ", "%20") + "&receipient=" + receipient.replace(" ", "%20");
      String json = conn.connect(cString);
      if (json == null) throw new DAOException("setFriendsReq - connection error");

      // Processing Array of Objects
      ArrayList<Map<String, String>> arrOfObj = M.jsonAofObj(json);
      arr = arrOfObj;
    } catch (DAOException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return arr;
  }

  public Map<String, String> getUser(String username) throws DAOException {
    Map<String, String> user = null;
    try {
      String json = conn.connect("/user?username=" + username.replace(" ", "%20"));
      if (json == null) throw new DAOException("getUser - connection error");

      // Processing Array of Objects
      ArrayList<Map<String, String>> arrOfObj = M.jsonAofObj(json);

      if (arrOfObj == null) throw new DAOException("getUser - Null Json Array");
      if (arrOfObj.size() != 1) throw new DAOException("getUser - User not unique");
      Map<String, String> obj = arrOfObj.get(0);
      if (obj == null) throw new DAOException("getUser - Null Json Object");
      user = obj;
    } catch (DAOException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return user;
  }

  public boolean checkPassword(String username, String password) throws DAOException {
    Map<String, String> user = this.getUser(username);
    if (user == null) throw new DAOException("checkPassword - Null User");
    if (user.get("password").equals(password)) return true;
    return false;
  }

  public ArrayList<Map<String, String>> getFriends(String username) throws DAOException {
    ArrayList<Map<String, String>> friends = null;
    try {
      String json = conn.connect("/user/friends?username=" + username.replace(" ", "%20"));
      if (json == null) throw new DAOException("getFriends - connection error");

      // Processing Array of Objects
      ArrayList<Map<String, String>> arrOfObj = M.jsonAofObj(json);
      if (arrOfObj == null) throw new DAOException("getFriends - Null Json Array");
      friends = arrOfObj;
    } catch (DAOException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return friends;
  }

  public ArrayList<Map<String, String>> getFriendsRequestS(String username) throws DAOException {
    ArrayList<Map<String, String>> friends = null;
    try {
      String json = conn.connect("/user/friendreqs?username=" + username.replace(" ", "%20"));
      if (json == null) throw new DAOException("getFriendsRequestS - connection error");

      // Processing Array of Objects
      ArrayList<Map<String, String>> arrOfObj = M.jsonAofObj(json);
      if (arrOfObj == null) throw new DAOException("getFriendsRequestS - Null Json Array");
      friends = arrOfObj;
    } catch (DAOException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return friends;
  }

  public ArrayList<Map<String, String>> getFriendsRequestR(String username) throws DAOException {
    ArrayList<Map<String, String>> friends = null;
    try {
      String json = conn.connect("/user/friendreqr?username=" + username.replace(" ", "%20"));
      if (json == null) throw new DAOException("getFriendsRequestR - connection error");

      // Processing Array of Objects
      ArrayList<Map<String, String>> arrOfObj = M.jsonAofObj(json);
      if (arrOfObj == null) throw new DAOException("getFriendsRequestR - Null Json Array");
      friends = arrOfObj;
    } catch (DAOException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return friends;
  }
}
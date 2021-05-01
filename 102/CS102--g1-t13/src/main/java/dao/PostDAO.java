package dao;

import helpers.M;
import entities.User;
import java.util.*;

// API Documentation
// url = localhost:9999
// Set Gifts = /posts/gifts/set?gid=<int>&gifter=<string>&giftee=<string>&=crop<string>&dt=<dt>&accepted<boolean>
// Set Replies = /posts/replies/set?rid=<int>&pid=<int>&username=<string>&reply=<string>&dt=<dt>
// Set Likes = /posts/likes/set?pid=<int>&username=<string>&isLike=<boolean>
// Set Posts = /posts/set?pid=<int>&username=<string>&dst_username=<string>&post=<string>&dt=<dt>
// Set Kill = /posts/kill/set?pid=<int>&username=<string>
// Get Posts = /posts/user?username=<string>
// Get Posts Dests = /posts/dst?username=<string>
// Get Likes = /posts/likes?pid=<int>
// Get Replies = /posts/replies?pid=<int>
// Get Gift Gifter = /posts/giftsGifter?username=<string>
// Get Gifts = /posts/gifts?username=<string>

public class PostDAO {
  private Connection conn;
  public PostDAO() {
    this.conn = new Connection();
  }

  public ArrayList<Map<String, String>> setKill(int pid, String username) throws DAOException {
    ArrayList<Map<String, String>> arr = null;
    try {
      String cString = "/posts/kill/set?pid=" + pid + "&username=" + username.replace(" ", "%20");
      String json = conn.connect(cString);
      if (json == null) throw new DAOException("setGifts - connection error");

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

  public ArrayList<Map<String, String>> createGifts(String gifter, String giftee, String crop) throws DAOException {
    ArrayList<Map<String, String>> arr = null;
    try {
      String cString = "/posts/gifts/set?gid=&gifter=" + gifter.replace(" ", "%20") + "&giftee=" + giftee.replace(" ", "%20") + "&crop=" + crop.replace(" ", "%20") + "&dt=" + System.currentTimeMillis();
      String json = conn.connect(cString);
      if (json == null) throw new DAOException("setGifts - connection error");

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

  public ArrayList<Map<String, String>> setGifts(int gid, boolean accepted) throws DAOException {
    ArrayList<Map<String, String>> arr = null;
    try {
      String cString = "/posts/gifts/set?gid=" + gid + "&accepted=" + accepted;
      String json = conn.connect(cString);
      if (json == null) throw new DAOException("setGifts - connection error");

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

  public ArrayList<Map<String, String>> setReplies(int pid, String username, String reply) throws DAOException {
    ArrayList<Map<String, String>> arr = null;
    try {
      String cString = "/posts/replies/set?rid=&pid=" + pid + "&username=" + username.replace(" ", "%20") + "&reply=" + reply.replace(" ", "%20") + "&dt=" + System.currentTimeMillis();
      String json = conn.connect(cString);
      if (json == null) throw new DAOException("setReplies - connection error");

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

  public ArrayList<Map<String, String>> deleteReplies(int rid) throws DAOException {
    ArrayList<Map<String, String>> arr = null;
    try {
      String cString = "/posts/replies/set?rid=" + rid;
      String json = conn.connect(cString);
      if (json == null) throw new DAOException("deleteReplies - connection error");

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

  // if (unique pid/username) {insert} else {update}
  public ArrayList<Map<String, String>> setLike(int pid, String username, boolean isLike) throws DAOException {
    ArrayList<Map<String, String>> arr = null;
    try {
      String cString = "/posts/likes/set?pid=" + pid + "&username=" + username.replace(" ", "%20") + "&isLike=" + Boolean.toString(isLike);
      String json = conn.connect(cString);
      if (json == null) throw new DAOException("setLike - connection error");

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

  public ArrayList<Map<String, String>> setPosts(String username, String dst_username, String post) throws DAOException {
    ArrayList<Map<String, String>> arr = null;
    try {
      String cString = "/posts/set?pid=&username=" + username.replace(" ", "%20") + "&dst_username=" + dst_username.replace(" ", "%20") + "&post=" + post.replace(" ", "%20") + "&dt=" + System.currentTimeMillis();
      String json = conn.connect(cString);
      if (json == null) throw new DAOException("setPosts - connection error");

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

  public ArrayList<Map<String, String>> deletePosts(int pid) throws DAOException {
    ArrayList<Map<String, String>> arr = null;
    try {
      String cString = "/posts/set?pid=" + pid;
      String json = conn.connect(cString);
      if (json == null) throw new DAOException("deletePosts - connection error");

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

  public ArrayList<Map<String, String>> getPostUser(String username) throws DAOException {
    ArrayList<Map<String, String>> posts = null;
    try {
      String json = conn.connect("/posts/user?username=" + username.replace(" ", "%20"));
      if (json == null) throw new DAOException("getPostUser - connection error");

      // Processing Array of Objects
      ArrayList<Map<String, String>> arrOfObj = M.jsonAofObj(json);
      if (arrOfObj == null) throw new DAOException("getPostUser - Null Json Array");
      posts = arrOfObj;
    } catch (DAOException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return posts;
  }

  public ArrayList<Map<String, String>> getPostDest(String username) throws DAOException {
    ArrayList<Map<String, String>> posts = null;
    try {
      String json = conn.connect("/posts/dst?username=" + username.replace(" ", "%20"));
      if (json == null) throw new DAOException("getPostDest - connection error");

      // Processing Array of Objects
      ArrayList<Map<String, String>> arrOfObj = M.jsonAofObj(json);
      if (arrOfObj == null) throw new DAOException("getPostDest - Null Json Array");
      posts = arrOfObj;
    } catch (DAOException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return posts;
  }

  public ArrayList<Map<String, String>> getLikes(int pid) throws DAOException {
    ArrayList<Map<String, String>> posts = null;
    try {
      String json = conn.connect("/posts/likes?pid=" + pid);
      if (json == null) throw new DAOException("getLikes - connection error");

      // Processing Array of Objects
      ArrayList<Map<String, String>> arrOfObj = M.jsonAofObj(json);
      if (arrOfObj == null) throw new DAOException("getLikes - Null Json Array");
      posts = arrOfObj;
    } catch (DAOException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return posts;
  }

  public ArrayList<Map<String, String>> getReplies(int pid) throws DAOException {
    ArrayList<Map<String, String>> posts = null;
    try {
      String json = conn.connect("/posts/replies?pid=" + pid);
      if (json == null) throw new DAOException("getReplies - connection error");

      // Processing Array of Objects
      ArrayList<Map<String, String>> arrOfObj = M.jsonAofObj(json);
      if (arrOfObj == null) throw new DAOException("getReplies - Null Json Array");
      posts = arrOfObj;
    } catch (DAOException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return posts;
  }

  public ArrayList<Map<String, String>> getGiftsGifter(String username) throws DAOException {
    ArrayList<Map<String, String>> posts = null;
    try {
      String json = conn.connect("/posts/giftsGifter?username=" + username.replace(" ", "%20"));
      if (json == null) throw new DAOException("getGifts - connection error");

      // Processing Array of Objects
      ArrayList<Map<String, String>> arrOfObj = M.jsonAofObj(json);
      if (arrOfObj == null) throw new DAOException("getGifts - Null Json Array");
      posts = arrOfObj;
    } catch (DAOException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return posts;
  }

  public ArrayList<Map<String, String>> getGifts(String username) throws DAOException {
    ArrayList<Map<String, String>> posts = null;
    try {
      String json = conn.connect("/posts/gifts?username=" + username.replace(" ", "%20"));
      if (json == null) throw new DAOException("getGifts - connection error");

      // Processing Array of Objects
      ArrayList<Map<String, String>> arrOfObj = M.jsonAofObj(json);
      if (arrOfObj == null) throw new DAOException("getGifts - Null Json Array");
      posts = arrOfObj;
    } catch (DAOException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return posts;
  }
}
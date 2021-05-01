package dao;

import helpers.M;
import entities.User;
import java.util.*;

// API Documentation
// url = localhost:9999
// Set Seeds = /farmers/seeds/set?username=<username>&crop=<crop>&quantity=<quantity>
// Set Lands = /farmers/seeds/set?username=<username>&crop=<crop>&lid=<lid>&startTime=<startTime>
// Get Seeds = /farmers/seeds?username=<username>
// Get Lands = /farmers/lands?username=<username>

public class FarmerDAO {
  private Connection conn;
  public FarmerDAO() {
    this.conn = new Connection();
  }
  // if (unique user/seed) {insert} else {edit}
  public ArrayList<Map<String, String>> setSeeds(String username, String crop, int quantity) throws DAOException {
    ArrayList<Map<String, String>> arr = null;
    try {
      String cString = "/farmers/seeds/set?username=" + username.replace(" ", "%20") + "&crop=" + crop.replace(" ", "%20") + "&quantity=" + quantity;
      String json = conn.connect(cString);
      if (json == null) throw new DAOException("setSeeds - connection error");

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

  public ArrayList<Map<String, String>> createLand(String username, String crop) throws DAOException {
    ArrayList<Map<String, String>> arr = null;
    try {
      String cString = "/farmers/lands/set?username=" + username.replace(" ", "%20") + "&crop=" + crop.replace(" ", "%20") + "&lid=&startTime=" + System.currentTimeMillis();
      String json = conn.connect(cString);
      if (json == null) throw new DAOException("createLand - connection error");

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

  public ArrayList<Map<String, String>> setLand(int lid, String username, String crop) throws DAOException {
    ArrayList<Map<String, String>> arr = null;
    try {
      String cString = "/farmers/lands/set?username=" + username.replace(" ", "%20") + "&crop=" + crop.replace(" ", "%20") + "&lid=" + lid + "&startTime=" + System.currentTimeMillis();
      String json = conn.connect(cString);
      if (json == null) throw new DAOException("setLands - connection error");

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

  public ArrayList<Map<String, String>> getLands(String username) throws DAOException {
    ArrayList<Map<String, String>> posts = null;
    try {
      String json = conn.connect("/farmers/lands?username=" + username.replace(" ", "%20"));
      if (json == null) throw new DAOException("getLands - connection error");

      // Processing Array of Objects
      ArrayList<Map<String, String>> arrOfObj = M.jsonAofObj(json);
      if (arrOfObj == null) throw new DAOException("getLands - Null Json Array");
      posts = arrOfObj;
    } catch (DAOException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return posts;
  }

  public ArrayList<Map<String, String>> getSeeds(String username) throws DAOException {
    ArrayList<Map<String, String>> posts = null;
    try {
      String json = conn.connect("/farmers/seeds?username=" + username.replace(" ", "%20"));
      if (json == null) throw new DAOException("getSeeds - connection error");

      // Processing Array of Objects
      ArrayList<Map<String, String>> arrOfObj = M.jsonAofObj(json);
      if (arrOfObj == null) throw new DAOException("getSeeds - Null Json Array");
      posts = arrOfObj;
    } catch (DAOException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return posts;
  }
}
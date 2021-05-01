package dao;

import java.net.*;
import java.util.*;
import java.io.*;

public class Connection {
  private String connectionString = "http://localhost:9999/server";
  // --- Connect ---
  // if (tomcat sends err msg) return null
  // return string got from tomcat
  public String connect(String targetURL) {
    HttpURLConnection connection = null;
    try {
      // Create connection
      URL url = new URL(this.connectionString + targetURL);
      connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      connection.setRequestProperty("Content-Language", "en-US");
      connection.setUseCaches(false);
      connection.setDoOutput(true);

      //Get Response  
      InputStream is = connection.getInputStream();
      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
      StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
      String line;
      while ((line = rd.readLine()) != null) {
        response.append(line);
        response.append('\r');
      }
      rd.close();
      String json = response.toString();
      if (json.contains("<p>")) return null;
      return json;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
  }
}
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.naming.*;
import javax.sql.*;

@WebServlet(name="/user", urlPatterns={"/user", "/user/friends", "/user/friendreqs", "/user/friendreqr", "/user/set", "/user/friends/set", "/user/friendsreq/set"})
public class User extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
    if (request.getRequestURI().equals("/server/user")) getUser(request, response);
    if (request.getRequestURI().equals("/server/user/friends")) getFriends(request, response);
    if (request.getRequestURI().equals("/server/user/friendreqs")) getFriendReqS(request, response);
    if (request.getRequestURI().equals("/server/user/friendreqr")) getFriendReqR(request, response);
    if (request.getRequestURI().equals("/server/user/set")) setUser(request, response);
    if (request.getRequestURI().equals("/server/user/friends/set")) setFriends(request, response);
    if (request.getRequestURI().equals("/server/user/friendsreq/set")) setFriendsReq(request, response);
  }

  private void setFriendsReq(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    try {
      Context initContext = new InitialContext();
      Context envContext = (Context) initContext.lookup("java:comp/env");
      DataSource ds = (DataSource) envContext.lookup("jdbc/social_magnet");
      Connection conn = ds.getConnection();

      String sqlSelect = "select * from friend_requests where sender=? and receipient=?";

      PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect);
      stmtSelect.setString(1, request.getParameter("sender"));
      stmtSelect.setString(2, request.getParameter("receipient"));
      ResultSet rs = stmtSelect.executeQuery();

      if (rs.next()) {
        String sqlUpdate = "delete from friend_requests where sender=? and receipient=?";
        PreparedStatement stmt = conn.prepareStatement(sqlUpdate);
        stmt.setString(1, request.getParameter("sender"));
        stmt.setString(2, request.getParameter("receipient"));
        int rows = stmt.executeUpdate();
        out.println("rows_deleted:" + rows + "//");
      } else {
        // INSERT if username and crop doesnt exists
        String sqlInsert = "insert into friend_requests(sender, receipient) values(?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sqlInsert);
        stmt.setString(1, request.getParameter("sender"));
        stmt.setString(2, request.getParameter("receipient"));
        int rows = stmt.executeUpdate();
        out.println("rows_inserted:" + rows + "//");
      }
      if (conn != null) conn.close();
    } catch (Exception ex) {
      out.println("<p>Error: " + ex.getMessage() + "</p>");
      out.println("<p>Check Tomcat console for details.</p>");
      ex.printStackTrace();
    }
    out.close();
  }

  private void setUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    try {
      Context initContext = new InitialContext();
      Context envContext = (Context) initContext.lookup("java:comp/env");
      DataSource ds = (DataSource) envContext.lookup("jdbc/social_magnet");
      Connection conn = ds.getConnection();

      String sqlSelect = "select * from users where username=?";

      PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect);
      stmtSelect.setString(1, request.getParameter("username"));
      ResultSet rs = stmtSelect.executeQuery();

      if (rs.next()) {
        String sqlUpdate = "update users set gold=?, xp=? where username=?";
        PreparedStatement stmt = conn.prepareStatement(sqlUpdate);
        stmt.setInt(1, Integer.parseInt(request.getParameter("gold")));
        stmt.setInt(2, Integer.parseInt(request.getParameter("xp")));
        stmt.setString(3, request.getParameter("username"));
        int rows = stmt.executeUpdate();
        out.println("rows_edited:" + rows + "//");
      } else {
        // INSERT if username and crop doesnt exists
        String sqlInsert = "insert into users(username, name, password, gold, xp) values(?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sqlInsert);
        stmt.setString(1, request.getParameter("username"));
        stmt.setString(2, request.getParameter("name"));
        stmt.setString(3, request.getParameter("password"));
        stmt.setInt(4, Integer.parseInt(request.getParameter("gold")));
        stmt.setInt(5, Integer.parseInt(request.getParameter("xp")));
        int rows = stmt.executeUpdate();
        out.println("rows_inserted:" + rows + "//");
      }
      if (conn != null) conn.close();
    } catch (Exception ex) {
      out.println("<p>Error: " + ex.getMessage() + "</p>");
      out.println("<p>Check Tomcat console for details.</p>");
      ex.printStackTrace();
    }
    out.close();
  }

  private void setFriends(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    try {
      Context initContext = new InitialContext();
      Context envContext = (Context) initContext.lookup("java:comp/env");
      DataSource ds = (DataSource) envContext.lookup("jdbc/social_magnet");
      Connection conn = ds.getConnection();

      String sqlSelect = "select * from friends where (u1=? and u2=?) or (u1=? and u2=?)";

      PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect);
      stmtSelect.setString(1, request.getParameter("username1"));
      stmtSelect.setString(2, request.getParameter("username2"));
      stmtSelect.setString(3, request.getParameter("username2"));
      stmtSelect.setString(4, request.getParameter("username1"));
      ResultSet rs = stmtSelect.executeQuery();

      if (rs.next()) {
        // if friends exists delete friends
        String sqlUpdate = "delete from friends where (u1=? and u2=?) or (u1=? and u2=?)";
        PreparedStatement stmt = conn.prepareStatement(sqlUpdate);
        stmt.setString(1, request.getParameter("username1"));
        stmt.setString(2, request.getParameter("username2"));
        stmt.setString(3, request.getParameter("username2"));
        stmt.setString(4, request.getParameter("username1"));
        int rows = stmt.executeUpdate();
        out.println("rows_edited:" + rows + "//");
      } else {
        // INSERT if friends
        String sqlInsert = "insert into friends(u1, u2) values(?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sqlInsert);
        stmt.setString(1, request.getParameter("username1"));
        stmt.setString(2, request.getParameter("username2"));
        int rows = stmt.executeUpdate();
        out.println("rows_inserted:" + rows + "//");
      }
      if (conn != null) conn.close();
    } catch (Exception ex) {
      out.println("<p>Error: " + ex.getMessage() + "</p>");
      out.println("<p>Check Tomcat console for details.</p>");
      ex.printStackTrace();
    }
    out.close();
  }

  private void getFriendReqR(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    try {
      Context initContext = new InitialContext();
      Context envContext = (Context) initContext.lookup("java:comp/env");
      DataSource ds = (DataSource) envContext.lookup("jdbc/social_magnet");
      Connection conn = ds.getConnection();

      String sqlStr = "select u.username, u.name, u.password, u.gold, u.xp from friend_requests f inner join users u on (f.sender=u.username) where f.receipient=?";

      PreparedStatement stmt = conn.prepareStatement(sqlStr);
      stmt.setString(1, request.getParameter("username"));
      ResultSet rs = stmt.executeQuery();

      Json json = new Json();
      while (rs.next()) {
        json.setString("username", rs.getString("username"));
        json.setString("name", rs.getString("name"));
        json.setString("password", rs.getString("password"));
        json.setInt("gold", rs.getInt("gold"));
        json.setInt("xp", rs.getInt("xp"));
        json.closeObj();
      }
      if (conn != null) conn.close();
      out.println(json.toJsonString());
    } catch (Exception ex) {
      out.println("<p>Error: " + ex.getMessage() + "</p>");
      out.println("<p>Check Tomcat console for details.</p>");
      ex.printStackTrace();
    }
    out.close();
  }

  private void getFriendReqS(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    try {
      Context initContext = new InitialContext();
      Context envContext = (Context) initContext.lookup("java:comp/env");
      DataSource ds = (DataSource) envContext.lookup("jdbc/social_magnet");
      Connection conn = ds.getConnection();

      String sqlStr = "select u.username, u.name, u.password, u.gold, u.xp from friend_requests f inner join users u on (f.receipient=u.username) where f.sender=?";

      PreparedStatement stmt = conn.prepareStatement(sqlStr);
      stmt.setString(1, request.getParameter("username"));
      ResultSet rs = stmt.executeQuery();

      Json json = new Json();
      while (rs.next()) {
        json.setString("username", rs.getString("username"));
        json.setString("name", rs.getString("name"));
        json.setString("password", rs.getString("password"));
        json.setInt("gold", rs.getInt("gold"));
        json.setInt("xp", rs.getInt("xp"));
        json.closeObj();
      }
      if (conn != null) conn.close();
      out.println(json.toJsonString());
    } catch (Exception ex) {
      out.println("<p>Error: " + ex.getMessage() + "</p>");
      out.println("<p>Check Tomcat console for details.</p>");
      ex.printStackTrace();
    }
    out.close();
  }

  private void getFriends(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    try {
      Context initContext = new InitialContext();
      Context envContext = (Context) initContext.lookup("java:comp/env");
      DataSource ds = (DataSource) envContext.lookup("jdbc/social_magnet");
      Connection conn = ds.getConnection();

      String sqlStr = "select u.username, u.name, u.password, u.gold, u.xp from friends f inner join users u on (f.u1=u.username or f.u2=u.username) where (f.u1=? or f.u2=?) and username!=?";

      PreparedStatement stmt = conn.prepareStatement(sqlStr);
      stmt.setString(1, request.getParameter("username"));
      stmt.setString(2, request.getParameter("username"));
      stmt.setString(3, request.getParameter("username"));
      ResultSet rs = stmt.executeQuery();

      Json json = new Json();
      while (rs.next()) {
        json.setString("username", rs.getString("username"));
        json.setString("name", rs.getString("name"));
        json.setString("password", rs.getString("password"));
        json.setInt("gold", rs.getInt("gold"));
        json.setInt("xp", rs.getInt("xp"));
        json.closeObj();
      }
      if (conn != null) conn.close();
      out.println(json.toJsonString());
    } catch (Exception ex) {
      out.println("<p>Error: " + ex.getMessage() + "</p>");
      out.println("<p>Check Tomcat console for details.</p>");
      ex.printStackTrace();
    }
    out.close();
  }

  private void getUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    try {
      Context initContext = new InitialContext();
      Context envContext = (Context) initContext.lookup("java:comp/env");
      DataSource ds = (DataSource) envContext.lookup("jdbc/social_magnet");
      Connection conn = ds.getConnection();

      String sqlStr = "select * from users where username=?";

      PreparedStatement stmt = conn.prepareStatement(sqlStr);
      stmt.setString(1, request.getParameter("username"));
      ResultSet rs = stmt.executeQuery();

      Json json = new Json();
      while (rs.next()) {
        json.setString("username", rs.getString("username"));
        json.setString("name", rs.getString("name"));
        json.setString("password", rs.getString("password"));
        json.setInt("gold", rs.getInt("gold"));
        json.setInt("xp", rs.getInt("xp"));
        json.closeObj();
      }
      if (conn != null) conn.close();
      out.println(json.toJsonString());
    } catch (Exception ex) {
      out.println("<p>Error: " + ex.getMessage() + "</p>");
      out.println("<p>Check Tomcat console for details.</p>");
      ex.printStackTrace();
    }
    out.close();
  }
}
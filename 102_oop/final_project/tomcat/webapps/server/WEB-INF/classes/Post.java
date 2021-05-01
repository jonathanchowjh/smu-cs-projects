import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.naming.*;
import javax.sql.*;

@WebServlet(name="/posts", urlPatterns={"/posts/user", "/posts/dst", "/posts/likes", "/posts/replies", "/posts/gifts", "/posts/giftsGifter", "/posts/set", "/posts/likes/set", "/posts/replies/set", "/posts/gifts/set", "/posts/kill/set"})
public class Post extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
    if (request.getRequestURI().equals("/server/posts/user")) getPosts(request, response);
    if (request.getRequestURI().equals("/server/posts/dst")) getPostsDst(request, response);
    if (request.getRequestURI().equals("/server/posts/likes")) getLikes(request, response);
    if (request.getRequestURI().equals("/server/posts/replies")) getReplies(request, response);
    if (request.getRequestURI().equals("/server/posts/gifts")) getGifts(request, response);
    if (request.getRequestURI().equals("/server/posts/giftsGifter")) getGiftsGifter(request, response);
    if (request.getRequestURI().equals("/server/posts/set")) setPosts(request, response);
    if (request.getRequestURI().equals("/server/posts/likes/set")) setLikes(request, response);
    if (request.getRequestURI().equals("/server/posts/replies/set")) setReplies(request, response);
    if (request.getRequestURI().equals("/server/posts/gifts/set")) setGifts(request, response);
    if (request.getRequestURI().equals("/server/posts/kill/set")) setKill(request, response);
  }

  private void setKill(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    try {
      Context initContext = new InitialContext();
      Context envContext = (Context) initContext.lookup("java:comp/env");
      DataSource ds = (DataSource) envContext.lookup("jdbc/social_magnet");
      Connection conn = ds.getConnection();

      String sqlInsert = "insert into posts_kill(pid, username) values(?, ?)";
      PreparedStatement stmt = conn.prepareStatement(sqlInsert);
      stmt.setInt(1, Integer.parseInt(request.getParameter("pid")));
      stmt.setString(2, request.getParameter("username"));
      int rows = stmt.executeUpdate();
      out.println("rows_inserted:" + rows + "//");

      if (conn != null) conn.close();
    } catch (Exception ex) {
      out.println("<p>Error: " + ex.getMessage() + "</p>");
      out.println("<p>Check Tomcat console for details.</p>");
      ex.printStackTrace();
    }
    out.close();
  }

  private void setGifts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    try {
      Context initContext = new InitialContext();
      Context envContext = (Context) initContext.lookup("java:comp/env");
      DataSource ds = (DataSource) envContext.lookup("jdbc/social_magnet");
      Connection conn = ds.getConnection();

      if (!request.getParameter("gid").equals("")) {
        String sqlUpdate = "update gifts set accepted=? where gid=?";
        PreparedStatement stmt = conn.prepareStatement(sqlUpdate);
        stmt.setBoolean(1, Boolean.parseBoolean(request.getParameter("accepted")));
        stmt.setInt(2, Integer.parseInt(request.getParameter("gid")));
        int rows = stmt.executeUpdate();
        out.println("rows_edited:" + rows + "//");
      } else {
        // INSERT if username and crop doesnt exists
        String sqlInsert = "insert into gifts(gifter, giftee, crop, accepted, dt) values(?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sqlInsert);
        stmt.setString(1, request.getParameter("gifter"));
        stmt.setString(2, request.getParameter("giftee"));
        stmt.setString(3, request.getParameter("crop"));
        stmt.setBoolean(4, false);
        stmt.setTimestamp(5, new Timestamp(Long.parseLong(request.getParameter("dt"))));
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

  private void setReplies(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    try {
      Context initContext = new InitialContext();
      Context envContext = (Context) initContext.lookup("java:comp/env");
      DataSource ds = (DataSource) envContext.lookup("jdbc/social_magnet");
      Connection conn = ds.getConnection();

      if (!request.getParameter("rid").equals("")) {
        String sqlUpdate = "delete from replies where rid=?";
        PreparedStatement stmt = conn.prepareStatement(sqlUpdate);
        stmt.setInt(1, Integer.parseInt(request.getParameter("rid")));
        int rows = stmt.executeUpdate();
        out.println("rows_deleted:" + rows + "//");
      } else {
        // INSERT if username and crop doesnt exists
        String sqlInsert = "insert into replies(pid, username, reply, dt) values(?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sqlInsert);
        stmt.setInt(1, Integer.parseInt(request.getParameter("pid")));
        stmt.setString(2, request.getParameter("username"));
        stmt.setString(3, request.getParameter("reply"));
        stmt.setTimestamp(4, new Timestamp(Long.parseLong(request.getParameter("dt"))));
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

  private void setLikes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    try {
      Context initContext = new InitialContext();
      Context envContext = (Context) initContext.lookup("java:comp/env");
      DataSource ds = (DataSource) envContext.lookup("jdbc/social_magnet");
      Connection conn = ds.getConnection();

      String sqlSelect = "select * from likes where pid=? and username=?";

      PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect);
      stmtSelect.setInt(1, Integer.parseInt(request.getParameter("pid")));
      stmtSelect.setString(2, request.getParameter("username"));
      ResultSet rs = stmtSelect.executeQuery();

      if (rs.next()) {
        String sqlUpdate = "update likes set isLike=? where pid=? and username=?";
        PreparedStatement stmt = conn.prepareStatement(sqlUpdate);
        stmt.setBoolean(1, Boolean.parseBoolean(request.getParameter("isLike")));
        stmt.setInt(2, Integer.parseInt(request.getParameter("pid")));
        stmt.setString(3, request.getParameter("username"));
        int rows = stmt.executeUpdate();
        out.println("rows_edited:" + rows + "//");
      } else {
        // INSERT if username and crop doesnt exists
        String sqlInsert = "insert into likes(pid, username, isLike) values(?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sqlInsert);
        stmt.setInt(1, Integer.parseInt(request.getParameter("pid")));
        stmt.setString(2, request.getParameter("username"));
        stmt.setBoolean(3, Boolean.parseBoolean(request.getParameter("isLike")));
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

  private void setPosts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    try {
      Context initContext = new InitialContext();
      Context envContext = (Context) initContext.lookup("java:comp/env");
      DataSource ds = (DataSource) envContext.lookup("jdbc/social_magnet");
      Connection conn = ds.getConnection();

      String dst_username = request.getParameter("dst_username");
      if (!request.getParameter("pid").equals("")) {
        String sqlUpdatePosts = "delete from posts where pid=?";
        String sqlUpdateLikes = "delete from likes where pid=?";
        String sqlUpdateReplies = "delete from replies where pid=?";
        PreparedStatement stmtPosts = conn.prepareStatement(sqlUpdatePosts);
        PreparedStatement stmtLikes = conn.prepareStatement(sqlUpdateLikes);
        PreparedStatement stmtReplies = conn.prepareStatement(sqlUpdateReplies);
        stmtPosts.setInt(1, Integer.parseInt(request.getParameter("pid")));
        stmtLikes.setInt(1, Integer.parseInt(request.getParameter("pid")));
        stmtReplies.setInt(1, Integer.parseInt(request.getParameter("pid")));
        int rows_likes = stmtLikes.executeUpdate();
        int rows_replies = stmtReplies.executeUpdate();
        int rows_posts = stmtPosts.executeUpdate();
        out.println("rows_deleted_posts:" + rows_posts + ",rows_deleted_likes:" + rows_likes + ",rows_deleted_replies:" + rows_replies + "//");
      } else if (dst_username.equals("NULL")) {
        String sqlUdate = "update posts set dst_username=? where pid=?";
        PreparedStatement stmt = conn.prepareStatement(sqlUdate);
        stmt.setNull(1, java.sql.Types.NULL);
        stmt.setString(2, request.getParameter("pid"));
        int rows = stmt.executeUpdate();
        out.println("rows_edited:" + rows + "//");
        
      } else {
        // INSERT if username and crop doesnt exists
        String sqlInsert = "insert into posts(username, dst_username, post, dt) values(?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sqlInsert);
        stmt.setString(1, request.getParameter("username"));
        stmt.setString(2, dst_username);
        stmt.setString(3, request.getParameter("post"));
        stmt.setTimestamp(4, new Timestamp(Long.parseLong(request.getParameter("dt"))));
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

  private void getGiftsGifter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    try {
      Context initContext = new InitialContext();
      Context envContext = (Context) initContext.lookup("java:comp/env");
      DataSource ds = (DataSource) envContext.lookup("jdbc/social_magnet");
      Connection conn = ds.getConnection();

      String sqlStr = "select * from gifts where gifter=?";

      PreparedStatement stmt = conn.prepareStatement(sqlStr);
      stmt.setString(1, request.getParameter("username"));
      ResultSet rs = stmt.executeQuery();

      Json json = new Json();
      while (rs.next()) {
        json.setInt("gid", rs.getInt("gid"));
        json.setString("gifter", rs.getString("gifter"));
        json.setString("giftee", rs.getString("giftee"));
        json.setString("crop", rs.getString("crop"));
        json.setString("accepted", Boolean.toString(rs.getBoolean("accepted")));
        json.setTimestamp("dt", rs.getTimestamp("dt"));
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

  private void getGifts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    try {
      Context initContext = new InitialContext();
      Context envContext = (Context) initContext.lookup("java:comp/env");
      DataSource ds = (DataSource) envContext.lookup("jdbc/social_magnet");
      Connection conn = ds.getConnection();

      String sqlStr = "select * from gifts where giftee=?";

      PreparedStatement stmt = conn.prepareStatement(sqlStr);
      stmt.setString(1, request.getParameter("username"));
      ResultSet rs = stmt.executeQuery();

      Json json = new Json();
      while (rs.next()) {
        json.setInt("gid", rs.getInt("gid"));
        json.setString("gifter", rs.getString("gifter"));
        json.setString("giftee", rs.getString("giftee"));
        json.setString("crop", rs.getString("crop"));
        json.setString("accepted", Boolean.toString(rs.getBoolean("accepted")));
        json.setTimestamp("dt", rs.getTimestamp("dt"));
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

  private void getReplies(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    try {
      Context initContext = new InitialContext();
      Context envContext = (Context) initContext.lookup("java:comp/env");
      DataSource ds = (DataSource) envContext.lookup("jdbc/social_magnet");
      Connection conn = ds.getConnection();

      String sqlStr = "select * from replies where pid=?";

      PreparedStatement stmt = conn.prepareStatement(sqlStr);
      stmt.setInt(1, Integer.parseInt(request.getParameter("pid")));
      ResultSet rs = stmt.executeQuery();

      Json json = new Json();
      while (rs.next()) {
        json.setInt("rid", rs.getInt("rid"));
        json.setInt("pid", rs.getInt("pid"));
        json.setString("username", rs.getString("username"));
        json.setString("reply", rs.getString("reply"));
        json.setTimestamp("dt", rs.getTimestamp("dt"));
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

  private void getLikes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    try {
      Context initContext = new InitialContext();
      Context envContext = (Context) initContext.lookup("java:comp/env");
      DataSource ds = (DataSource) envContext.lookup("jdbc/social_magnet");
      Connection conn = ds.getConnection();

      String sqlStr = "select * from likes where pid=?";

      PreparedStatement stmt = conn.prepareStatement(sqlStr);
      stmt.setInt(1, Integer.parseInt(request.getParameter("pid")));
      ResultSet rs = stmt.executeQuery();

      Json json = new Json();
      while (rs.next()) {
        json.setInt("pid", rs.getInt("pid"));
        json.setString("username", rs.getString("username"));
        json.setString("isLike", Boolean.toString(rs.getBoolean("isLike")));
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

  private void getPostsDst(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    try {
      Context initContext = new InitialContext();
      Context envContext = (Context) initContext.lookup("java:comp/env");
      DataSource ds = (DataSource) envContext.lookup("jdbc/social_magnet");
      Connection conn = ds.getConnection();

      String sqlStr = "select p.pid, p.post, p.dt, p.username, p.dst_username from posts p left outer join posts_kill pk on p.pid=pk.pid where (p.dst_username=? or p.post like ?) and (pk.username is null or pk.username!=?)";

      PreparedStatement stmt = conn.prepareStatement(sqlStr);
      stmt.setString(1, request.getParameter("username"));
      stmt.setString(2, "%@" + request.getParameter("username") + "%");
      stmt.setString(3, request.getParameter("username"));
      ResultSet rs = stmt.executeQuery();

      Json json = new Json();
      while (rs.next()) {
        json.setInt("pid", rs.getInt("pid"));
        json.setString("username", rs.getString("username"));
        json.setString("dst_username", rs.getString("dst_username"));
        json.setString("post", rs.getString("post"));
        json.setTimestamp("dt", rs.getTimestamp("dt"));
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

  private void getPosts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    try {
      Context initContext = new InitialContext();
      Context envContext = (Context) initContext.lookup("java:comp/env");
      DataSource ds = (DataSource) envContext.lookup("jdbc/social_magnet");
      Connection conn = ds.getConnection();

      String sqlStr = "select p.pid, p.post, p.dt, p.username, p.dst_username from posts p left outer join posts_kill pk on p.pid=pk.pid where (p.username=? or p.post like ?) and (pk.username is null or pk.username!=?)";

      PreparedStatement stmt = conn.prepareStatement(sqlStr);
      stmt.setString(1, request.getParameter("username"));
      stmt.setString(2, "%@" + request.getParameter("username") + "%");
      stmt.setString(3, request.getParameter("username"));
      ResultSet rs = stmt.executeQuery();

      Json json = new Json();
      while (rs.next()) {
        json.setInt("pid", rs.getInt("pid"));
        json.setString("username", rs.getString("username"));
        json.setString("dst_username", rs.getString("dst_username"));
        json.setString("post", rs.getString("post"));
        json.setTimestamp("dt", rs.getTimestamp("dt"));
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
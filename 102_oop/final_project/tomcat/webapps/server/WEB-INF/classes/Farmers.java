import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.naming.*;
import javax.sql.*;

@WebServlet(name="/farmers", urlPatterns={"/farmers/lands", "/farmers/seeds", "/farmers/seeds/set", "/farmers/lands/set"})
public class Farmers extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
    if (request.getRequestURI().equals("/server/farmers/lands")) getLands(request, response);
    if (request.getRequestURI().equals("/server/farmers/seeds")) getSeeds(request, response);
    if (request.getRequestURI().equals("/server/farmers/seeds/set")) setSeeds(request, response);
    if (request.getRequestURI().equals("/server/farmers/lands/set")) setLands(request, response);
  }

  private void setLands(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    try {
      Context initContext = new InitialContext();
      Context envContext = (Context) initContext.lookup("java:comp/env");
      DataSource ds = (DataSource) envContext.lookup("jdbc/social_magnet");
      Connection conn = ds.getConnection();

      if (!request.getParameter("lid").equals("")) {
        String sqlUpdate = "update lands set crop=?, startTime=? where lid=? and username=?";
        PreparedStatement stmt = conn.prepareStatement(sqlUpdate);

        if (request.getParameter("crop").equals("")) {
          stmt.setNull(1, java.sql.Types.INTEGER);
        } else {
          stmt.setString(1, request.getParameter("crop"));
        }
        stmt.setTimestamp(2, new Timestamp(Long.parseLong(request.getParameter("startTime"))));
        stmt.setInt(3, Integer.parseInt(request.getParameter("lid")));
        stmt.setString(4, request.getParameter("username"));
        int rows = stmt.executeUpdate();
        out.println("rows_edited:" + rows + "//");
      } else {
        // INSERT if username and crop doesnt exists
        String sqlInsert = "insert into lands(username, crop, startTime) values(?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sqlInsert);
        stmt.setString(1, request.getParameter("username"));
        stmt.setString(2, request.getParameter("crop"));
        stmt.setTimestamp(3, new Timestamp(Long.parseLong(request.getParameter("startTime"))));
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

  private void setSeeds(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    try {
      Context initContext = new InitialContext();
      Context envContext = (Context) initContext.lookup("java:comp/env");
      DataSource ds = (DataSource) envContext.lookup("jdbc/social_magnet");
      Connection conn = ds.getConnection();

      String sqlSelect = "select * from seeds where username=? and crop=?";

      PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect);
      stmtSelect.setString(1, request.getParameter("username"));
      stmtSelect.setString(2, request.getParameter("crop"));
      ResultSet rs = stmtSelect.executeQuery();

      if (rs.next()) {
        String sqlUpdate = "update seeds set quantity=? where username=? and crop=?";
        PreparedStatement stmt = conn.prepareStatement(sqlUpdate);
        stmt.setInt(1, Integer.parseInt(request.getParameter("quantity")));
        stmt.setString(2, request.getParameter("username"));
        stmt.setString(3, request.getParameter("crop"));
        int rows = stmt.executeUpdate();
        out.println("rows_edited:" + rows + "//");
      } else {
        // INSERT if username and crop doesnt exists
        String sqlInsert = "insert into seeds(quantity, username, crop) values(?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sqlInsert);
        stmt.setInt(1, Integer.parseInt(request.getParameter("quantity")));
        stmt.setString(2, request.getParameter("username"));
        stmt.setString(3, request.getParameter("crop"));
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

  private void getSeeds(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    try {
      Context initContext = new InitialContext();
      Context envContext = (Context) initContext.lookup("java:comp/env");
      DataSource ds = (DataSource) envContext.lookup("jdbc/social_magnet");
      Connection conn = ds.getConnection();

      String sqlStr = "select * from seeds where username=?";

      PreparedStatement stmt = conn.prepareStatement(sqlStr);
      stmt.setString(1, request.getParameter("username"));
      ResultSet rs = stmt.executeQuery();

      Json json = new Json();
      while (rs.next()) {
        json.setInt("quantity", rs.getInt("quantity"));
        json.setString("username", rs.getString("username"));
        json.setString("crop", rs.getString("crop"));
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

  private void getLands(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    try {
      Context initContext = new InitialContext();
      Context envContext = (Context) initContext.lookup("java:comp/env");
      DataSource ds = (DataSource) envContext.lookup("jdbc/social_magnet");
      Connection conn = ds.getConnection();

      String sqlStr = "select * from lands where username=?";

      PreparedStatement stmt = conn.prepareStatement(sqlStr);
      stmt.setString(1, request.getParameter("username"));
      ResultSet rs = stmt.executeQuery();

      Json json = new Json();
      while (rs.next()) {
        json.setInt("lid", rs.getInt("lid"));
        json.setString("username", rs.getString("username"));
        json.setString("crop", rs.getString("crop"));
        json.setTimestamp("startTime", rs.getTimestamp("startTime"));
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
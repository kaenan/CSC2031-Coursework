import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;


@WebServlet("/DBOutput")
public class DBOutput extends HttpServlet {

    private Connection conn;
    private Statement stmt;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // MySql database connection info
        String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        String USER = "user";
        String PASS = "password";

        // URLs to connect to database depending on your development approach
        // (NOTE: please change to option 1 when submitting)

        // 1. use this when running everything in Docker using docker-compose
        //String DB_URL = "jdbc:mysql://db:3306/lottery";

        // 2. use this when running tomcat server locally on your machine and mysql database server in Docker
        String DB_URL = "jdbc:mysql://localhost:33333/lottery";

        try {
            // create database connection and statement
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            // get parameter from admin_home.jsp
            String adminDB = request.getParameter("db");

            // check parameter value
            if (adminDB.equals("View Database")){
                // query database and get results
                ResultSet rs = stmt.executeQuery("SELECT Firstname, Lastname, Email, Phone, Username FROM userAccounts");
                // create HTML table text
                String content = "<table border='1' cellspacing='2' cellpadding='2' width='100%' align='left'>" +
                        "<tr><th>First name</th><th>Last name</th><th>Email</th><th>Phone number</th><th>Username</th></tr>";
                // add HTML table data using data from database excluding hashed password
                while (rs.next()) {
                    content += "<tr><td>" + rs.getString("Firstname") + "</td>" +
                            "<td>" + rs.getString("Lastname") + "</td>" +
                            "<td>" + rs.getString("Email") + "</td>" +
                            "<td>" + rs.getString("Phone") + "</td>" +
                            "<td>" + rs.getString("Username") + "</td></tr>";
                }
                // finish HTML table text
                content += "</table>";
                // close connection
                conn.close();
                // display output.jsp page with given content above if successful
                RequestDispatcher dispatcher = request.getRequestDispatcher("admin/admin_home.jsp");
                request.setAttribute("data", content);
                dispatcher.forward(request, response);

                // if the request for database has come from another page
            }else {
                // query database and get results
                ResultSet rs = stmt.executeQuery("SELECT * FROM userAccounts");
                // create HTML table text
                String content = "<table border='1' cellspacing='2' cellpadding='2' width='100%' align='left'>" +
                        "<tr><th>First name</th><th>Last name</th><th>Email</th><th>Phone number</th><th>Username</th><th>Password</th></tr>";
                // add HTML table data using data from database
                while (rs.next()) {
                    content += "<tr><td>" + rs.getString("Firstname") + "</td>" +
                            "<td>" + rs.getString("Lastname") + "</td>" +
                            "<td>" + rs.getString("Email") + "</td>" +
                            "<td>" + rs.getString("Phone") + "</td>" +
                            "<td>" + rs.getString("Username") + "</td>" +
                            "<td>" + rs.getString("Pwd") + "</td></tr>";
                }
                // finish HTML table text
                content += "</table>";
                // close connection
                conn.close();
                // display output.jsp page with given content above if successful
                RequestDispatcher dispatcher = request.getRequestDispatcher("/output.jsp");
                request.setAttribute("message", "Database");
                request.setAttribute("data", content);
                dispatcher.forward(request, response);
            }
        } catch (Exception se) {
            se.printStackTrace();
            // display error.jsp page with given message if successful
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            request.setAttribute("message", "Database Error, Please try again");
            dispatcher.forward(request, response);
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

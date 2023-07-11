import notservlets.PassEncrypt;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/UserLogin")
public class UserLogin extends HttpServlet {

    private Connection conn;
    private Statement stmt;
    int checkif = 0;
    int loginAttempts = 0;
    String loginMessage;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // MySql database connection info
        String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        String USER = "user";
        String PASS = "password";

        // URLs to connect to database depending on your development approach
        // (NOTE: please change to option 1 when submitting)

        // 1. use this when running everything in Docker using docker-compose
  //      String DB_URL = "jdbc:mysql://db:3306/lottery";

        // 2. use this when running tomcat server locally on your machine and mysql database server in Docker
        String DB_URL = "jdbc:mysql://localhost:33333/lottery";

        try {
            // create database connection and statement
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            // create PassEncrypt to compare password with hashed passwords stored
            PassEncrypt passCheck = new PassEncrypt();
            // get user inputted parameters from login
            String username = request.getParameter("username");
            String password = passCheck.Encrypt(request.getParameter("password"));

            // query database and get results
            ResultSet rs = stmt.executeQuery("SELECT * FROM userAccounts");

            // check each row to see if there is a matching username and hashed password
                while (rs.next()) {
                    if (rs.getString("Username").equals(username) && rs.getString("Pwd").equals(password)) {
                        checkif ++;
                        break;
                    }
                }
                // if a match
                if (checkif == 1) {
                    checkif = 0;
                    // check if there is already a session and if there is remove all attributes except keypair
                    HttpSession oldSessions = request.getSession(false);
                    if (oldSessions !=null){
                        oldSessions.removeAttribute("firstname");
                        oldSessions.removeAttribute("lastname");
                        oldSessions.removeAttribute("email");
                        oldSessions.removeAttribute("username");
                        oldSessions.removeAttribute("hashedpassword");
                    }
                    // create new sessions and set attributes
                    HttpSession newSession = request.getSession();
                    newSession.setAttribute("firstname", rs.getString("Firstname"));
                    newSession.setAttribute("lastname", rs.getString("Lastname"));
                    newSession.setAttribute("email", rs.getString("Email"));
                    newSession.setAttribute("username", rs.getString("Username"));
                    newSession.setAttribute("hashedpassword", rs.getString("Pwd"));
                    // close connection
                    conn.close();
                    // if user is logging in as an admin role
                    if (username.equals("admin")){
                        // display admin_home.jsp page if the user is an admin
                        RequestDispatcher dispatcher = request.getRequestDispatcher("admin/admin_home.jsp");
                        request.setAttribute("message", "You have successfully logged in as an admin");
                        dispatcher.forward(request, response);
                        // if user is logging in as a public role
                    }else {
                        // display account.jsp page if login is successful
                        RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
                        request.setAttribute("message", "You have successfully logged in");
                        dispatcher.forward(request, response);
                    }
                    // if user logging in parameters have no match with database
                } else if (checkif == 0){
                    //begin login attempts
                    if (loginAttempts != 3) {
                        loginAttempts = loginAttempts + 1;
                    } else {
                        loginAttempts = 0;
                    }
                    // create new sessions and sets number of attempts remaining
                    HttpSession newSession = request.getSession();
                    newSession.setAttribute("loginAttempts", loginAttempts);
                    // display index.jsp page with given case of attempts left
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
                    switch (loginAttempts){
                        case 1:
                            loginMessage = "2 More login attempts remaining";
                            request.setAttribute("attempts", loginMessage);
                            break;
                        case 2:
                            loginMessage = "1 More login attempts remaining";
                            request.setAttribute("attempts", loginMessage);
                            break;
                        case 3:
                            loginMessage = "No more login attempts remaining, please try again later";
                            request.setAttribute("attempts", loginMessage);
                            break;
                        default:
                            loginMessage = null;
                            request.setAttribute("attempts", loginMessage);
                    }
                    request.setAttribute("message", "Login Failed, please try again.");
                    dispatcher.forward(request, response);
                }
        } catch (Exception se) {
            se.printStackTrace();
            // display error.jsp page with given message if successful
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            request.setAttribute("message", "Login Failed, please try again.");
            dispatcher.forward(request, response);
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
                se2.printStackTrace();
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

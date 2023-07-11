import notservlets.PassEncrypt;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;

@WebServlet("/CreateAccount")
public class CreateAccount extends HttpServlet {

    private Connection conn;
    private PreparedStatement stmt;

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

        // get parameter data that was submitted in HTML form (use form attributes 'name')
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("roles");

        try{
            // create database connection and statement
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //userAccounts table must have 'role' column added
            // Create sql query
            String query = "INSERT INTO userAccounts (Firstname, Lastname, Email, Phone, Username, Pwd, Role)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?)";

            // hash inputted password to be stored
            PassEncrypt pss = new PassEncrypt();
            String hashpass = pss.Encrypt(password);

            // set values into SQL query statement
            stmt = conn.prepareStatement(query);
            stmt.setString(1,firstname);
            stmt.setString(2,lastname);
            stmt.setString(3,email);
            stmt.setString(4,phone);
            stmt.setString(5,username);
            stmt.setString(6,hashpass);
            stmt.setString(7,role);

            // execute query and close connection
            stmt.execute();
            conn.close();

            // check if there is already a session
            HttpSession oldSessions = request.getSession(false);
            if (oldSessions !=null){
                oldSessions.invalidate();
            }
            // create new sessions and set attributes using parameters from user inputs
            HttpSession newSession = request.getSession();
            newSession.setAttribute("firstname", firstname);
            newSession.setAttribute("lastname", lastname);
            newSession.setAttribute("email", email);
            newSession.setAttribute("username", username);
            newSession.setAttribute("hashedpassword", hashpass);

            if (role.equals("admin")){
                // display admin_home page if user has registered as an admin with given message if successful
                RequestDispatcher dispatcher = request.getRequestDispatcher("admin/admin_home.jsp");
                request.setAttribute("message", "You have successfully created an admin account");
                dispatcher.forward(request, response);
            }else {
                // display account.jsp page with given message if successful
                RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
                request.setAttribute("message", firstname + ", you have successfully created an account");
                dispatcher.forward(request, response);
            }
        } catch(Exception se){
            se.printStackTrace();
            // display error.jsp page with given message if unsuccessful
            RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
            request.setAttribute("message", "This username/password combination already exists. Please try again");
            dispatcher.forward(request, response);
        }
        finally{
            try{
                if(stmt!=null)
                    stmt.close();
            }
            catch(SQLException se2){}
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

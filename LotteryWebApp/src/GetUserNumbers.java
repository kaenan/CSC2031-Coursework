import javax.crypto.Cipher;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.sql.*;
import java.util.Arrays;

@WebServlet("/GetUserNumbers")
public class GetUserNumbers extends HttpServlet {
    private Connection conn;
    private Statement stmt;
    String winningNumbers;
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

        // MySql database connection info
        String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        String USER = "user";
        String PASS = "password";

        // URLs to connect to database depending on your development approach
        // 1. use this when running everything in Docker using docker-compose
        //String DB_URL = "jdbc:mysql://db:3306/lottery";

        // 2. use this when running tomcat server locally on your machine and mysql database server in Docker
        String DB_URL = "jdbc:mysql://localhost:33333/lottery";

        try {
            // create database connection and statement
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            // query database and get results
            ResultSet rs = stmt.executeQuery("SELECT draws FROM lotteryDraws");

            // get winning draw from database
            while (rs.next()) {
                winningNumbers = rs.getString("draws");
            }
            // close connection
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            // request http session for attributes
            HttpSession session = req.getSession();

            // create file path to access users draws
            String directory = "encryptedfiles/";
            String nameFile = session.getAttribute("hashedpassword").toString();
            String filepath = directory + nameFile.substring(0, 20) + ".txt";

            // get session attributes keypair and cipher
            KeyPair pair = (KeyPair) session.getAttribute("keypair");
            Cipher cipher = (Cipher) session.getAttribute("cipher");

            // get bytes from file path and using key pair put in decrypt mode
            byte[] readfile = Files.readAllBytes(Paths.get(filepath));
            cipher.init(Cipher.DECRYPT_MODE, pair.getPrivate());

            // reads each draw looking at 256 byte chunks, decrypts and adds to string array
            int ii = 256;
            String[] decipheredStrings = new String[0];
            for (int i=0; i < readfile.length; i+=256){
                byte[] decipher = cipher.doFinal(Arrays.copyOfRange(readfile, i, ii));
                String deciphered = new String(decipher, StandardCharsets.UTF_8);
                String[] ds = new String[decipheredStrings.length + 1];
                for (int x=0; x < decipheredStrings.length; x++){
                    ds[x] = decipheredStrings[x];
                }
                ds[decipheredStrings.length] = deciphered;
                decipheredStrings = ds;
                ii+=256;
            }

            // display account.jsp page with given message, draws and winning draw if successful
            RequestDispatcher dispatcher = req.getRequestDispatcher("/account.jsp");
            req.setAttribute("message", "Draws Displayed");
            req.setAttribute("draws", decipheredStrings);
            req.setAttribute("winningNumbers", winningNumbers);
            dispatcher.forward(req, resp);
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}
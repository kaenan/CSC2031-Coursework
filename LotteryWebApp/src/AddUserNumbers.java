import javax.crypto.Cipher;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;

@WebServlet("/AddUserNumbers")
public class AddUserNumbers extends HttpServlet {
    public KeyPair pair;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       try {
           // get each string and input into one string
           String numbers1 = request.getParameter("num1") + ", "+
                   request.getParameter("num2") + ", "+
                   request.getParameter("num3") + ", "+
                   request.getParameter("num4") + ", "+
                   request.getParameter("num5") + ", "+
                   request.getParameter("num6");

           // create a file path to directory with first 20 characters of hashed password as file name
           HttpSession session = request.getSession();
           String hsh = session.getAttribute("hashedpassword").toString();
           String filepath = "encryptedfiles";
           String file = filepath +"/" + hsh.substring(0, 20) + ".txt";

           File direc = new File("encryptedfiles");
           if (! direc.exists()){
               direc.mkdir();
           }

           // check if there is already a keypair in use, if not create one
           if (session.getAttribute("keypair") == null){
               KeyPairGenerator kp = KeyPairGenerator.getInstance("RSA");
               pair = kp.generateKeyPair();
               session.setAttribute("keypair", pair);
           }else {
               pair = (KeyPair) session.getAttribute("keypair");
           }

           // encrypt string of draws using keypair and cipher
           PublicKey publicKey = pair.getPublic();
           Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
           cipher.init(Cipher.ENCRYPT_MODE, publicKey);
           cipher.update(numbers1.getBytes());
           byte[] ciphered = cipher.doFinal();
           session.setAttribute("cipher", cipher);

           // output encrypted string to file output
           OutputStream os = new FileOutputStream(file, true);
           os.write(ciphered);
           os.flush();
           os.close();

           // display account page with success message that draw has been added
           RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
           request.setAttribute("message", "Draw added");
           dispatcher.forward(request, response);

       }catch (Exception e){
           e.printStackTrace();
       }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

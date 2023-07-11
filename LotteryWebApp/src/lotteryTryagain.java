import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@WebServlet("/lotteryTryagain")
public class lotteryTryagain extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // request http session
        HttpSession session = request.getSession();
        // create file path to remove
        String directory = "encryptedfiles/";
        String nameFile = session.getAttribute("hashedpassword").toString();
        String filepath = directory + nameFile.substring(0, 20) + ".txt";
        // removes file from directory
        Files.delete(Paths.get(filepath));

        // display account.jsp page with given message if successful
        RequestDispatcher dispatcher = request.getRequestDispatcher("/account.jsp");
        request.setAttribute("message", " ");
        dispatcher.forward(request, response);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebFilter(filterName = "InputFilter")
public class InputFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        boolean invalid = false;

        //Pattern checked against user inputs
        Pattern pattern = Pattern.compile("([<>!{}])|(insert)|(into)|(where)|(script)|(delete)|(input)", Pattern.CASE_INSENSITIVE);
        Map params = req.getParameterMap();

        //Checks each input against the pattern
        if (params != null){
            Iterator iter = params.keySet().iterator();
            while (iter.hasNext()){
                String key = (String) iter.next();
                String[] values = (String[]) params.get(key);
                for (String s : values) {
                    Matcher matcher2 = pattern.matcher(s);
                    if (matcher2.find()) {
                        invalid = true;
                        break;
                    }
                }
               if (invalid){
                   break;
               }
            }
        }
        //Dispatches the user to the page depending on pattern outcome
        if (invalid){
            try {
                RequestDispatcher dispatcher = req.getRequestDispatcher("/error.jsp");
                req.setAttribute("message", "You have entered invalid characters, please try again");
                dispatcher.forward(req, resp);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }else {
            chain.doFilter(req, resp);
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }
}

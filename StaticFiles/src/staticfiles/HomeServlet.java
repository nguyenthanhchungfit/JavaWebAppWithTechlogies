/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staticfiles;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Nguyen Thanh Chung
 */
public class HomeServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/html");
        String html = "<!DOCTYPE>"
                + "<html>"
                + "<head>"
                + "<link rel=\"stylesheet\" type=\"text/css\" href=\"public\\css\\style.css\">"
                + "<head>"
                +"<body>"
                + "<h1>Welcome to My World!</h1>"
                + "<body>"
                + "</html>";
        resp.getWriter().println(html);
        
    }
    
}

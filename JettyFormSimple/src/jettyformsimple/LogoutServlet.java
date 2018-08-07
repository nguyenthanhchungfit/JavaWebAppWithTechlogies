/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jettyformsimple;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author cpu11165-local
 */
public class LogoutServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if(SessionsManager.isExistedSession(session.getId())){
            SessionsManager.removeSession(session.getId());
            session.invalidate();
            
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("text/html");
            resp.sendRedirect("/login");
            
        }        
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jettyformsimple;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;



/**
 *
 * @author Nguyen Thanh Chung
 */
public class JettyServer {
    
    public static void main(String[] args) throws Exception {
        Server server = new Server();
        
        // HTTP Connector
        
        ServerConnector http = new ServerConnector(server);
        http.setHost("localhost");
        http.setPort(8000);
        http.setIdleTimeout(30000);
        
        // Set the connector
        server.addConnector(http);
        //ServletHandler handlers=new ServletHandler();
        
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setContextPath("/");
        server.setHandler(handler);
        
        ServletHolder infor = new ServletHolder(new ProfileServlet());
        handler.addServlet(infor, "/");
        
        ServletHolder signup = new ServletHolder(new RegisterFormServlet());
        handler.addServlet(signup, "/signup");
        
        ServletHolder login = new ServletHolder(new LoginServlet());
        handler.addServlet(login, "/login");

        /*
        handlers.addServletWithMapping(FormServlet.class, "/");
        handlers.addServletWithMapping(RegisterFormServlet.class, "/register");
        handlers.addServletWithMapping(LoginServlet.class, "/login");
        server.setHandler(handlers);
        */
        //start server
        server.start();
        server.join();
    }
    
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jettyformsimple;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;



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
        ServletHandler handlers=new ServletHandler();
        
        handlers.addServletWithMapping(FormServlet.class, "/");
        handlers.addServletWithMapping(RegisterFormServlet.class, "/register");
        server.setHandler(handlers);
        
        //start server
        server.start();
        server.join();
    }
    
    
    
}

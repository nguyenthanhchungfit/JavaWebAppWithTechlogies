/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chungnt.formjetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;

/**
 *
 * @author cpu11165-local
 */
public class JettyServer {
    
    public static void main(String[] args) throws Exception{
        Server server = new Server();
    
        // HTTP Connector
        ServerConnector http = new ServerConnector(server);
        http.setHost("localhost");
        http.setPort(8000);
        http.setIdleTimeout(30000);
        
        // Set the connector
        server.addConnector(http);
        
        // Add servlet
//        ServletContextHandler context = new ServletContextHandler
//                        (ServletContextHandler.SESSIONS);
//        context.setContextPath("/");
//        server.setHandler(context);
//        
//        context.addServlet(FormServlet.class, "/");
//        context.addServlet(RegisterInformServlet.class, "/register");
        
        ServletHandler handler=new ServletHandler();
        handler.addServletWithMapping(FormServlet.class, "/");
        handler.addServletWithMapping(RegisterInformServlet.class, "/register");
        server.setHandler(handler);
        
        //start server
        server.start();
        server.join();
        
        
    }
    
}

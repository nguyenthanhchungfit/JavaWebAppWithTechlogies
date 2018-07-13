/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package realtimetest;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;


/**
 *
 * @author cpu11165-local
 */
public class RealTimeTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        Server server = new Server();
        ServletHandler handler = new ServletHandler();
        
        handler.addServletWithMapping(IndexHandler.class, "/");
        
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{handler});
        server.setHandler(handlers);
        
        server.start();
        server.join();
    }
    
}




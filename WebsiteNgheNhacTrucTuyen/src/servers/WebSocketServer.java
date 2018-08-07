/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servers;

import admin_server.LogsServlet;
import com.vng.zing.jettyserver.WebServers;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;

import org.eclipse.jetty.servlet.ServletHandler;

/**
 *
 * @author cpu11165-local
 */
public class WebSocketServer {

    public boolean setupAndStart() {
        Server servers = new Server(8003);
//         ServletHandler handler = new ServletHandler();
//        
//        handler.addServletWithMapping(LogsServlet.class, "/");
//        
//        HandlerList handlers = new HandlerList();
//        handlers.setHandlers(new Handler[]{handler});
//        servers.setHandler(handlers);
        
        ServletContextHandler handler = new ServletContextHandler();
        handler.addServlet(LogsServlet.class, "/");
        servers.setHandler(handler);
        
        try {
            servers.start();
        } catch (Exception ex) {
            Logger.getLogger(WebSocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            servers.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(WebSocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
}

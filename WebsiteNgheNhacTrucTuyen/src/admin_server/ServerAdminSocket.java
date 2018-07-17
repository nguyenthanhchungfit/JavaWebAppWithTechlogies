/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin_server;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletHandler;
/**
 *
 * @author Nguyen Thanh Chung
 */
public class ServerAdminSocket {
    public static void main(String[] args) throws Exception {
        System.out.println("Admin Server started");
        
        Server server = new Server(8003);
        ServletHandler handler = new ServletHandler();
        
        handler.addServletWithMapping(LogsServlet.class, "/");
        
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{handler});
        server.setHandler(handlers);
        
        server.start();
        server.join();
    }
}

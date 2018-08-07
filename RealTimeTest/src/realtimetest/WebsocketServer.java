/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package realtimetest;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
/**
 *
 * @author cpu11165-local
 */
public class WebsocketServer {
    public boolean setupAndRun() throws Exception{
        Server servers = new Server(3000);
        
        ServletHandler handler = new ServletHandler();
        handler.addServletWithMapping(ToUpperWebSocketServlet.class, "/");
        //handler.addServletWithMapping(HelloHandler.class, "/");
        //servers.setup(handler);
        servers.setHandler(handler);
        //boolean success = servers.start();
        
        //System.out.println(servers.getInfo());
        
        servers.start();
        servers.join();
        
        return true;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web_server;

import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URL;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;



/**
 *
 * @author cpu11165-local
 */
public class ServerMP3 {
    public static void main(String[] args) throws Exception{
        
        Server server = new Server();
        ServerConnector http = new ServerConnector(server);
        http.setHost("localhost");
        http.setPort(8000);
        http.setIdleTimeout(30000);
        
        // Set the connector
        server.addConnector(http);
        
        
        HandlerList handlers = new HandlerList();
        
        
        ServletContextHandler contextResource = new ServletContextHandler();
        contextResource.setContextPath("/static");
        
        ServletContextHandler contextContent = new ServletContextHandler();
        contextContent.setContextPath("/");
        

        URL url = ServerMP3.class.getResource("/public");
        if(url == null)
            throw new FileNotFoundException("Unable to find required /resources");

        URI baseURI = url.toURI().resolve("./"); // resolve to directory itself.
        System.out.println("Base Resource URI is " + baseURI);

        contextResource.setBaseResource(Resource.newResource(baseURI));

        // Add something to serve the static files
        // It's named "default" to conform to servlet spec
        
        ServletHolder myHome = new ServletHolder(new HomeServlet());
        contextContent.addServlet(myHome, "/");
        
        ServletHolder searchServlet = new ServletHolder(new SearchServlet());
        contextContent.addServlet(searchServlet, "/search");
        
        ServletHolder songServlet = new ServletHolder(new SongServlet());
        contextContent.addServlet(songServlet, "/song");
        
        ServletHolder singerServlet = new ServletHolder(new SingerServlet());
        contextContent.addServlet(singerServlet, "/singer");
        
        ServletHolder lyricsServlet = new ServletHolder(new LyricsServlet());
        contextContent.addServlet(lyricsServlet, "/lyric");
        
        ServletHolder loginServlet = new ServletHolder(new LoginServlet());
        contextContent.addServlet(loginServlet, "/login");
        
        ServletHolder signupServlet = new ServletHolder(new SignupServlet());
        contextContent.addServlet(signupServlet, "/signup");
        
        ServletHolder adminServlet = new ServletHolder(new AdminServlet());
        contextContent.addServlet(adminServlet, "/admin");
        
        ServletHolder staticHolder = new ServletHolder(new DefaultServlet());
        contextResource.addServlet(staticHolder, "/");

        handlers.addHandler(contextResource);
        handlers.addHandler(contextContent);
        

        server.setHandler(handlers);
        server.start();

        System.out.println("Your server is started on " + server.getURI());
        server.join();
    }
}

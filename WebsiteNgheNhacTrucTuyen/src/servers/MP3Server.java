/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servers;

import com.vng.zing.jettyserver.WebServers;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import web_server.AdminServlet;
import web_server.HomeServlet;
import web_server.LoginServlet;
import web_server.LogoutServlet;
import web_server.LyricsServlet;
import web_server.SearchServlet;
import web_server.ServerMP3;
import web_server.SignupServlet;
import web_server.SingerServlet;
import web_server.SongServlet;
import web_server.StatisticServlet;

/**
 *
 * @author cpu11165-local
 */
public class MP3Server {
    public boolean setupAndStart() throws FileNotFoundException, URISyntaxException{
        WebServers servers = new WebServers("appmp3");
        
        
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

        try {
            contextResource.setBaseResource(Resource.newResource(baseURI));
        } catch (MalformedURLException ex) {
            Logger.getLogger(MP3Server.class.getName()).log(Level.SEVERE, null, ex);
        }

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
        
        ServletHolder logoutServlet = new ServletHolder(new LogoutServlet());
        contextContent.addServlet(logoutServlet, "/logout");
        
        ServletHolder signupServlet = new ServletHolder(new SignupServlet());
        contextContent.addServlet(signupServlet, "/signup");
        
        ServletHolder adminServlet = new ServletHolder(new AdminServlet());
        contextContent.addServlet(adminServlet, "/admin");
        
        ServletHolder statisticServlet = new ServletHolder(new StatisticServlet());
        contextContent.addServlet(statisticServlet, "/statistic");
        
        ServletHolder staticHolder = new ServletHolder(new DefaultServlet());
        contextResource.addServlet(staticHolder, "/");

        handlers.addHandler(contextResource);
        handlers.addHandler(contextContent);
        
        servers.setup(handlers);
        
        return servers.start();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staticfiles;


import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
/**
 *
 * @author Nguyen Thanh Chung
 */
public class StaticFiles {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, URISyntaxException, MalformedURLException, Exception {
        Server server = new Server(8080);

        HandlerList handlers = new HandlerList();

        ServletContextHandler contextResource = new ServletContextHandler();
        contextResource.setContextPath("/public");
        
        ServletContextHandler contextContent = new ServletContextHandler();
        contextContent.setContextPath("/");
        

        URL url = StaticFiles.class.getResource("/css");
        if(url == null)
            throw new FileNotFoundException("Unable to find required /resources");

        URI baseURI = url.toURI().resolve("./"); // resolve to directory itself.
        System.out.println("Base Resource URI is " + baseURI);

        contextResource.setBaseResource(Resource.newResource(baseURI));

        // Add something to serve the static files
        // It's named "default" to conform to servlet spec
        
        ServletHolder myHome = new ServletHolder(new HomeServlet());
        contextContent.addServlet(myHome, "/");
        
        ServletHolder staticHolder = new ServletHolder(new DefaultServlet());
        contextResource.addServlet(staticHolder, "/");

        handlers.addHandler(contextResource);
        handlers.addHandler(contextContent);
        //handlers.addHandler(new DefaultHandler()); // always last handler

        server.setHandler(handlers);
        server.start();

        System.out.println("Your server is started on " + server.getURI());
        server.join();
    }
    
}

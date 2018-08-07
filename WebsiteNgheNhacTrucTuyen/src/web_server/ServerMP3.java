/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web_server;

import servers.MP3Server;



/**
 *
 * @author cpu11165-local
 */
public class ServerMP3 {
        
    public static void main(String[] args) throws Exception{
        
//        Server server = new Server();
//        ServerConnector http = new ServerConnector(server);
//        http.setHost("localhost");
//        http.setPort(8000);
//        http.setIdleTimeout(30000);
        
        // Set the connector
//        server.addConnector(http);

        MP3Server mp3Server = new MP3Server();
        if(!mp3Server.setupAndStart()){
            System.err.println("Could not start http servers! Exit now.");
            System.exit(1);
        }else{
            System.out.println("@@@ MP3 Server started!!!");
        }
    }
}

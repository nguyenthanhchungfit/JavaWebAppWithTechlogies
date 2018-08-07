/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servers;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;

/**
 *
 * @author cpu11165-local
 */
public class Servers {
    public static void main(String[] args) throws FileNotFoundException, URISyntaxException {
        
        // 1. User Server
        UserServer userServer = new UserServer();
        if(!userServer.setupAndStart()){
            System.err.println("Could not start thrift servers! Exit now.");
            System.exit(1);
        }else{
            System.out.println("@@@ User Server started!!!");
        }
        
        // 2. Data Server
        DataServer dataServer = new DataServer();
        if (!dataServer.setupAndStart()) {
            System.err.println("Could not start thrift servers! Exit now.");
            System.exit(1);
        } else {
            System.out.println("@@@ Data Server started!!!");
        }
        
        // 3. Socket Server
        
        
        // 4. MP3 Server
        MP3Server mp3Server = new MP3Server();
        if(!mp3Server.setupAndStart()){
            System.err.println("Could not start http servers! Exit now.");
            System.exit(1);
        }else{
            System.out.println("@@@ MP3 Server started!!!");
        }
    }
}

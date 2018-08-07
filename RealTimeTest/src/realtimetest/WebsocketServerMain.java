/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package realtimetest;


/**
 *
 * @author cpu11165-local
 */
public class WebsocketServerMain {
    public static void main(String[] args) throws Exception {
        WebsocketServer wServer = new WebsocketServer();
        if(!wServer.setupAndRun()){
            System.err.println("Can't start Websocket Server");
            System.exit(1);
        }else{
            System.out.println("@@@@@ WebsocketServer Started");
        }
        
        System.out.println("Working Directory = " +
              System.getProperty("user.dir"));
        
    }
}

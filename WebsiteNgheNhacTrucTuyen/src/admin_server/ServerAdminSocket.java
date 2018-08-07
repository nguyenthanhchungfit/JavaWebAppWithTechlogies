/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin_server;

import servers.WebSocketServer;
/**
 *
 * @author Nguyen Thanh Chung
 */
public class ServerAdminSocket {
    public static void main(String[] args) throws Exception {
        WebSocketServer webSocketServer = new WebSocketServer();
        
        if(!webSocketServer.setupAndStart()){
            System.err.println("Could not start http servers! Exit now.");
            System.exit(1);
        }else{
            System.out.println("@@ Server Admin Socket Started");
        }
    }
}

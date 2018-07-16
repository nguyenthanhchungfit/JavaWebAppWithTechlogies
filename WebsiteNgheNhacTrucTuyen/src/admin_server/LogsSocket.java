/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin_server;

import java.io.IOException;
import java.util.ArrayList;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
/**
 *
 * @author Nguyen Thanh Chung
 */
@WebSocket
public class LogsSocket {
    public static Session sessionConnect = null;
    private static ArrayList<String> listSession = new ArrayList<>();

    static{
        listSession.add("done");
    }
    
    private static LogsSocket instance = new LogsSocket();
    
    public static LogsSocket getInstance(){
        return instance;
    }
    
    public LogsSocket(){
        System.out.println("New Object");
        System.out.println(listSession.toString());
    }
    
    @OnWebSocketMessage
    public void onText(Session session, String message) throws IOException {
        System.out.println("Message received:" + message);
        if (session.isOpen()) {
            String response = message.toUpperCase();
            listSession.add(message);
            System.out.println("current size: " + listSession.size());
            session.getRemote().sendString(response);
        }
    }

    @OnWebSocketConnect
    public void onConnect(Session session) throws IOException {
        sessionConnect = session;
        System.out.println(listSession.size());
        System.out.println(sessionConnect.toString());
        System.out.println(session.getRemoteAddress().getHostString() + " connected!");
    }

    @OnWebSocketClose
    public void onClose(Session session, int status, String reason) {
        System.out.println(session.getRemoteAddress().getHostString() + " closed!");
    }
    
    public void sendMyMessage(String message) throws IOException {   
        if (sessionConnect != null && sessionConnect.isOpen()) {
            System.out.println("Vo day");
            sessionConnect.getRemote().sendString(message);
        }
    }
    
    public static void addnewSession(){
       listSession.add("Chung");
    }
    
}

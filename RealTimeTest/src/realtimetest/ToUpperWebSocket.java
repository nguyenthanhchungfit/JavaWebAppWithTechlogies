/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package realtimetest;

import java.io.IOException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

/**
 *
 * @author cpu11165-local
 */
@WebSocket
public class ToUpperWebSocket {

    public Session sessionConnect = null;

    @OnWebSocketMessage
    public void onText(Session session, String message) throws IOException {
        System.out.println("Message received:" + message);
        if (session.isOpen()) {
            String response = message.toUpperCase();
            session.getRemote().sendString(response);
        }
    }

    @OnWebSocketConnect
    public void onConnect(Session session) throws IOException {
        sessionConnect = session;
        System.out.println(session.getRemoteAddress().getHostString() + " connected!");
    }

    @OnWebSocketClose
    public void onClose(Session session, int status, String reason) {
        System.out.println(session.getRemoteAddress().getHostString() + " closed!");
    }

    public void sendMyMessage(Session session) throws IOException {
        System.out.println("Test message sent from server");
        if (session.isOpen()) {
            session.getRemote().sendString("Hello Babe!!!");
        }
    }

}

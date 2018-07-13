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
import java.net.URI;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

public class WebSocketClientMain {

    public static void main(String[] args) {
        String dest = "ws://localhost:3000/toUpper";
        WebSocketClient client = new WebSocketClient();
        try {

            ToUpperClientSocket socket = new ToUpperClientSocket();
            client.start();
            URI echoUri = new URI(dest);
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            client.connect(socket, echoUri, request);
            socket.getLatch().await();
            socket.sendMessage("echo");
            socket.sendMessage("test");
            Thread.sleep(10000l);

        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                client.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

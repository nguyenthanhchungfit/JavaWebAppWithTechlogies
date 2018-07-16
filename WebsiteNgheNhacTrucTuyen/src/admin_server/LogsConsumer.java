/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin_server;

import java.io.IOException;
import java.util.ArrayList;
import org.eclipse.jetty.websocket.api.Session;

/**
 *
 * @author Nguyen Thanh Chung
 */
public class LogsConsumer {
    public static void main(String[] args) throws InterruptedException, IOException {
        System.out.println("done");
        LogsSocket.addnewSession();
    }
}

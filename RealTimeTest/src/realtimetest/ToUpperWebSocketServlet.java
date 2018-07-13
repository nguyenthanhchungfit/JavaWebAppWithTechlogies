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

import javax.servlet.annotation.WebServlet;


import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

@WebServlet(urlPatterns="/toUpper")
public class ToUpperWebSocketServlet  extends WebSocketServlet{

	@Override
	public void configure(WebSocketServletFactory factory) {
		
	      factory.register(ToUpperWebSocket.class);
		
	}

}
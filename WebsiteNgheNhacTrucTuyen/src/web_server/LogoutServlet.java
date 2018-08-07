/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web_server;

import contracts.MP3ServerContract;
import contracts.UserServerContract;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import thrift_services.UserServices;

/**
 *
 * @author cpu11165-local
 */
public class LogoutServlet extends HttpServlet {

    private static final int PORT_USER_SERVER = UserServerContract.PORT;
    private static final String HOST_USER_SERVER = UserServerContract.HOST_SERVER;

    private static final String SERVER_NAME = MP3ServerContract.SERVRE_NAME;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("******** Logout Servlet");
        Cookie[] cookies = req.getCookies();
        boolean flag = false;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String c_user_key = cookie.getName();
                if ("c_user".equals(c_user_key)) {
                    flag = true;
                    String c_user = cookie.getValue();
                    System.out.println("c_user : " + c_user);
                    this.logout(c_user);
                    
                    cookie.setMaxAge(0);
                    resp.addCookie(cookie);
                    break;
                }
            }
        }
        resp.sendRedirect("http://localhost:8000");
    }

    private void logout(String c_user) {
        try {
            
            TSocket socket = new TSocket(HOST_USER_SERVER, PORT_USER_SERVER);
            TTransport transport = new TFramedTransport(socket);
            transport.open();
            
            TBinaryProtocol protocol = new TBinaryProtocol(transport);
            TMultiplexedProtocol mpUserServices = new TMultiplexedProtocol(protocol, "UserServices");
            UserServices.Client userServices = new UserServices.Client(mpUserServices);
            userServices.logout(c_user);
            
            transport.close();
            
        } catch (TException ex) {
            ex.printStackTrace();
        }
    }
}

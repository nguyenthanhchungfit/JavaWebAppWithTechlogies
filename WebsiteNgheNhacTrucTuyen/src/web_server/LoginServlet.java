/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web_server;

import Helpers.FormatPureString;
import contracts.MP3ServerContract;
import contracts.UserServerContract;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import thrift_services.UserServices;

/**
 *
 * @author cpu11165-local
 */
public class LoginServlet extends HttpServlet {

    private static final int PORT_USER_SERVER = UserServerContract.PORT;
    private static final String HOST_USER_SERVER = UserServerContract.HOST_SERVER;
    
    private static final String SERVER_NAME = MP3ServerContract.SERVRE_NAME;
    
    private static Logger logger = LogManager.getLogger(LoginServlet.class.getName());
    private static Stopwatch stopwatch = SimonManager.getStopwatch(MP3ServerContract.STOP_WATCH_LOGIN_SERVLET);
    
    private static String messageForLog = "Login ";
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Split split = stopwatch.start();
        String messageLog = "";
        
        System.out.println("Login - POST");

        PrintWriter out = resp.getWriter();
        resp.setContentType("text/plain");
        resp.setStatus(HttpServletResponse.SC_OK);
        String decodedData = this.getDataAjax(req);

        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println(cookie.getName() + " : " + cookie.getValue());
            }
        }

        if (!decodedData.isEmpty()) {
            
            Map<String, String> dataParse = this.parseFromQueryString(decodedData);
            String username = dataParse.get("username");
            String password = dataParse.get("password");
            
            System.out.println(username);
            System.out.println(password);  
            
            String c_user = this.loginAccount(username, password);
            
            
            System.out.println("C_USER" + c_user);
            if(!c_user.isEmpty()){
                Cookie cookie = new Cookie("c_user", c_user);
                cookie.setMaxAge(Session.MAX_AGE);
                resp.addCookie(cookie);
            }
            out.println(c_user);
            
            split.stop();
            messageLog = FormatPureString.formatStringMessageLogs(SERVER_NAME, split.runningFor(), 
                    messageForLog + "username=" +username + " password=" + password + " user_id=" + c_user);
            logger.info(messageLog);
            
        } else {
            split.stop();
            messageLog = FormatPureString.formatStringMessageLogs(SERVER_NAME, split.runningFor(), 
                    messageForLog + "error: Not data received!");
            logger.warn(messageLog);
            out.println("Not data received!");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("text/html");
        resp.setStatus(HttpServletResponse.SC_OK);

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        System.out.println(username);
        System.out.println(password);

        out.println("Done!");
    }

    private String getDataAjax(HttpServletRequest req) throws IOException {
        InputStream is = req.getInputStream();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buf = new byte[32];
        int r = 0;
        while (r >= 0) {
            r = is.read(buf);
            if (r >= 0) {
                os.write(buf, 0, r);
            }
        }
        String s = new String(os.toByteArray(), "UTF-8");
        String decoded = URLDecoder.decode(s, "UTF-8");
        return decoded;
    }

    private Map<String, String> parseFromQueryString(String query) {
        Map<String, String> data = new HashMap<>();
        String[] arr = query.split("&");
        for (String ele : arr) {
            String[] eles = ele.split("=");
            data.put(eles[0], eles[1]);
        }
        return data;
    }

    // Connect to User Server
    private String loginAccount(String username, String password) {
        String c_user = "";
        try {
            
            TSocket socket = new TSocket(HOST_USER_SERVER, PORT_USER_SERVER);
            TTransport transport = new TFramedTransport(socket);
            transport.open();
            
            TBinaryProtocol protocol = new TBinaryProtocol(transport);
            TMultiplexedProtocol mpUserServices = new TMultiplexedProtocol(protocol, "UserServices");
            UserServices.Client userServices = new UserServices.Client(mpUserServices);
            c_user = userServices.login(username, password);
            
            transport.close();
            
        } catch (TException ex) {
            ex.printStackTrace();
        }
        return c_user;
    }
    

}

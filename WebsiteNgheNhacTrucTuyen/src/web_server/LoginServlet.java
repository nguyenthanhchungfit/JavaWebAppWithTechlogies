/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web_server;

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
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.transport.TSocket;
import thrift_services.UserServices;

/**
 *
 * @author cpu11165-local
 */
public class LoginServlet extends HttpServlet {

    private final int port = 8002;
    private final String host = "localhost";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("Login - POST");

        PrintWriter out = resp.getWriter();
        resp.setContentType("text/html");
        resp.setStatus(HttpServletResponse.SC_OK);
        String decodedData = this.getDataAjax(req);

        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println(cookie.getName() + " : " + cookie.getValue());
            }
        }

        if (decodedData != "") {
            Map<String, String> dataParse = this.parseFromQueryString(decodedData);
            String username = dataParse.get("username");
            String password = dataParse.get("password");

            System.out.println(username);
            System.out.println(password);
            out.println("Done!");
        } else {
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

    private String loginAccount(String username, int password) {
        String c_user = "";
        try {
            TSocket transport = new TSocket(host, port);
            transport.open();
            TBinaryProtocol protocol = new TBinaryProtocol(transport);
            TMultiplexedProtocol mpUserServices = new TMultiplexedProtocol(protocol, "UserServices");
            UserServices.Client userServices = new UserServices.Client(mpUserServices);

            transport.close();
        } catch (TException ex) {
            ex.printStackTrace();
        }
        return c_user;
    }

}

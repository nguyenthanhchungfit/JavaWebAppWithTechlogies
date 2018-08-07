/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web_server;

import contracts.UserServerContract;
import hapax.Template;
import hapax.TemplateDictionary;
import hapax.TemplateLoader;
import hapax.TemplateResourceLoader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Session;
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
public class HomeServlet extends HttpServlet {

    private static final String HOST_USER_SERVER = UserServerContract.HOST_SERVER;
    private static final int PORT_USER_SERVER = UserServerContract.PORT;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        boolean isAdmin = this.updateCookie(req, resp);

        TemplateLoader templateLoader = TemplateResourceLoader.create("public/hapax/");
        try {
            Template template = templateLoader.getTemplate("home.xtm");
            TemplateDictionary templateDictionary = new TemplateDictionary();

            Template headerTemplate = templateLoader.getTemplate("partial_header.xtm");
            TemplateDictionary templateDictionaryHeader = new TemplateDictionary();

            // header template render
            templateDictionaryHeader.setVariable("href_home", "/");
            templateDictionaryHeader.setVariable("resource_zamp3_ic", "./static/public/images/zamp3.png");
            if (isAdmin) {
                templateDictionaryHeader.setVariable("style_display_btnAcc", "display:block;");
                templateDictionaryHeader.setVariable("style_display_btnLogin", "display:none;");
            } else {
                templateDictionaryHeader.setVariable("style_display_btnAcc", "display:none;");
                templateDictionaryHeader.setVariable("style_display_btnLogin", "display:block;");
            }
            templateDictionary.setVariable("header", headerTemplate.renderToString(templateDictionaryHeader));

            templateDictionary.setVariable("footer", "partial_footer.xtm");
            out.println(template.renderToString(templateDictionary));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean updateCookie(HttpServletRequest req, HttpServletResponse resp) {
        Cookie[] cookies = req.getCookies();
        boolean flag_user = false;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String c_user_key = cookie.getName();
                if ("c_user".equals(c_user_key)) {
                    String c_user = cookie.getValue();
                    flag_user = this.isAdminSession(c_user);
                    if (flag_user) {
                        cookie.setMaxAge(Session.MAX_AGE);
                        resp.addCookie(cookie);
                    }
                    break;
                }
            }
        }
        return flag_user;
    }

    // Check Admin
    private boolean isAdminSession(String c_user) {
        boolean isAdmin = false;
        try {

            TSocket socket = new TSocket(HOST_USER_SERVER, PORT_USER_SERVER);
            TTransport transport = new TFramedTransport(socket);
            transport.open();

            TBinaryProtocol protocol = new TBinaryProtocol(transport);
            TMultiplexedProtocol mpUserServices = new TMultiplexedProtocol(protocol, "UserServices");
            UserServices.Client userServices = new UserServices.Client(mpUserServices);
            isAdmin = userServices.isAdminSession(c_user);

            transport.close();
        } catch (TException ex) {
            ex.printStackTrace();
        }
        return isAdmin;
    }

}

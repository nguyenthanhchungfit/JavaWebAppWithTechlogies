/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web_server;

import Helpers.FormatPureString;
import cache_data.DataCacher;
import contracts.DataServerContract;
import contracts.MP3ServerContract;
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
import models.Singer;
import models.SingerResult;
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
import thrift_services.SingerServices;
import thrift_services.UserServices;


/**
 *
 * @author Nguyen Thanh Chung
 */
public class SingerServlet extends HttpServlet {
    
    
    private static final String HOST_DATA_SERVER = DataServerContract.HOST_SERVER;
    private static final int PORT_DATA_SERVER = DataServerContract.PORT;
    
    private static final String HOST_USER_SERVER = UserServerContract.HOST_SERVER;
    private static final int PORT_USER_SERVER = UserServerContract.PORT;
    
    
    private static final String SERVER_NAME = MP3ServerContract.SERVRE_NAME;
    
    private static final Logger logger = LogManager.getLogger(SingerServlet.class.getName());
    private static Stopwatch stopwatch = SimonManager.getStopwatch(MP3ServerContract.STOP_WATCH_SINGER_SERVLET);
    
    private static String messsageForLog = "GET SINGER ";
    
    private static final DataCacher dataCacher = DataCacher.getInstance();
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        Split split = stopwatch.start();
        
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
          
        String idSinger = req.getParameter("id");
        Singer singer = this.getSingerById(idSinger);
        boolean isAdmin = this.updateCookie(req, resp);
        
        String messageLog = "";
        
        TemplateLoader templateLoader = TemplateResourceLoader.create("public/hapax/");
        try{
            Template template = templateLoader.getTemplate("singer.xtm"); 
            //Template footerTemplate = templateLoader.getTemplate("partial_footer.xtm");
            Template headerTemplate = templateLoader.getTemplate("partial_header.xtm");
            
            TemplateDictionary templateDictionary = new TemplateDictionary();
            //TemplateDictionary templateDictionaryFooter = new TemplateDictionary();
            TemplateDictionary templateDictionaryHeader = new TemplateDictionary();
            if(singer == null){
                // LOGGER
                split.stop();
                messageLog = FormatPureString.formatStringMessageLogs(SERVER_NAME, split.runningFor(), messsageForLog + " id=" + idSinger + " :" + "null");
                logger.info(messageLog);
                
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.println("404 NOT FOUND!");
            }else{
                
                String linkCoverImage = "../static/public/images/singers/cover/" + singer.imgCover;
                String linkAvatarImage = "../static/public/images/singers/avatar/" + singer.imgAvatar;
                templateDictionary.setVariable("img_cover", linkCoverImage);
                templateDictionary.setVariable("img_avatar", linkAvatarImage);
                templateDictionary.setVariable("name", singer.name);
                templateDictionary.setVariable("realname", singer.realname);
                templateDictionary.setVariable("dob", singer.dob);
                templateDictionary.setVariable("country", singer.country);
                templateDictionary.setVariable("description", singer.description);
                
                
                // header template render
                templateDictionaryHeader.setVariable("href_home", "./");
                templateDictionaryHeader.setVariable("resource_zamp3_ic", "../static/public/images/zamp3.png");
                if(isAdmin){
                    templateDictionaryHeader.setVariable("style_display_btnAcc", "display:block;");
                    templateDictionaryHeader.setVariable("style_display_btnLogin", "display:none;");
                }else{
                    templateDictionaryHeader.setVariable("style_display_btnAcc", "display:none;");
                    templateDictionaryHeader.setVariable("style_display_btnLogin", "display:block;");
                }
                templateDictionary.setVariable("header", headerTemplate.renderToString(templateDictionaryHeader));
                
                
                // footer template render
                // k sử dụng dạng include > trong partial_view
                //templateDictionary.setVariable("footer", footerTemplate.renderToString(templateDictionaryFooter));
                templateDictionary.setVariable("footer", "partial_footer.xtm");
                
                // LOGGER
                split.stop();
                messageLog = FormatPureString.formatStringMessageLogs(SERVER_NAME, split.runningFor(), 
                        messsageForLog + " id=" + idSinger + " :" + "name=" + singer.name);
                logger.info(messageLog);
                out.println(template.renderToString(templateDictionary));  
            }
            return;
        }catch(Exception e){
            // LOGGER
            
            split.stop();
            messageLog = FormatPureString.formatStringMessageLogs(SERVER_NAME, split.runningFor(), 
                        messsageForLog + " id=" + idSinger + " : error=" +  e.getMessage());
            logger.error(messageLog);
            
            e.printStackTrace();
        }
    }

    
    private Singer getSingerFromDataServerById(String id){
        System.out.println("GET SINGER : ID=" + id);
        Singer singer = null;
        try{
            TSocket socket = new TSocket(HOST_DATA_SERVER, PORT_DATA_SERVER);
            TTransport transport = new TFramedTransport(socket);
            transport.open();
            
            TBinaryProtocol protocol = new TBinaryProtocol(transport);
            TMultiplexedProtocol mpSingerServices = new TMultiplexedProtocol(protocol, "SingerServices");
            SingerServices.Client singerServices = new SingerServices.Client(mpSingerServices);                    
            SingerResult sr = singerServices.getSingerById(id);
            if(sr.result == 0){
                singer = sr.singer;
            }
            transport.close(); 
        }catch(TException ex){
            ex.printStackTrace();
        }  
        return singer;
    }
    
    private Singer getSingerById(String id){
        Singer singer = null;
        String keySingerCache = "singer:" + id;
        if(dataCacher.isExisted(keySingerCache)){
            singer = dataCacher.getCacheSinger(keySingerCache);
        }else{
            singer = getSingerFromDataServerById(id);
        }
        return singer;
    }
    
    private boolean updateCookie(HttpServletRequest req, HttpServletResponse resp){
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

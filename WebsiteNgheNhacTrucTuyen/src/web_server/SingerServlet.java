/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web_server;

import Helpers.FormatPureString;
import contracts.DataServerContract;
import contracts.MP3ServerContract;
import hapax.Template;
import hapax.TemplateDictionary;
import hapax.TemplateLoader;
import hapax.TemplateResourceLoader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Singer;
import models.SingerResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.transport.TSocket;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import thrift_services.SingerServices;


/**
 *
 * @author Nguyen Thanh Chung
 */
public class SingerServlet extends HttpServlet {
    
    
    private static final String HOST = DataServerContract.HOST_SERVER;
    private static final int PORT = DataServerContract.PORT;
    private static final String SERVER_NAME = MP3ServerContract.SERVRE_NAME;
    
    private static final Logger logger = LogManager.getLogger(SingerServlet.class.getName());
    private static Stopwatch stopwatch = SimonManager.getStopwatch(MP3ServerContract.STOP_WATCH_SINGER_SERVLET);
    
    private static String messsageForLog = "GET SINGER ";
    
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
        
        String messageLog = "";
        
        TemplateLoader templateLoader = TemplateResourceLoader.create("public/hapax/");
        try{
            Template template = templateLoader.getTemplate("singer.xtm"); 
            Template footerTemplate = templateLoader.getTemplate("partial_footer.xtm");
            
            TemplateDictionary templateDictionary = new TemplateDictionary();
            TemplateDictionary templateDictionaryFooter = new TemplateDictionary();
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
        
        
    
    private Singer getSingerById(String id){
        System.out.println("GET SINGER : ID=" + id);
        Singer singer = null;
        try{
            TSocket transport  = new TSocket(HOST, PORT);
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
    
}

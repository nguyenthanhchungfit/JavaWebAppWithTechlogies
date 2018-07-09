/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web_server;

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
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.transport.TSocket;
import thrift_services.SingerServices;


/**
 *
 * @author Nguyen Thanh Chung
 */
public class SingerServlet extends HttpServlet {

    private final int port = 8001;
    private final String host = "localhost";
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        
        String idSinger = req.getParameter("id");
        Singer singer = this.getSingerById(idSinger);
        TemplateLoader templateLoader = TemplateResourceLoader.create("public/hapax/");
        try{
            Template template = templateLoader.getTemplate("singer.xtm");              
            TemplateDictionary templateDictionary = new TemplateDictionary();
            if(singer == null){
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.println("404 NOT FOUND!");
            }else{
                templateDictionary.setVariable("name", singer.name);
                templateDictionary.setVariable("realname", singer.realname);
                templateDictionary.setVariable("dob", singer.dob);
                templateDictionary.setVariable("country", singer.country);
                templateDictionary.setVariable("description", singer.description);
                out.println(template.renderToString(templateDictionary));  
            }
            return;
        }catch(Exception e){
            e.printStackTrace();
        }
    }
        
        
    
    private Singer getSingerById(String id){
        System.out.println("GET SINGER : ID=" + id);
        Singer singer = null;
        try{
            TSocket transport  = new TSocket(host, port);
            transport.open();
            
            TBinaryProtocol protocol = new TBinaryProtocol(transport);
            TMultiplexedProtocol mpSingerServices = new TMultiplexedProtocol(protocol, "SingerServices");
            SingerServices.Client singerServices = new SingerServices.Client(mpSingerServices);
                    
            SingerResult sr = singerServices.getSingerById(id);

            if(sr.result == 0){
                singer = sr.singer;
            }
            System.out.println(sr);
            transport.close(); 
        }catch(TException ex){
            ex.printStackTrace();
        }  
        return singer;
    } 
    
}

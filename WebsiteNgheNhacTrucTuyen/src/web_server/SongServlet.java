/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web_server;

import hapax.Template;
import hapax.TemplateDataDictionary;
import hapax.TemplateDictionary;
import hapax.TemplateLoader;
import hapax.TemplateResourceLoader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.ServicesDataCenter;
import models.Singer;
import models.Song;
import models.SongResult;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 *
 * @author Nguyen Thanh Chung
 */
public class SongServlet extends HttpServlet {
    private final int port = 8001;
    private final String host = "localhost";
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        
        // Get data from client
        String song_name = req.getParameter("search_text");
        System.out.println(song_name);
        
        TemplateLoader templateLoader = TemplateResourceLoader.create("public/hapax/");
        if(song_name.equals("")){
            try{
                Template template = templateLoader.getTemplate("song.xtm");
                TemplateDictionary templateDictionary = new TemplateDictionary();              
                out.println(template.renderToString(templateDictionary));  
                return;
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        // Gửi request về DataServer
        Song song = this.getSongByName(song_name);
        
        try{
            Template template = templateLoader.getTemplate("song.xtm");
            TemplateDictionary templateDictionary = new TemplateDictionary();
            if(song != null){
                templateDictionary.setVariable("composers", Helper.beautifyString(song.composers));
                templateDictionary.setVariable("album", song.album);
                templateDictionary.setVariable("kinds", Helper.beautifyString(song.kinds));
                for(int i =0; i< song.singers.size(); i++){
                    Singer singer = song.singers.get(i);
                    TemplateDataDictionary temp = templateDictionary.addSection("singers");
                    String link = "/singer/" + singer.id;
                    temp.setVariable("link", link);
                    temp.setVariable("name", singer.name);
                }
                templateDictionary.setVariable("lyrics", song.lyrics.get(0));
            }else{
                templateDictionary.setVariable("error", "Không tìm thấy bài hát!");
                templateDictionary.setVariable("err", "display:none;");
            }
            out.println(template.renderToString(templateDictionary));
            }catch(Exception e){
                e.printStackTrace();
            }  
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        TemplateLoader templateLoader = TemplateResourceLoader.create("public/hapax/");
        try{
            Template template = templateLoader.getTemplate("song.xtm");
            TemplateDictionary templateDictionary = new TemplateDictionary();
            templateDictionary.setVariable("err", "display:none;");
            out.println(template.renderToString(templateDictionary));
        }catch(Exception e){
                e.printStackTrace();
        }
    }
    
    private Song getSongByName(String name){
        Song song = null;
        try{
            TTransport transport = new TSocket(host, port);
            transport.open();
            
            TProtocol protocol = new TBinaryProtocol(transport);               
            ServicesDataCenter.Client client = new ServicesDataCenter.Client(protocol);        
            SongResult sr = client.getSongData(name);
            if(sr.result == 0){
                  song = sr.song;
            }
            transport.close(); 
        }catch(TException ex){
            ex.printStackTrace();
        }  
        return song;
    } 
}

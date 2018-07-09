/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web_server;

import Helpers.FormatPureString;
import crawler_data.CrawlerContracts;
import hapax.Template;
import hapax.TemplateDataDictionary;
import hapax.TemplateDictionary;
import hapax.TemplateLoader;
import hapax.TemplateResourceLoader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Referencer;
import models.Song;
import models.SongResult;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.transport.TSocket;
import thrift_services.SongServices;
import web_server_cache.SongCache;

/**
 *
 * @author Nguyen Thanh Chung
 */
public class SongServlet extends HttpServlet {
    private final int port = 8001;
    private final String host = "localhost";
    private SongCache songCache = new SongCache();
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        TemplateLoader templateLoader = TemplateResourceLoader.create("public/hapax/");
        
        String id_song = req.getParameter("id");
        if(id_song.equals("")){
            try{
                Template template = templateLoader.getTemplate("song.xtm");
                TemplateDictionary templateDictionary = new TemplateDictionary();
                out.println(template.renderToString(templateDictionary));
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            Song song = getSongById(id_song);
            if(song == null){
                try{
                    Template template = templateLoader.getTemplate("song.xtm");
                    TemplateDictionary templateDictionary = new TemplateDictionary();
                    out.println(template.renderToString(templateDictionary));
                }catch(Exception e){
                    e.printStackTrace();
                }
            }else{
                try{
                    Template template = templateLoader.getTemplate("song.xtm");
                    TemplateDictionary templateDictionary = new TemplateDictionary();
                    templateDictionary.setVariable("song_name", song.name);
                    templateDictionary.setVariable("id_song", song.id);
                    
                    
                    ArrayList<Referencer> refsSinger = (ArrayList<Referencer>) song.getSingers();
                    for(Referencer ref : refsSinger){
                        TemplateDataDictionary tempSinger = templateDictionary.addSection("singers");
                        String link_singer = "../singer?id=" + ref.id;
                        String name_singer = ref.name;
                        
                        tempSinger.setVariable("name_singer", name_singer);
                        tempSinger.setVariable("link_singer", link_singer);
                    }
                    
                    String formatComposers = FormatPureString.formatStringFromStrings(song.composers);
                    templateDictionary.setVariable("composers", formatComposers);
                    
                    templateDictionary.setVariable("album", song.album.name);
                    
                    String formatKinds = FormatPureString.formatStringFromRefs(song.kinds);
                    templateDictionary.setVariable("kinds", formatKinds);
                    
                    String link_data_mp3 = "../" + CrawlerContracts.LINK_PATH_SONG_DATA + song.id + ".mp3";
                    templateDictionary.setVariable("link_data_mp3", link_data_mp3);
                    
                    templateDictionary.setVariable("views", (int) song.views);
                    templateDictionary.setVariable("id_lyric", song.lyrics);

                    out.println(template.renderToString(templateDictionary));
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        
        
        
    }
    
    private Song getSongFromDataServerById(String id){
        System.out.println("GET SONG:" + id +", REQUEST TO DATA SERVER");
        Song song = null;
        try{
            TSocket transport  = new TSocket(host, port);
            transport.open();
            
            TBinaryProtocol protocol = new TBinaryProtocol(transport);
            TMultiplexedProtocol mpSongServices = new TMultiplexedProtocol(protocol, "SongServices");
            SongServices.Client songServices = new SongServices.Client(mpSongServices);
                    
            SongResult songResult = songServices.getSongById(id);
            if(songResult.result == 0){
                song = songResult.song;
            }
            transport.close(); 
        }catch(TException ex){
            ex.printStackTrace();
        }  
        return song;
    }
    
    private Song getSongById(String id){
        String keySong = "song:" + id;
        if(songCache.isExisted(keySong)){
            return songCache.getCacheSong(keySong);
        }else{
            Song song = this.getSongFromDataServerById(id);
            if(song != null){
                songCache.insertNewCache(song);
            }
            return song;
        }
    }
}

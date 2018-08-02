/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web_server;

import Helpers.FormatJson;
import Helpers.FormatPureString;
import contracts.DataServerContract;
import contracts.MP3ServerContract;
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
import models.Song;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.transport.TSocket;
import thrift_services.SongServices;
import crawler_data.CrawlerContracts;
import models.Referencer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.json.simple.JSONArray;


/**
 *
 * @author cpu11165-local
 */
public class SearchServlet extends HttpServlet {

    private static final int PORT = DataServerContract.PORT;
    private static final String HOST = DataServerContract.HOST_SERVER;
    
    private static final String SERVER_NAME = MP3ServerContract.SERVRE_NAME;
    
    private static Logger logger = LogManager.getLogger(SearchServlet.class.getName());
    private static Stopwatch stopwatch = SimonManager.getStopwatch(MP3ServerContract.STOP_WATCH_SEARCH_SERVLET);
    
    private String messageForLog = "SEARCH SONG: ";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        Split split = stopwatch.start();
        String messageLog = "";
        
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        // Get data from client
        String song_name = req.getParameter("search_text");
        
        if(song_name == null){
            split.stop();
            messageLog = FormatPureString.formatStringMessageLogs(SERVER_NAME, split.runningFor(),
                        messageForLog + "name=null");
            logger.warn(messageLog);
            out.println("<h2>Empty Name Search</h2>");
            return;
        }
        
        // Nếu name rỗng thì trả lại giao diện chính 
        TemplateLoader templateLoader = TemplateResourceLoader.create("public/hapax/");
        if (song_name.equals("")) {
            try {
                Template template = templateLoader.getTemplate("home.xtm");
                TemplateDictionary templateDictionary = new TemplateDictionary();
                templateDictionary.setVariable("search_name", song_name);
                templateDictionary.setVariable("footer", "partial_footer.xtm");
                out.println(template.renderToString(templateDictionary));
                
                // Logger
                split.stop();
                messageLog = FormatPureString.formatStringMessageLogs(SERVER_NAME, split.runningFor(),
                        messageForLog + "name=" + song_name + " : result=empty name");
                logger.info(messageLog);
                return;
            } catch (Exception e) {
                split.stop();
                messageLog = FormatPureString.formatStringMessageLogs(SERVER_NAME, split.runningFor(),
                        messageForLog + "name=" + song_name + " : result=empty name" + " error=" + e.getMessage());
                logger.error(messageLog);
                e.printStackTrace();
            }
        }

        // Nận dữ liệu từ thrift server 
        ArrayList<Song> songs = getSongsByName(song_name);
        try {
            Template template = templateLoader.getTemplate("search.xtm");
            TemplateDictionary templateDictionary = new TemplateDictionary();
            int size = songs.size();
            templateDictionary.setVariable("result_mount", songs.size());
            templateDictionary.setVariable("search_name", song_name);
            for (int i = 0; i < size; i++) {
                Song song = songs.get(i);
                TemplateDataDictionary temp = templateDictionary.addSection("item");
                String link_song = "../song?id=" + song.id;
                String link_img_song = "../" + CrawlerContracts.LINK_PATH_SONG + song.image;
                temp.setVariable("link_song", link_song);
                temp.setVariable("link_image", link_img_song);
                temp.setVariable("name_song", song.name);

                ArrayList<Referencer> refsSinger = (ArrayList<Referencer>) song.getSingers();
                for (Referencer ref : refsSinger) {
                    TemplateDataDictionary tempSinger = temp.addSection("singers");
                    String link_singer = "../singer?id=" + ref.id;
                    String name_singer = ref.name;

                    tempSinger.setVariable("name_singer", name_singer);
                    tempSinger.setVariable("link_singer", link_singer);
                }

                String kind = FormatPureString.formatStringFromRefs(song.kinds);
                temp.setVariable("kinds", kind);

                String views = ((Long) song.views).toString();
                temp.setVariable("views", views);

            }

            templateDictionary.setVariable("footer", "partial_footer.xtm");
            out.println(template.renderToString(templateDictionary));
            split.stop();
            messageLog = FormatPureString.formatStringMessageLogs(SERVER_NAME, split.runningFor(),
                        messageForLog + "name=" + song_name + " : result=" + songs.size() + " songs");
            logger.info(messageLog);
            return;
        } catch (Exception e) {
            split.stop();
            messageLog = FormatPureString.formatStringMessageLogs(SERVER_NAME, split.runningFor(),
                        messageForLog + "name=" + song_name + " : error:" + e.getMessage());
            logger.error(messageLog);
            e.printStackTrace();
            
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        String name = req.getParameter("name");
        
        if (name != null) {
            ArrayList<Song> songs = getSongsESEByName(name);
            resp.setContentType("application/json;charset=UTF-8");        
            PrintWriter out = resp.getWriter();
            JSONArray jsonResult = FormatJson.convertFromSongESEToJSONArray(songs);
            out.print(jsonResult);
        } else {
            resp.setContentType("text/html;charset=UTF-8");           
            TemplateLoader templateLoader = TemplateResourceLoader.create("public/hapax/");
            try {
                PrintWriter out = resp.getWriter();
                Template template = templateLoader.getTemplate("home.xtm");
                TemplateDictionary templateDictionary = new TemplateDictionary();
                templateDictionary.setVariable("footer", "partial_footer.xtm");
                out.println(template.renderToString(templateDictionary));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private ArrayList<Song> getSongsByName(String name) {
        ArrayList<Song> songs = new ArrayList<>();
        try {
            TSocket transport = new TSocket(HOST, PORT);
            transport.open();

            TBinaryProtocol protocol = new TBinaryProtocol(transport);
            TMultiplexedProtocol mpSongServices = new TMultiplexedProtocol(protocol, "SongServices");
            SongServices.Client songServices = new SongServices.Client(mpSongServices);

            songs = (ArrayList<Song>) songServices.getSongsSearchAPIByName(name);

            transport.close();
        } catch (TException ex) {
            ex.printStackTrace();
        }
        return songs;
    }

    private ArrayList<Song> getSongsESEByName(String name) {
        ArrayList<Song> songs = new ArrayList<>();
        try {
            TSocket transport = new TSocket(HOST, PORT);
            transport.open();

            TBinaryProtocol protocol = new TBinaryProtocol(transport);
            TMultiplexedProtocol mpSongServices = new TMultiplexedProtocol(protocol, "SongServices");
            SongServices.Client songServices = new SongServices.Client(mpSongServices);

            songs = (ArrayList<Song>) songServices.getSongsSearchESEByName(name);

            transport.close();
        } catch (TException ex) {
            ex.printStackTrace();
        }
        return songs;
    }

}

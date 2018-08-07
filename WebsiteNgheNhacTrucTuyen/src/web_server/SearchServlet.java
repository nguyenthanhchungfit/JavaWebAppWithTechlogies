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
import contracts.UserServerContract;
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
import javax.servlet.http.Cookie;
import models.Referencer;
import models.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TTransport;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.json.simple.JSONArray;
import thrift_services.UserServices;

/**
 *
 * @author cpu11165-local
 */
public class SearchServlet extends HttpServlet {

    private static final int PORT_DATA_SERVER = DataServerContract.PORT;
    private static final String HOST_DATA_SERVER = DataServerContract.HOST_SERVER;

    private static final String HOST_USER_SERVER = UserServerContract.HOST_SERVER;
    private static final int PORT_USER_SERVER = UserServerContract.PORT;

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

        if (song_name == null) {
            split.stop();
            messageLog = FormatPureString.formatStringMessageLogs(SERVER_NAME, split.runningFor(),
                    messageForLog + "name=null");
            logger.warn(messageLog);
            out.println("<h2>Empty Name Search</h2>");
            return;
        }

        boolean isAdmin = this.updateCookie(req, resp);

        // Nếu name rỗng thì trả lại giao diện chính 
        TemplateLoader templateLoader = TemplateResourceLoader.create("public/hapax/");
        if (song_name.equals("")) {
            try {
                Template template = templateLoader.getTemplate("home.xtm");
                Template headerTemplate = templateLoader.getTemplate("partial_header.xtm");
                TemplateDictionary templateDictionaryHeader = new TemplateDictionary();

                TemplateDictionary templateDictionary = new TemplateDictionary();
                templateDictionary.setVariable("search_name", song_name);

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

                // footer
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

            Template headerTemplate = templateLoader.getTemplate("partial_header.xtm");
            TemplateDictionary templateDictionaryHeader = new TemplateDictionary();

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

            // header template render
            templateDictionaryHeader.setVariable("href_home", "./");
            templateDictionaryHeader.setVariable("resource_zamp3_ic", "../static/public/images/zamp3.png");
            if (isAdmin) {
                templateDictionaryHeader.setVariable("style_display_btnAcc", "display:block;");
                templateDictionaryHeader.setVariable("style_display_btnLogin", "display:none;");
            } else {
                templateDictionaryHeader.setVariable("style_display_btnAcc", "display:none;");
                templateDictionaryHeader.setVariable("style_display_btnLogin", "display:block;");
            }
            templateDictionary.setVariable("header", headerTemplate.renderToString(templateDictionaryHeader));

            // footer
            templateDictionary.setVariable("footer", "partial_footer.xtm");
            out.println(template.renderToString(templateDictionary));
            
            // Logger
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
            TSocket socket = new TSocket(HOST_DATA_SERVER, PORT_DATA_SERVER);
            TTransport transport = new TFramedTransport(socket);
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
            TSocket socket = new TSocket(HOST_DATA_SERVER, PORT_DATA_SERVER);
            TTransport transport = new TFramedTransport(socket);
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

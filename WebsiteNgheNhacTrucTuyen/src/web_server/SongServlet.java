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
import cache_data.DataCacher;
import com.vng.zing.stats.Profiler;
import com.vng.zing.stats.ThreadProfiler;
import contracts.DataServerContract;
import contracts.MP3ServerContract;
import contracts.UserServerContract;
import javax.servlet.http.Cookie;
import models.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TTransport;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import thrift_services.UserServices;

/**
 *
 * @author Nguyen Thanh Chung
 */
public class SongServlet extends HttpServlet {

    private static final String HOST_DATA_SERVER = DataServerContract.HOST_SERVER;
    private static final int PORT_DATA_SERVER = DataServerContract.PORT;

    private static final String HOST_USER_SERVER = UserServerContract.HOST_SERVER;
    private static final int PORT_USER_SERVER = UserServerContract.PORT;

    private static final String SERVER_NAME = MP3ServerContract.SERVRE_NAME;

    private DataCacher songCache = DataCacher.getInstance();

    private static final Logger logger = LogManager.getLogger(SongServlet.class.getName());
    private static Stopwatch stopwatch = SimonManager.getStopwatch(MP3ServerContract.STOP_WATCH_SONG_SERVLET);
    private static Stopwatch otherStopwatch = SimonManager.getStopwatch("other" + MP3ServerContract.STOP_WATCH_SONG_SERVLET);

    private static String messsageForLog = "GET SONG: id = ";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ThreadProfiler profiler = Profiler.createThreadProfilerInHttpProc(MP3ServerContract.SONG_SERVLET, req);
        profiler.push(this.getClass(), "output");
        Split split = stopwatch.start();

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        TemplateLoader templateLoader = TemplateResourceLoader.create("public/hapax/");

        String id_song = req.getParameter("id");
        boolean isAdmin = this.updateCookie(req, resp);

        // Test consumer log
        String messageLog = "";

        if (id_song.equals("")) {
            try {
                Template template = templateLoader.getTemplate("song.xtm");
                TemplateDictionary templateDictionary = new TemplateDictionary();
                Template headerTemplate = templateLoader.getTemplate("partial_header.xtm");
                TemplateDictionary templateDictionaryHeader = new TemplateDictionary();

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

                templateDictionary.setVariable("footer", "partial_footer.xtm");

                out.println(template.renderToString(templateDictionary));

                // Logging and Monitoring
                split.stop();
                messageLog = FormatPureString.formatStringMessageLogs(SERVER_NAME, split.runningFor(), messsageForLog + id_song + " empty id!");
                logger.info(messageLog);

            } catch (Exception e) {
                e.printStackTrace();
                // Logging and Monitoring
                split.stop();
                messageLog = FormatPureString.formatStringMessageLogs(SERVER_NAME, split.runningFor(), messsageForLog + id_song + " " + e.getMessage());
                logger.error(messageLog);
            } finally {
                profiler.pop(this.getClass(), "output");
                Profiler.closeThreadProfiler();
            }
        } else {
            Song song = getSongById(id_song);
            if (song == null) {
                try {
                    Template template = templateLoader.getTemplate("song.xtm");
                    TemplateDictionary templateDictionary = new TemplateDictionary();
                    Template headerTemplate = templateLoader.getTemplate("partial_header.xtm");
                    TemplateDictionary templateDictionaryHeader = new TemplateDictionary();

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

                    templateDictionary.setVariable("footer", "partial_footer.xtm");

                    out.println(template.renderToString(templateDictionary));

                    // Logging and Monitoring
                    split.stop();
                    messageLog = FormatPureString.formatStringMessageLogs(SERVER_NAME, split.runningFor(), messsageForLog + " song return null!");
                    logger.info(messageLog);
                } catch (Exception e) {
                    e.printStackTrace();
                    // Logging and Monitoring
                    split.stop();
                    messageLog = FormatPureString.formatStringMessageLogs(SERVER_NAME, split.runningFor(), messsageForLog + id_song + " " + e.getMessage());
                    logger.error(messageLog);
                } finally {
                    profiler.pop(this.getClass(), "output");
                    Profiler.closeThreadProfiler();
                }
            } else {
                try {
                    Template template = templateLoader.getTemplate("song.xtm");
                    TemplateDictionary templateDictionary = new TemplateDictionary();
                    Template headerTemplate = templateLoader.getTemplate("partial_header.xtm");
                    TemplateDictionary templateDictionaryHeader = new TemplateDictionary();

                    templateDictionary.setVariable("song_name", song.name);
                    templateDictionary.setVariable("id_song", song.id);

                    ArrayList<Referencer> refsSinger = (ArrayList<Referencer>) song.getSingers();
                    for (Referencer ref : refsSinger) {
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
                    // Logging and Monitoring
                    split.stop();
                    messageLog = FormatPureString.formatStringMessageLogs(SERVER_NAME, split.runningFor(), messsageForLog + id_song + " " + "success");
                    logger.info(messageLog);

                    out.println(template.renderToString(templateDictionary));
                } catch (Exception e) {
                    e.printStackTrace();
                    // Logging and Monitoring
                    split.stop();
                    messageLog = FormatPureString.formatStringMessageLogs(SERVER_NAME, split.runningFor(), messsageForLog + id_song + " " + e.getMessage());
                    logger.error(messageLog);
                } finally {
                    profiler.pop(this.getClass(), "output");
                    Profiler.closeThreadProfiler();
                }
            }
        }

    }

    private Song getSongFromDataServerById(String id) {
        Split split = otherStopwatch.start();
        System.out.println("GET SONG:" + id + ", REQUEST TO DATA SERVER");
        Song song = null;
        ThreadProfiler profiler = Profiler.getThreadProfiler();
        try {
            TSocket socket = new TSocket(HOST_DATA_SERVER, PORT_DATA_SERVER);
            TTransport transport = new TFramedTransport(socket);
            transport.open();

            TBinaryProtocol protocol = new TBinaryProtocol(transport);

            TMultiplexedProtocol mpSongServices = new TMultiplexedProtocol(protocol, "SongServices");
            SongServices.Client songServices = new SongServices.Client(mpSongServices);

            SongResult songResult = songServices.getSongById(id);
            if (songResult.result == 0) {
                song = songResult.song;
            }
            transport.close();
            split.stop();
            String messageLog = FormatPureString.formatStringMessageLogs(SERVER_NAME, split.runningFor(),
                    messsageForLog + id + " from data_server");
            logger.info(messageLog);
        } catch (TException ex) {
            split.stop();
            String messageLog = FormatPureString.formatStringMessageLogs(SERVER_NAME, split.runningFor(),
                    messsageForLog + id + " error=" + ex.getMessage());
            logger.warn(messageLog);
            ex.printStackTrace();
            profiler.pop(this.getClass(), "output");
            Profiler.closeThreadProfiler();
        }
        return song;
    }

    private Song getSongById(String id) {
        Split split = otherStopwatch.start();
        String keySong = "song:" + id;
        if (songCache.isExisted(keySong)) {
            Song song = songCache.getCacheSong(keySong);
            split.stop();
            String messageLog = FormatPureString.formatStringMessageLogs(SERVER_NAME, split.runningFor(),
                    messsageForLog + id + " from mp3_server_cache");
            logger.info(messageLog);
            return song;

        } else {
            Song song = this.getSongFromDataServerById(id);
            if (song != null) {
                songCache.insertNewSongCache(song);
            }
            return song;
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

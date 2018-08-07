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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.DataLyric;
import models.Session;
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
import thrift_services.LyricServices;
import thrift_services.UserServices;

/**
 *
 * @author cpu11165-local
 */
public class LyricsServlet extends HttpServlet{

    private static final int PORT_DATA_SERVER = DataServerContract.PORT;
    private static final String HOST_DATA_SERVER = DataServerContract.HOST_SERVER;
    
    private static final String HOST_USER_SERVER = UserServerContract.HOST_SERVER;
    private static final int PORT_USER_SERVER = UserServerContract.PORT;
    
    private static final String SERVER_NAME = MP3ServerContract.SERVRE_NAME;
    
    private static Logger logger = LogManager.getLogger(LyricsServlet.class.getName());
    private static Stopwatch stopwatch = SimonManager.getStopwatch(MP3ServerContract.STOP_WATCH_LYRIC_SERVLET);
    private static String messsageForLog = "GET LYRIC ";
    
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        this.updateCookie(req, resp);
        
        Split split = stopwatch.start();
        
        String id = req.getParameter("id");      
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        ArrayList<DataLyric> dataLyrics= getLyricsById(id);
        // Logger
        split.stop();
        String messageLog = FormatPureString.formatStringMessageLogs(SERVER_NAME, split.runningFor(), 
                messsageForLog + "id=" +id + " : lyric_count=" + dataLyrics.size());
        logger.info(messageLog);
        
        out.println(FormatJson.convertDataLyricsToJSON(dataLyrics));
    }
    
    
    private ArrayList<DataLyric> getLyricsById(String id){
        ArrayList<DataLyric> dataLyrics = null;
        try{
            TSocket socket = new TSocket(HOST_DATA_SERVER, PORT_DATA_SERVER);
            TTransport transport = new TFramedTransport(socket);
            transport.open();
            
            TBinaryProtocol protocol = new TBinaryProtocol(transport);
            TMultiplexedProtocol mpLyricServices = new TMultiplexedProtocol(protocol, "LyricServices");
            LyricServices.Client lyricServices = new LyricServices.Client(mpLyricServices);
            dataLyrics = (ArrayList<DataLyric>) lyricServices.getDataLyricsById(id);      
            transport.close(); 
        }catch(TException ex){
            ex.printStackTrace();
        }  
        return dataLyrics;
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

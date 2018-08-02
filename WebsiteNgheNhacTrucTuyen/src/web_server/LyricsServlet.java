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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.DataLyric;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.transport.TSocket;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import thrift_services.LyricServices;
import thrift_services.SongServices;

/**
 *
 * @author cpu11165-local
 */
public class LyricsServlet extends HttpServlet{

    private static final int PORT = DataServerContract.PORT;
    private static final String HOST = DataServerContract.HOST_SERVER;
    
    private static final String SERVER_NAME = MP3ServerContract.SERVRE_NAME;
    
    private static Logger logger = LogManager.getLogger(LyricsServlet.class.getName());
    private static Stopwatch stopwatch = SimonManager.getStopwatch(MP3ServerContract.STOP_WATCH_LYRIC_SERVLET);
    private static String messsageForLog = "GET LYRIC ";
    
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
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
            TSocket transport  = new TSocket(HOST, PORT);
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
}

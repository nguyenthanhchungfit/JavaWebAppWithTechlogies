/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web_server;

import Helpers.FormatDateTimeString;
import contracts.MP3ServerContract;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.javasimon.SimonManager;
import org.javasimon.Stopwatch;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author cpu11165-local
 */
public class StatisticServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("json/application;charset=UTF-8");
        JSONObject result = new JSONObject();
        
        String pid = req.getParameter("pid");
        Stopwatch stopwatch = null;
        
        if(pid != null){
            switch(pid){
                case MP3ServerContract.PID_SS_MP3:
                    String dateGet = FormatDateTimeString.formatTime(new Date());
                    result.put("success", true);
                    result.put("date", dateGet);
                    
                    JSONArray datas = new JSONArray();
                    
                    // Song
                    stopwatch = SimonManager.getStopwatch(MP3ServerContract.STOP_WATCH_SONG_SERVLET);
                    long songCount = stopwatch.getCounter();
                    JSONObject songJSON = new JSONObject();
                    songJSON.put("mp3_song", songCount);
                    datas.add(songJSON);
                    
                    // Singer
                    stopwatch = SimonManager.getStopwatch(MP3ServerContract.STOP_WATCH_SINGER_SERVLET);
                    long singerCount = stopwatch.getCounter();
                    JSONObject singerJSON = new JSONObject();
                    singerJSON.put("mp3_singer", singerCount);
                    datas.add(singerJSON);
                    
                    // Lyric
                    stopwatch = SimonManager.getStopwatch(MP3ServerContract.STOP_WATCH_LYRIC_SERVLET);
                    long lyricCount = stopwatch.getCounter();
                    JSONObject lyricJSON = new JSONObject();
                    lyricJSON.put("mp3_lyric", lyricCount);
                    datas.add(lyricJSON);
                    
                    // Search
                    stopwatch = SimonManager.getStopwatch(MP3ServerContract.STOP_WATCH_SEARCH_SERVLET);
                    long searchCount = stopwatch.getCounter();
                    JSONObject searchJSON = new JSONObject();
                    searchJSON.put("mp3_search", searchCount);
                    datas.add(searchJSON);
                    
                    // Login
                    stopwatch = SimonManager.getStopwatch(MP3ServerContract.STOP_WATCH_LOGIN_SERVLET);
                    long loginCount = stopwatch.getCounter();
                    JSONObject loginJSON = new JSONObject();
                    loginJSON.put("mp3_login", loginCount);
                    datas.add(loginJSON);
                    
                    result.put("datas", datas);
                    // reset watch
                    break;
                case MP3ServerContract.PID_SS_SONG_MP3:
                    stopwatch = SimonManager.getStopwatch(MP3ServerContract.STOP_WATCH_SONG_SERVLET);
                    result.put("success", true);
                    result.put("count", stopwatch.getCounter());
                    break;
                case MP3ServerContract.PID_SS_SINGER_MP3:
                    result.put("success", true);
                    break;
                case MP3ServerContract.PID_SS_LYRIC_MP3:
                    result.put("success", true);
                    break;
                case MP3ServerContract.PID_SS_SEARCH_MP3:
                    result.put("success", true);
                    break;
                case MP3ServerContract.PID_SS_DATA:
                    break;
                case MP3ServerContract.PID_SS_SONG_DATA:
                    result.put("success", true);
                    break;
                case MP3ServerContract.PID_SS_SINGER_DATA:
                    result.put("success", true);
                    break;
                case MP3ServerContract.PID_SS_LYRIC_DATA:
                    result.put("success", true);
                    break;
                case MP3ServerContract.PID_SS_SEARCH_DATA:
                    result.put("success", true);
                    break;
                default:
                    result.put("success", true);
                    result.put("message", "this pid not supported!");
            }
        }else{
            result.put("success", false);
            result.put("message", "this pid not supported!");
        }
        
        PrintWriter out = resp.getWriter();
        out.print(result);
    }
    
}

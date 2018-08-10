/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contracts;

/**
 *
 * @author cpu11165-local
 */
public class MP3ServerContract {
    public static final String HOST_SERVER = "localhost";
    public static final int PORT = 8000;
    
    public static final String SERVRE_NAME = "mp3_server";
    
    public static final String STOP_WATCH_SONG_SERVLET = "sw_song_servlet";
    public static final String STOP_WATCH_SEARCH_SERVLET = "sw_search_servlet";
    public static final String STOP_WATCH_LYRIC_SERVLET = "sw_lyric_servlet";
    public static final String STOP_WATCH_SINGER_SERVLET = "sw_singer_servlet";
    public static final String STOP_WATCH_LOGIN_SERVLET = "sw_login_servlet";
    
    
    /*---------------- PID StatisticServlet --------------*/
    public static final String PID_SS_MP3 = "0000";
    public static final String PID_SS_SONG_MP3 = "01";
    public static final String PID_SS_SINGER_MP3 = "02";
    public static final String PID_SS_LYRIC_MP3 = "03";
    public static final String PID_SS_SEARCH_MP3 = "04";
    
    
    public static final String PID_SS_DATA = "1111";
    public static final String PID_SS_SONG_DATA = "11";
    public static final String PID_SS_SINGER_DATA = "12";
    public static final String PID_SS_LYRIC_DATA = "13";
    public static final String PID_SS_SEARCH_DATA = "14";
    
    /*---------------- END PID StatisticServlet --------------*/
    
    public static final String SONG_SERVLET = "SongServlet";
    public static final String SEARCH_SERVLET = "SearchServlet";
    public static final String SINGER_SERVLET = "SingerServlet";
    public static final String LYRIC_SERVLET = "LyricServlet";
    
}

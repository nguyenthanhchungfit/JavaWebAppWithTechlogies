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
public class DataServerContract {
    public static final String HOST_SERVER = "localhost";
    public static final int PORT = 8001;
    
    public static final String SERVRE_NAME = "data_server";
    
    public static final String FOLDER_KYOTO_CABINET_DB = "kyoto_cabinet_db";
    public static final String PATH_KC_SONG_DB = FOLDER_KYOTO_CABINET_DB + "/songs.kch";
    public static final String PATH_KC_SINGER_DB = FOLDER_KYOTO_CABINET_DB + "/singers.kch";
    public static final String PATH_KC_LYRIC_DB = FOLDER_KYOTO_CABINET_DB + "/lyrics.kch";
    public static final String PATH_KC_ALBUM_DB = FOLDER_KYOTO_CABINET_DB + "/albums.kch";
    
    
    public static final String STOP_WATCH_SONG_SERVICE = "sw_song_service";
    public static final String STOP_WATCH_SINGER_SERVICE = "sw_singer_service";
    public static final String STOP_WATCH_SEARCH_SERVICE = "sw_search_service";
    public static final String STOP_WATCH_LYRIC_SERVICE = "sw_lyric_service";
    
}

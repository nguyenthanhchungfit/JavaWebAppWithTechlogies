/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cache;

import server_data.DBSongModel;
import models.Song;
import models.SongResult;
import cache_data.DataCacher;

/**
 *
 * @author cpu11165-local
 */
public class SongCacheTest {
    public static void main(String[] args) {
        DataCacher songCache = DataCacher.getInstance();
//        SongResult sr = DBSongModel.getSongById("ZW9C0WDI");
//        if(sr.result == 0){
//            songCache.insertNewCache(sr.song);
//        }
        //Song song = songCache.getCacheSong("song:ZW9C0WDI");
        System.out.println(songCache.isExisted("song:ZW9C0WDI"));
    }
}

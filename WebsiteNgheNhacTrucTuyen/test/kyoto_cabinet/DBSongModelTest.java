/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kyoto_cabinet;

import Helpers.FormatJson;
import Helpers.FormatPureString;
import data_access_object.DBSongModelKyotoCabinet;
import data_access_object.DBSongModelMongo;
import elastic_search_engine.ESESong;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Song;
import models.SongResult;
import server_data.DBSongModel;

/**
 *
 * @author cpu11165-local
 */
public class DBSongModelTest {
    private static DBSongModel dbSongMG = new DBSongModelMongo();
    private static DBSongModel dbSongKC = new DBSongModelKyotoCabinet();
    
    public static void main(String[] args){
        
        testGetAllSongs();
        //testRead("ZW9C0WDI");
        //testInsertSongs();
        
        
    }
    
    private static void testInsert(String id){
        SongResult sr = dbSongMG.getSongById(id);
        if(sr.result == 0){
            Song song = sr.song;
            dbSongKC.InsertSong(song);   
        }
    }
    
    private static void testRead(String id){
        SongResult sr = dbSongKC.getSongById(id);
        if(sr.result == 0){
            System.out.println(sr.song);
        }else{
            System.out.println("failed");
        }
    }
    
    private static void testCount(){
        System.out.println("Count: " + dbSongKC.getTotalDocumentInDB());
    }
    
    private static void testClear(){
        dbSongKC.removeAllRecords();
    }
    
    private static void testInsertSongs(){
     
        ArrayList<Song> songs = (ArrayList<Song>) dbSongMG.getAllSongs();
        dbSongKC.InsertSongs(songs);
        
    }
    
    private static void testGetAllSongs(){
        ESESong eseSong = new ESESong();
        ArrayList<Song> songs = (ArrayList<Song>) dbSongKC.getAllSongs();
        for(Song song : songs){
            System.out.println(song);
        }
    }
}

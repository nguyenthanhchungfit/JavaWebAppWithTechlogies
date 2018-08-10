/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kyoto_cabinet;

import data_access_object.DBLyricModelKyotoCabinet;
import data_access_object.DBLyricModelMongo;
import java.util.ArrayList;
import models.Lyric;
import server_data.DBLyricModel;


/**
 *
 * @author cpu11165-local
 */
public class DBLyricModelTest {
    
    private static final DBLyricModel dbMG = new DBLyricModelMongo();
    private static final DBLyricModel dbKC = new DBLyricModelKyotoCabinet();
    
    public static void main(String[] args) {
        //Lyric lyric = new Lyric();
        //lyric.id = "ZWZABABU";
        //testGetLyric();
    }
    
    private static void testInsertNewLyric(){
        String id = "ZW9C0WDI";
        Lyric lyric = dbMG.getLyric(id);
        System.out.println(lyric);
        dbKC.InsertLyric(lyric);
    }
    
    private static void testGetLyric(){
        String id = "ZWZABABU";
        Lyric lyric = dbKC.getLyric(id);
        System.out.println(lyric);
    }
    
    private static void testGetCount(){
        System.out.println("Count: " + dbKC.getTotalDocumentInDB());
    }
    
    private static void testClear(){
        dbKC.removeAllRecords();
    }
    
    private static void testInsertLyrics(){
        ArrayList<Lyric> lyrics = (ArrayList<Lyric>) dbMG.getAllLyrics();
        dbKC.InsertLyrics(lyrics);
    }
}

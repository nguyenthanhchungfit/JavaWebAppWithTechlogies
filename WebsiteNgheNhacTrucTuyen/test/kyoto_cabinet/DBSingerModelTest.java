/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kyoto_cabinet;

import data_access_object.DBSingerModelKyotoCabinet;
import data_access_object.DBSingerModelMongo;
import java.util.ArrayList;
import models.Singer;
import server_data.DBSingerModel;

/**
 *
 * @author cpu11165-local
 */
public class DBSingerModelTest {
    
    private static final DBSingerModel dbMG = new DBSingerModelMongo();
    private static final DBSingerModel dbKC = new DBSingerModelKyotoCabinet();
    
    public static void main(String[] args) {
        //testInsertSingers();
        //testInsertSinger();
        //testGetSinger();
        testGetCount();
//       testClear();
//        testGetCount();
    }
    
    private static void testInsertSinger(){
        String id = "IWZFE0CD";
        Singer singer = dbMG.getSingerInformation(id);
        dbKC.InsertSinger(singer);
    }
    
    private static void testGetSinger(){
        String id = "IWZFE0CD";
        Singer singer = dbKC.getSingerInformation(id);
        System.out.println(singer);
    }
    
    private static void testGetCount(){
        System.out.println(dbKC.getTotalDocumentInDB());
    }
    
    private static void testInsertSingers(){
        ArrayList<Singer> singers = (ArrayList<Singer>) dbMG.getAllSingers();
        dbKC.InsertSingers(singers);
    }
    
    private static void testClear(){
        dbKC.removeAllRecords();
    }
    
}

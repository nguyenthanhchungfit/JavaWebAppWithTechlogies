/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kyoto_cabinet;

import data_access_object.DBAlbumModelKyotoCabinet;
import data_access_object.DBAlbumModelMongo;
import java.util.ArrayList;
import models.Album;
import server_data.DBAlbumModel;

/**
 *
 * @author cpu11165-local
 */
public class DBAlbumModelTest {
    
    private static final DBAlbumModel dbMG = new DBAlbumModelMongo();
    private static final DBAlbumModel dbKC = new DBAlbumModelKyotoCabinet();
    
    public static void main(String[] args) {
        testInsertAlbums();
        testGetCount();
    }
    
    private static void testInsertNewAlbum(){
        String id = "ZOCW9A0W";
        Album album = dbMG.getAlbum(id);
        System.out.println(album);
        dbKC.InsertAlbum(album);
    }
    
    private static void testGetAlbum(){
        String id = "ZOCW9A0W";
        Album album = dbKC.getAlbum(id);
        System.out.println(album);
    }
    
    private static void testGetCount(){
        System.out.println("Count : " + dbKC.getTotalDocumentInDB());
    }
    
    private static void testClear(){
        dbKC.removeAllRecords();
    }
    
    private static void testInsertAlbums(){
        ArrayList<Album> albums = (ArrayList<Album>) dbMG.getAllAlbums();
        dbKC.InsertAlbums(albums);
    }
}

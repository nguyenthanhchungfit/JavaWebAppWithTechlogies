/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlerzingmp3;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author cpu11165-local
 */
public class InsertData {
    
    static final String DATABASE = "app_mp3";
    static final String COLLECTION_SONGS = "songs";
    static final String COLLECTION_SINGERS = "singers";
    static final String HOST = "localhost";
    static final int PORT = 27017;
    static final String USERNAME = "thanhchung";
    static final String PASSWORD = "123";
    
    static MongoClient mongo = null;
    static MongoCredential credential = null;
    static MongoDatabase mongo_db = null;
    static MongoCollection<Document> collectionSongs = null;
    static MongoCollection<Document> collectionSingers = null;
    
    static{
        mongo = new MongoClient(HOST, PORT);
        credential = MongoCredential.createCredential(USERNAME, DATABASE, PASSWORD.toCharArray());
        mongo_db = mongo.getDatabase(DATABASE);
        collectionSongs = mongo_db.getCollection(COLLECTION_SONGS);
        collectionSingers = mongo_db.getCollection(COLLECTION_SINGERS);
        System.out.println("OK");
        
    }
    
    public static void InsertSinger(SingerModel singer){
        Document document = new Document("id", singer.getId())
                            .append("name", singer.getName())
                            .append("realname", singer.getRealName())
                            .append("dob", singer.getDob())
                            .append("country", singer.getCountry())
                            .append("description", singer.getDescription());
        
        collectionSingers.insertOne(document);
    }
    
    public static void InsertSingers(List<SingerModel> list){
        
    }
    
    public static void InsertSong(SongModel song){
        
        ArrayList<SingerModel> singers = song.getSingers();
        
        
        ArrayList<Document> docs = new ArrayList<>();
        
        for(SingerModel singer : singers){        
            Document doc = new Document();
            doc.put("id", singer.getId());
            doc.put("name", singer.getName());
            docs.add(doc);
        }
        
        Document document = new Document("id", song.getId())
                            .append("name", song.getName())
                            .append("album", song.getAlbum())
                            .append("lyrics", song.getLyrics())
                            .append("composers", song.getComposers())
                            .append("kinds", song.getKinds())
                            .append("singers", docs);
        collectionSongs.insertOne(document);
    }
    
    public static void InsertSongs(List<SongModel> songs){
        
    }
    
    public static boolean isExistedSong(String id){
        
        FindIterable<Document> k = collectionSongs.find(new Document("id", id));
        if(k.iterator().hasNext()){
            return true;
        }
        return false;
    }
    
    public static boolean isExistedSinger(String id){
        FindIterable<Document> k = collectionSingers.find(new Document("id", id));
        if(k.iterator().hasNext()){
            return true;
        }
        return false;
    }
}

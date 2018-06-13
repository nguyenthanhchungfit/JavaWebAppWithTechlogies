/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data_server;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import models.Singer;
import models.Song;
import models.SongResult;
import org.bson.Document;

/**
 *
 * @author Nguyen Thanh Chung
 */
public class SongDB {
    private static MongoClient mongo = null;
    private static MongoCredential credential = null;
    private static MongoDatabase mongo_db = null;
    private static MongoCollection<Document> collectionSongs = null;
    
    static{
        mongo = new MongoClient(DBContracts.HOST, DBContracts.PORT);
        credential = MongoCredential.createCredential(DBContracts.USERNAME
                , DBContracts.DATABASE_NAME, DBContracts.PASSWORD.toCharArray());
        mongo_db = mongo.getDatabase(DBContracts.DATABASE_NAME);
        collectionSongs = mongo_db.getCollection(DBContracts.COLLECTION_SONGS);
    }
    
    public static SongResult getSongByName(String name){
        SongResult sr = new SongResult();
        
        Document regQuery = new Document();
        regQuery.append("$regex", "^(?)" + Pattern.quote(name));
        regQuery.append("$options", "i");
        
        Document findQuery = new Document();
        findQuery.append("name", regQuery);

        FindIterable<Document> docs = collectionSongs.find(findQuery);
        //Iterator it = docs.iterator();
        
//        while(it.hasNext()){
//            Document doc = (Document) it.next();
//            System.out.println(doc.getString("id"));
//        }
      
        Document doc = docs.first();
        if(doc != null){
            sr.result = 0;
            sr.song = new Song();
            sr.song.id = doc.getString("id");
            sr.song.name = doc.getString("name");
            sr.song.album = doc.getString("album");
            sr.song.lyrics = (List<String>) doc.get("lyrics");
            sr.song.composers  = (List<String>) doc.get("composers");
            sr.song.kinds = (List<String>) doc.get("kinds");
            List<Document> singers = (List<Document>) doc.get("singers");
            sr.song.singers = new ArrayList<>();
            for(Document docSinger : singers){
                String sId = docSinger.getString("id");
                String sName = docSinger.getString("name");
                Singer singer = new Singer();
                singer.id = sId;
                singer.name = sName;
                sr.song.singers.add(singer);
            }
        }else{
            sr.result = -1;
            sr.song = null;
        }
        return sr;
    } 
    
    public static String getLyric(String id, int page)
    {
        FindIterable<Document> docs = collectionSongs.find(eq("id", id));
        Document doc = docs.first();
        if(doc != null){
            List<String> arrLyrics = (List<String>)doc.get("lyrics");
            if(page >= arrLyrics.size() || page < 0){
                return "";
            }else{
                return arrLyrics.get(page);
            }
        }else{
            return "";
        }
    }
    
    public static void getNameInsensitive(String name){
        Document regQuery = new Document();
        regQuery.append("$regex", "^(?)" + Pattern.quote(name));
        regQuery.append("$options", "i");
        
        Document findQuery = new Document();
        findQuery.append("name", regQuery);
        FindIterable<Document> iterable = collectionSongs.find(findQuery);
        Iterator<Document> it = iterable.iterator();
        while(it.hasNext()){
            Document doc = it.next();
            System.out.println(doc.getString("id"));
        }
    }
    
    public static boolean isExistedSong(String id){
        
        FindIterable<Document> k = collectionSongs.find(new Document("id", id));
        if(k.iterator().hasNext()){
            return true;
        }
        return false;
    }
    
    public static void InsertSong(Song song){
        
        ArrayList<Singer> singers = (ArrayList<Singer>) song.getSingers();
        ArrayList<Document> docs = new ArrayList<>();
        
        for(Singer singer : singers){        
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
    
    public static void InsertSongs(List<Song> songs){
        
    }
}



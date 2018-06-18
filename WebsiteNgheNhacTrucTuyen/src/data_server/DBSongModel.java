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
import models.Referencer;
import org.bson.Document;

/**
 *
 * @author Nguyen Thanh Chung
 */
public class DBSongModel {
    private static MongoClient mongo = null;
    private static MongoCredential credential = null;
    private static MongoDatabase mongo_db = null;
    private static MongoCollection<Document> collectionSongs = null;
    
    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_ALBUM = "album";
    private static final String FIELD_LYRIC = "lyric";
    private static final String FIELD_COMPOSERS = "composers";
    private static final String FIELD_KARA = "kara";
    private static final String FIELD_DURATION = "duration";
    private static final String FIELD_KINDS = "kinds";
    private static final String FIELD_SINGERS = "singers";
    private static final String FIELD_VIEWS = "views";
    private static final String FIELD_COMMENT = "comment";
    private static final String FIELD_IMAGE = "image";
    
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
      
        Document doc = docs.first();
        
        if(doc != null){
            sr.result = 0;
            sr.song = new Song();
            sr.song.id = doc.getString("id");
            sr.song.name = doc.getString("name");
            sr.song.album = (Referencer) doc.get("album");
            sr.song.lyrics =  (String) doc.get("lyrics");
            sr.song.composers  = (List<String>) doc.get("composers");
            sr.song.kinds =  (List<Referencer>) doc.get("kinds");
            List<Document> singers = (List<Document>) doc.get("singers");
            sr.song.singers = new ArrayList<>();
            for(Document docSinger : singers){
                String sId = docSinger.getString("id");
                String sName = docSinger.getString("name");
                Referencer singer = new Referencer();
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
        
        ArrayList<Document> singer_docs = DBContracts.getReferencers((ArrayList<Referencer>) song.singers);
        ArrayList<Document> kind_docs = DBContracts.getReferencers((ArrayList<Referencer>) song.kinds);
        Document album_doc = DBContracts.getReferencer(song.album);
        
        Document document = new Document(FIELD_ID, song.id)
                            .append(FIELD_NAME, song.name)
                            .append(FIELD_ALBUM, album_doc)
                            .append(FIELD_LYRIC, song.lyrics)
                            .append(FIELD_COMPOSERS, song.composers)
                            .append(FIELD_KARA, song.kara)
                            .append(FIELD_DURATION, song.duration)
                            .append(FIELD_KINDS, kind_docs)
                            .append(FIELD_SINGERS, singer_docs)
                            .append(FIELD_VIEWS, song.views)
                            .append(FIELD_COMMENT, song.comment)
                            .append(FIELD_IMAGE, song.image);
        
        collectionSongs.insertOne(document);
    }
    
    public static void InsertSongs(List<Song> songs){
        for(Song song : songs){
            InsertSong(song);
        }
    }
    
    // Kiểm tra album đã tồn tại trong song chưa
    public static boolean isExistedAlbumInSong(String idSong, String idAlbum){
        FindIterable<Document> k = collectionSongs.find(new Document(FIELD_ID, idSong));
        if(k != null){
            Document doc = k.first();
            String idAlbumFind = doc.getString(FIELD_ALBUM);
            if(idAlbum.equals(idAlbumFind)){
                return true;
            }
        }
        return false;
    }
    
    
}



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data_server;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.List;
import models.Referencer;
import models.Singer;
import org.bson.Document;

/**
 *
 * @author Nguyen Thanh Chung
 */

public class SingerDB {
    private static MongoClient mongo = null;
    private static MongoCredential credential = null;
    private static MongoDatabase mongo_db = null;
    private static MongoCollection<Document> collectionSingers = null;
    
    static{
        mongo = new MongoClient(DBContracts.HOST, DBContracts.PORT);
        credential = MongoCredential.createCredential(DBContracts.USERNAME
                , DBContracts.DATABASE_NAME, DBContracts.PASSWORD.toCharArray());
        mongo_db = mongo.getDatabase(DBContracts.DATABASE_NAME);
        collectionSingers = mongo_db.getCollection(DBContracts.COLLECTION_SINGERS);
    }
    
    public static Singer getSingerInformation(String idSinger){
        Singer singer = null;
        FindIterable<Document> docs = collectionSingers.find(new Document("id", idSinger));
        Document doc = docs.first();
        if(null!= doc){
            singer = new Singer();
            singer.id = doc.getString("id");
            singer.name = doc.getString("name");
            singer.realname = doc.getString("realname");
            singer.dob = doc.getString("dob");
            singer.country = doc.getString("country");
            singer.description = doc.getString("description");
            singer.songs = (List<Referencer>) doc.get("songs");
            singer.videos = (List<Referencer>) doc.get("videos");
            singer.albums = (List<Referencer>) doc.get("albums");
            singer.imgAvatar = doc.getString("img_avatar");
            singer.imgCover = doc.getString("img_cover");
        }
        return singer;
    }
    
    public static boolean isExistedSinger(String id){
        FindIterable<Document> k = collectionSingers.find(new Document("id", id));
        if(k.iterator().hasNext()){
            return true;
        }
        return false;
    }
    
    public static void InsertSinger(Singer singer){
        Document document = new Document("id", singer.getId())
                            .append("name", singer.getName())
                            .append("realname", singer.getRealname())
                            .append("dob", singer.getDob())
                            .append("country", singer.getCountry())
                            .append("description", singer.getDescription());
        
        collectionSingers.insertOne(document);
    }
    
    public static void InsertSingers(List<Singer> list){
        
    }
}

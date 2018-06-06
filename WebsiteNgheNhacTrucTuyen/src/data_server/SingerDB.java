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
            singer = new Singer(doc.getString("id"), doc.getString("name"),
                    doc.getString("realname"), doc.getString("dob"),
                    doc.getString("country"), doc.getString("description")); 
        }
        return singer;
    }
    
    
}

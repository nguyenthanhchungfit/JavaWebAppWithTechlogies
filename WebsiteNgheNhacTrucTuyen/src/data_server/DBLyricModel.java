/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data_server;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import models.DataLyric;
import models.Lyric;
import org.bson.Document;

/**
 *
 * @author cpu11165-local
 */
public class DBLyricModel {
    private static MongoClient mongo = null;
    private static MongoCredential credential = null;
    private static MongoDatabase mongo_db = null;
    private static MongoCollection<Document> collectionLyrics = null;
    private static final String FIELD_ID = "id";
    private static final String FIELD_DATAS = "datas";
    
    
    static{
        mongo = new MongoClient(DBContracts.HOST, DBContracts.PORT);
        credential = MongoCredential.createCredential(DBContracts.USERNAME
                , DBContracts.DATABASE_NAME, DBContracts.PASSWORD.toCharArray());
        mongo_db = mongo.getDatabase(DBContracts.DATABASE_NAME);
        collectionLyrics = mongo_db.getCollection(DBContracts.COLLECTION_LYRICS);
    }
    
    public static void InsertLyric(Lyric lyric){
        
        ArrayList<Document> song_docs = DBContracts.getReferencersLyric((ArrayList<DataLyric>) lyric.datas);
        
        Document doc = new Document(FIELD_ID, lyric.id)
                            .append(FIELD_DATAS, song_docs);
        
        collectionLyrics.insertOne(doc);
    }
}

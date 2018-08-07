/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_data;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 *
 * @author cpu11165-local
 */
public class DBVideoModel {
    private static MongoClient mongo = null;
    private static MongoCredential credential = null;
    private static MongoDatabase mongo_db = null;
    private static MongoCollection<Document> collectionVideos = null;
    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_SINGERS = "singers";
    private static final String FIELD_LYRIC = "lyric";
    private static final String FIELD_QUALITIES = "qualities";
    private static final String FIELD_VIEWS = "views";
    private static final String FIELD_DURATION = "duration";
    private static final String FIELD_COMMENT = "comment";
    private static final String FIELD_IMAGE= "image";

    
    
    static{
        mongo = new MongoClient(DBDataContracts.HOST, DBDataContracts.PORT);
        credential = MongoCredential.createCredential(DBDataContracts.USERNAME
                , DBDataContracts.DATABASE_NAME, DBDataContracts.PASSWORD.toCharArray());
        mongo_db = mongo.getDatabase(DBDataContracts.DATABASE_NAME);
        collectionVideos = mongo_db.getCollection(DBDataContracts.COLLECTION_VIDEOS);
    }
}

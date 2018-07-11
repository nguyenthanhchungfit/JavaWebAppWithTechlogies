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
public class DBUserModel {
    private static MongoClient mongo = null;
    private static MongoCredential credential = null;
    private static MongoDatabase mongo_db = null;
    private static MongoCollection<Document> collectionUsers = null;
    private static final String FIELD_USERNAME = "username";
    private static final String FIELD_PASSWORD = "password";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_ADDRESS = "address";
    private static final String FIELD_DOB = "dob";
    private static final String FIELD_PHONE = "phone";
    private static final String FIELD_SEX = "sex";
    private static final String FIELD_FAVOR_LIST_SONGS = "favor_list_songs";
    private static final String FIELD_FAVOR_LIST_SINGERS = "favor_list_singers";

    
    
    static{
        mongo = new MongoClient(DBDataContracts.HOST, DBDataContracts.PORT);
        credential = MongoCredential.createCredential(DBDataContracts.USERNAME
                , DBDataContracts.DATABASE_NAME, DBDataContracts.PASSWORD.toCharArray());
        mongo_db = mongo.getDatabase(DBDataContracts.DATABASE_NAME);
        collectionUsers = mongo_db.getCollection(DBDataContracts.COLLECTION_USERS);
    }
}

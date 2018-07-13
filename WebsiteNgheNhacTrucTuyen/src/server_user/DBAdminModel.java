/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_user;

import Helpers.EncryptAndDecrypt;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Date;
import org.bson.Document;
import server_user.DBUserContract;

/**
 *
 * @author cpu11165-local
 */
public class DBAdminModel {
    private static MongoClient mongo = null;
    private static MongoCredential credential = null;
    private static MongoDatabase mongo_db = null;
    private static MongoCollection<Document> collectionAdmins = null;
    
    private static final String FIELD_USERNAME = "username";
    private static final String FIELD_PASSWORD = "password";
    private static final String FIELD_CREATE_DATE = "create_date";
    private static final String FIELD_UPDATE_DATE = "update_date";
    private static final String FIELD_TYPE = "type_user";
    
    private static final EncryptAndDecrypt encode_pw = new EncryptAndDecrypt();
    
    static{
        mongo = new MongoClient(DBUserContract.HOST, DBUserContract.PORT);
        credential = MongoCredential.createCredential(DBUserContract.USERNAME,
                 DBUserContract.DATABASE_NAME, DBUserContract.PASSWORD.toCharArray());
        mongo_db = mongo.getDatabase(DBUserContract.DATABASE_NAME);
        collectionAdmins = mongo_db.getCollection(DBUserContract.COLLECTION_ADMINS); 
    }
    
    
    public void insertNewAdmin(String username, String password) throws GeneralSecurityException, UnsupportedEncodingException{
        
        if(this.isExistedUsername(username)){
            return;
        }
        
        String encryptPassword = encode_pw.encrypt(password);
        Date date = new Date();
        String strDate = DBUserContract.DATE_TIME_FORMATTER.format(date);
        Document document = new Document(FIELD_USERNAME, username)
                                        .append(FIELD_PASSWORD, encryptPassword)
                                        .append(FIELD_CREATE_DATE, strDate)
                                        .append(FIELD_UPDATE_DATE, strDate)
                                        .append(FIELD_TYPE, 0);
        
        collectionAdmins.insertOne(document);
    }
    
    public int isValidAccount(String username, String password) throws GeneralSecurityException, IOException{
        Document query = new Document(FIELD_USERNAME, username);
        FindIterable<Document> k = collectionAdmins.find(query);
        if (k.iterator().hasNext()) {
            Document doc = k.first();
            boolean isMatchPassword = encode_pw.isValidPassword(doc.getString(FIELD_PASSWORD), password);
            if(isMatchPassword){
                return doc.getInteger(FIELD_TYPE,-1);
            }
            return -1;
        }
        return -1;
    }
    
    public boolean isExistedUsername(String username){
        Document query = new Document(FIELD_USERNAME, username);
        FindIterable<Document> k = collectionAdmins.find(query);
        if (k.iterator().hasNext()) {
            return true;
        }
        return false;
    }
    
    public long getTotalDocumentInDB(){
        long count = 0;
        FindIterable<Document> iter = collectionAdmins.find();
        for(Document doc : iter){
            count++;
        }
        return count;
    }
}

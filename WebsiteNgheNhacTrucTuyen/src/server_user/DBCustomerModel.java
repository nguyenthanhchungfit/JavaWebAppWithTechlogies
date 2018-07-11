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
import models.Customer;
import org.bson.Document;

/**
 *
 * @author cpu11165-local
 */
public class DBCustomerModel {
    private static MongoClient mongo = null;
    private static MongoCredential credential = null;
    private static MongoDatabase mongo_db = null;
    private static MongoCollection<Document> collectionCustomers = null;
    
    private static final String FIELD_USERNAME = "username";
    private static final String FIELD_PASSWORD = "password";
    private static final String FIELD_FULLNAME = "fullname";
    private static final String FIELD_DOB = "dob";
    private static final String FIELD_ADDRESS = "address";
    private static final String FIELD_SEX = "sex";
    private static final String FIELD_EMAIL = "email";
    private static final String FIELD_IMAGE = "image";
    private static final String FIELD_CREATE_DATE = "create_date";
    private static final String FIELD_UPDATE_DATE = "update_date";
    private static final String FIELD_TYPE = "type_user";
    
    private static final EncryptAndDecrypt encode_pw = new EncryptAndDecrypt();
    
    static{
        mongo = new MongoClient(DBUserContract.HOST, DBUserContract.PORT);
        credential = MongoCredential.createCredential(DBUserContract.USERNAME,
                 DBUserContract.DATABASE_NAME, DBUserContract.PASSWORD.toCharArray());
        mongo_db = mongo.getDatabase(DBUserContract.DATABASE_NAME);
        collectionCustomers = mongo_db.getCollection(DBUserContract.COLLECTION_CUSTOMERS); 
    }
    
    
    public void insertNewCustomer(Customer newCustomer) throws GeneralSecurityException, UnsupportedEncodingException{
        
        if(this.isExistedUsername(newCustomer.username)) return;
        
        String encryptPassword = encode_pw.encrypt(newCustomer.password);
        Date date = new Date();
        String strDate = DBUserContract.DATE_TIME_FORMATTER.format(date);
        Document document = new Document(FIELD_USERNAME, newCustomer.getUsername())
                                        .append(FIELD_PASSWORD, encryptPassword)
                                        .append(FIELD_FULLNAME, newCustomer.getFullname())
                                        .append(FIELD_DOB, newCustomer.getDob())
                                        .append(FIELD_ADDRESS, newCustomer.getAddress())
                                        .append(FIELD_SEX, newCustomer.sex)
                                        .append(FIELD_EMAIL, newCustomer.getEmail())
                                        .append(FIELD_IMAGE, "")
                                        .append(FIELD_CREATE_DATE, strDate)
                                        .append(FIELD_UPDATE_DATE, strDate)
                                        .append(FIELD_TYPE, 1);
        
        collectionCustomers.insertOne(document);
    }
    
    public int isValidAccount(String username, String password) throws GeneralSecurityException, IOException{
        Document query = new Document(FIELD_USERNAME, username);
        FindIterable<Document> k = collectionCustomers.find(query);
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
        FindIterable<Document> k = collectionCustomers.find(query);
        if (k.iterator().hasNext()) {
            return true;
        }
        return false;
    }
}

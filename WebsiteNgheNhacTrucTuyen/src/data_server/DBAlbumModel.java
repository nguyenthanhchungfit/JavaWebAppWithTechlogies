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
import models.Album;
import models.Referencer;
import org.bson.Document;

/**
 *
 * @author cpu11165-local
 */
public class DBAlbumModel {
    private static MongoClient mongo = null;
    private static MongoCredential credential = null;
    private static MongoDatabase mongo_db = null;
    private static MongoCollection<Document> collectionAlbums = null;
    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_SONGS = "songs";
    private static final String FIELD_IMAGE = "image";

    
    
    static{
        mongo = new MongoClient(DBContracts.HOST, DBContracts.PORT);
        credential = MongoCredential.createCredential(DBContracts.USERNAME
                , DBContracts.DATABASE_NAME, DBContracts.PASSWORD.toCharArray());
        mongo_db = mongo.getDatabase(DBContracts.DATABASE_NAME);
        collectionAlbums = mongo_db.getCollection(DBContracts.COLLECTION_ALBUMS);
    }
    
    public static void InsertAlbum(Album album){
        
        ArrayList<Document> song_docs = DBContracts.getReferencers((ArrayList<Referencer>) album.songs);
        
        Document doc = new Document(FIELD_ID , album.id)
                            .append(FIELD_NAME, album.name)
                            .append(FIELD_IMAGE, album.image)
                            .append(FIELD_SONGS, song_docs);
        
        collectionAlbums.insertOne(doc);
        
    }
}

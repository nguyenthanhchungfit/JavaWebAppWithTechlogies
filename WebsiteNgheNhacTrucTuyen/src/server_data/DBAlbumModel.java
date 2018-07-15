/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_data;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.FindIterable;
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

    static {
        mongo = new MongoClient(DBDataContracts.HOST, DBDataContracts.PORT);
        credential = MongoCredential.createCredential(DBDataContracts.USERNAME,
                 DBDataContracts.DATABASE_NAME, DBDataContracts.PASSWORD.toCharArray());
        mongo_db = mongo.getDatabase(DBDataContracts.DATABASE_NAME);
        collectionAlbums = mongo_db.getCollection(DBDataContracts.COLLECTION_ALBUMS);
    }

    public static boolean isExistedAlbum(String id) {

        FindIterable<Document> k = collectionAlbums.find(new Document("id", id));
        if (k.iterator().hasNext()) {
            return true;
        }
        return false;
    }

    public static void InsertAlbum(Album album) {
        if (!isExistedAlbum(album.id)) {
            ArrayList<Document> song_docs = DBDataContracts.getReferencers((ArrayList<Referencer>) album.songs);

            Document doc = new Document(FIELD_ID, album.id)
                    .append(FIELD_NAME, album.name)
                    .append(FIELD_IMAGE, album.image)
                    .append(FIELD_SONGS, song_docs);

            collectionAlbums.insertOne(doc);
        }
    }
    
    
    public long getTotalDocumentInDB(){
        long count = 0;
        FindIterable<Document> iter = collectionAlbums.find();
        for(Document doc : iter){
            count++;
        }
        return count;
    }
}

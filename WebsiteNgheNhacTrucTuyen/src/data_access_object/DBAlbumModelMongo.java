/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data_access_object;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.List;
import models.Album;
import models.AlbumResult;
import models.ModelInitiation;
import models.Referencer;
import org.bson.Document;
import server_data.DBAlbumModel;
import server_data.DBDataContracts;

/**
 *
 * @author cpu11165-local
 */
public class DBAlbumModelMongo implements DBAlbumModel{
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

    @Override
    public boolean isExistedAlbum(String id) {

        FindIterable<Document> k = collectionAlbums.find(new Document("id", id));
        if (k.iterator().hasNext()) {
            return true;
        }
        return false;
    }

    @Override
    public void InsertAlbum(Album album) {
        if (!isExistedAlbum(album.id)) {
            ArrayList<Document> song_docs = DBDataContracts.getReferencers((ArrayList<Referencer>) album.songs);

            Document doc = new Document(FIELD_ID, album.id)
                    .append(FIELD_NAME, album.name)
                    .append(FIELD_IMAGE, album.image)
                    .append(FIELD_SONGS, song_docs);

            collectionAlbums.insertOne(doc);
        }
    }
    
    @Override
    public long getTotalDocumentInDB(){
        return collectionAlbums.count();
    }

    @Override
    public void removeAllRecords() {
        Document filter = new Document();
        collectionAlbums.deleteMany(filter);
    }

    @Override
    public void InsertAlbums(List<Album> albums) {
        for(Album album : albums){
            InsertAlbum(album);
        }
    }

    @Override
    public AlbumResult getAlbumResult(String id) {
        AlbumResult ar = new AlbumResult();
        ar.result = -1;
        ar.album = null;
        
        Album album = getAlbum(id);
        if(album != null){
            ar.result = 0;
            ar.album = album;
        }
        return ar;
    }

    @Override
    public Album getAlbum(String id) {
        Album album = null;
        FindIterable<Document> docs = collectionAlbums.find(new Document("id", id));
        Document doc = docs.first();
        if (null != doc) {
            album = new Album();
            ModelInitiation.initAlbum(album);      
            
            album.id = doc.getString(FIELD_ID);
            album.name = doc.getString(FIELD_NAME);
            album.image = doc.getString(FIELD_IMAGE);
            List<Document> song_docs = (List<Document>) doc.get("songs");
            
            for (Document docSong : song_docs) {
                String sID = docSong.getString("id");
                String sName = docSong.getString("name");
                album.songs.add(new Referencer(sID, sName));
            }
        }
        return album;
    }

    @Override
    public List<Album> getAllAlbums() {
        ArrayList<Album> albums = new ArrayList<>();
        FindIterable<Document> docs = collectionAlbums.find();
        
        for(Document doc : docs){
            Album album = new Album();
            ModelInitiation.initAlbum(album);      
            
            album.id = doc.getString(FIELD_ID);
            album.name = doc.getString(FIELD_NAME);
            album.image = doc.getString(FIELD_IMAGE);
            List<Document> song_docs = (List<Document>) doc.get("songs");
            
            for (Document docSong : song_docs) {
                String sID = docSong.getString("id");
                String sName = docSong.getString("name");
                album.songs.add(new Referencer(sID, sName));
            }
            
            albums.add(album);
        }
        
        return albums;
    }
}

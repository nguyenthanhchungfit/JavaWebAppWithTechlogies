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
import models.DataLyric;
import models.Lyric;
import models.LyricResult;
import models.ModelInitiation;
import models.Referencer;
import org.bson.Document;
import server_data.DBDataContracts;
import server_data.DBLyricModel;

/**
 *
 * @author cpu11165-local
 */
public class DBLyricModelMongo implements DBLyricModel {

    private static MongoClient mongo = null;
    private static MongoCredential credential = null;
    private static MongoDatabase mongo_db = null;
    private static MongoCollection<Document> collectionLyrics = null;
    private static final String FIELD_ID = "id";
    private static final String FIELD_DATAS = "datas";

    static {
        mongo = new MongoClient(DBDataContracts.HOST, DBDataContracts.PORT);
        credential = MongoCredential.createCredential(DBDataContracts.USERNAME,
                DBDataContracts.DATABASE_NAME, DBDataContracts.PASSWORD.toCharArray());
        mongo_db = mongo.getDatabase(DBDataContracts.DATABASE_NAME);
        collectionLyrics = mongo_db.getCollection(DBDataContracts.COLLECTION_LYRICS);
    }

    public boolean isExistedLyric(String id) {

        FindIterable<Document> k = collectionLyrics.find(new Document("id", id));
        if (k.iterator().hasNext()) {
            return true;
        }
        return false;
    }

    public void InsertLyric(Lyric lyric) {
        if (!isExistedLyric(lyric.id)) {
            ArrayList<Document> song_docs = DBDataContracts.getReferencersLyric((ArrayList<DataLyric>) lyric.datas);

            Document doc = new Document(FIELD_ID, lyric.id)
                    .append(FIELD_DATAS, song_docs);

            collectionLyrics.insertOne(doc);
        }
    }

    public List<DataLyric> getDataLyricsById(String id) {
        ArrayList<DataLyric> dataLyrics = new ArrayList<>();

        Lyric lyric = getLyric(id);
        if (lyric != null) {
            dataLyrics = (ArrayList<DataLyric>) lyric.datas;
        }

        return dataLyrics;
    }

    public long getTotalDocumentInDB() {
        return collectionLyrics.count();
    }

    @Override
    public void removeAllRecords() {
        Document filter = new Document();
        collectionLyrics.deleteMany(filter);
    }

    @Override
    public void InsertLyrics(List<Lyric> lyrics) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LyricResult getLyricResult(String id) {
        LyricResult lr = new LyricResult();
        lr.result = -1;
        lr.lyric = null;

        FindIterable<Document> docs = collectionLyrics.find(new Document(FIELD_ID, id));
        if (docs != null) {
            Document doc = docs.first();
            Lyric lyric = new Lyric();
            ModelInitiation.initLyric(lyric);
            lyric.id = (String) doc.get(FIELD_ID);

            ArrayList<Document> data_docs = (ArrayList<Document>) doc.get(FIELD_DATAS);
            for (Document data_doc : data_docs) {
                String contributor = data_doc.getString("contributor");
                String content = data_doc.getString("content");
                lyric.datas.add(new DataLyric(contributor, content));
            }

            lr.result = 0;
            lr.lyric = lyric;
        }

        return lr;
    }

    @Override
    public Lyric getLyric(String id) {
        LyricResult lr = getLyricResult(id);
        return lr.lyric;
    }

    @Override
    public List<Lyric> getAllLyrics() {
        ArrayList<Lyric> lyrics = new ArrayList<>();

        FindIterable<Document> docs = collectionLyrics.find();

        if (docs != null) {
            for (Document doc : docs) {
                
                Lyric lyric = new Lyric();
                ModelInitiation.initLyric(lyric);
                
                lyric.id = (String) doc.get(FIELD_ID);
                ArrayList<Document> data_docs = (ArrayList<Document>) doc.get(FIELD_DATAS);
                for (Document data_doc : data_docs) {
                    String contributor = data_doc.getString("contributor");
                    String content = data_doc.getString("content");
                    lyric.datas.add(new DataLyric(contributor, content));
                }
                lyrics.add(lyric);
            }
        }
        return lyrics;
    }
}

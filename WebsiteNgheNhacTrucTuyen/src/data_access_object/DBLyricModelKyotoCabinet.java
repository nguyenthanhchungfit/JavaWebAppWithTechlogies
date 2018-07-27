/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data_access_object;

import Helpers.SerialAndDeSerial;
import contracts.DataServerContract;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import kyotocabinet.DB;
import models.DataLyric;
import models.Lyric;
import models.LyricResult;
import server_data.DBLyricModel;

/**
 *
 * @author cpu11165-local
 */
public class DBLyricModelKyotoCabinet implements DBLyricModel {

    private static final DB db_kyoto = new DB();
    private static final String PATH_FILE_DB = DataServerContract.PATH_KC_LYRIC_DB;

    @Override
    public boolean isExistedLyric(String id) {
        boolean success_db = true;
        boolean isExisted = false;
        if (!db_kyoto.open(PATH_FILE_DB, DB.OCREATE | DB.OREADER)) {
            success_db = false;
            System.err.println("open error: " + db_kyoto.error());
        }

        if (success_db) {
            isExisted = (db_kyoto.check(id) == -1 ? false : true);
            if (!db_kyoto.close()) {
                System.err.println("close db: " + db_kyoto.error());
            }
        }

        return isExisted;
    }

    @Override
    public void InsertLyric(Lyric lyric) {
        // open the database
        if (isExistedLyric(lyric.id)) {
            System.out.println("lyric is existed!!");
            return;
        }

        boolean success_db = true;
        if (!db_kyoto.open(PATH_FILE_DB, DB.OCREATE | DB.OWRITER)) {
            success_db = false;
            System.err.println("open error: " + db_kyoto.error());
        }

        if (success_db) {
            byte[] idBytes = lyric.id.getBytes();
            byte[] dataLyric;
            try {
                dataLyric = SerialAndDeSerial.serialize(lyric);
                if (!db_kyoto.add(idBytes, dataLyric)) {
                    System.err.println("insert lyric db error: " + db_kyoto.error());
                }
            } catch (IOException ex) {
                Logger.getLogger(DBLyricModelKyotoCabinet.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (!db_kyoto.close()) {
                    System.err.println("close db: " + db_kyoto.error());
                }
            }
        }
    }

    @Override
    public List<DataLyric> getDataLyricsById(String id) {
        ArrayList<DataLyric> dataLyrics = new ArrayList<>();
        Lyric lyric = getLyric(id);
        
        if (lyric != null && lyric.datas != null) {
            dataLyrics = (ArrayList<DataLyric>) lyric.datas;
        }
        
        return dataLyrics;
    }

    @Override
    public long getTotalDocumentInDB() {
        boolean success_db = true;
        long count = 0;
        if (!db_kyoto.open(PATH_FILE_DB, DB.OREADER)) {
            success_db = false;
            System.err.println("open error: " + db_kyoto.error());
        }

        if (success_db) {
            count = db_kyoto.count();

            if (!db_kyoto.close()) {
                System.err.println("close db: " + db_kyoto.error());
            }
        }
        return count;
    }

    @Override
    public void removeAllRecords() {
        boolean success_db = true;
        if (!db_kyoto.open(PATH_FILE_DB, DB.OWRITER)) {
            success_db = false;
            System.err.println("open error: " + db_kyoto.error());
        }

        if (success_db) {
            if (!db_kyoto.clear()) {
                System.err.println("clear db: " + db_kyoto.error());
            }
            if (!db_kyoto.close()) {
                System.err.println("close db: " + db_kyoto.error());
            }
        }
    }

    @Override
    public void InsertLyrics(List<Lyric> lyrics) {
        for (Lyric lyric : lyrics) {
            InsertLyric(lyric);
        }
    }

    @Override
    public LyricResult getLyricResult(String id) {
        LyricResult lr = new LyricResult();
        lr.result = -1;
        lr.lyric = null;
        
        Lyric lyric = getLyric(id);
        if(lyric != null){
            lr.result = 0;
            lr.lyric = lyric;
        }
        
        return lr;
    }

    @Override
    public Lyric getLyric(String id) {
        Lyric lyric = null;
        // open the database
        boolean success_db = true;
        if (!db_kyoto.open(PATH_FILE_DB, DB.OREADER)) {
            success_db = false;
            System.err.println("open error: " + db_kyoto.error());
        }

        if (success_db) {
            byte[] idBytes = id.getBytes();
            byte[] dataLyric = db_kyoto.get(idBytes);
            lyric = (Lyric) SerialAndDeSerial.deserialize(dataLyric);
            if (!db_kyoto.close()) {
                System.err.println("close db: " + db_kyoto.error());
            }
        }
        return lyric;
    }

    @Override
    public List<Lyric> getAllLyrics() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

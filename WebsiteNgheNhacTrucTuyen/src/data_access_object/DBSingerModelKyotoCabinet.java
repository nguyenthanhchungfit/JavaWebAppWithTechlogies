/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data_access_object;

import Helpers.SerialAndDeSerial;
import contracts.DataServerContract;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import kyotocabinet.DB;
import models.Referencer;
import models.Singer;
import models.SingerResult;
import server_data.DBSingerModel;

/**
 *
 * @author cpu11165-local
 */
public class DBSingerModelKyotoCabinet implements DBSingerModel {

    private static final DB db_kyoto = new DB();
    private static final String PATH_FILE_DB = DataServerContract.PATH_KC_SINGER_DB;

    @Override
    public Singer getSingerInformation(String idSinger) {
        SingerResult sr = this.getSinger(idSinger);
        return sr.singer;
    }

    @Override
    public boolean isExistedSinger(String id) {
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
    public void InsertSinger(Singer singer) {
        // open the database
        if (isExistedSinger(singer.id)) {
            System.out.println("Singer is existed!!");
            return;
        }

        boolean success_db = true;
        if (!db_kyoto.open(PATH_FILE_DB, DB.OCREATE | DB.OWRITER)) {
            success_db = false;
            System.err.println("open error: " + db_kyoto.error());
        }
        if (success_db) {
            byte[] idByte = singer.id.getBytes();
            try {
                byte[] singerByte = SerialAndDeSerial.serialize(singer);
                if (!db_kyoto.add(idByte, singerByte)) {
                    System.err.println("insert singer: " + db_kyoto.error());
                }
            } catch (IOException ex) {
                Logger.getLogger(DBSingerModelKyotoCabinet.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (!db_kyoto.close()) {
                    System.err.println("close db: " + db_kyoto.error());
                }
            }
        }
    }

    @Override
    public void InsertSingers(List<Singer> singers) {
        for (Singer singer : singers) {
            this.InsertSinger(singer);
        }
    }

    @Override
    public boolean isExistedSongInSinger(String idSinger, String idSong) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void insertNewAlbumToAlbumSinger(String idSinger, Referencer newAlbum) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
            System.err.println("open db singer error: " + db_kyoto.error());
        }

        if (success_db) {
            if (!db_kyoto.clear()) {
                System.err.println("clear db singer error: " + db_kyoto.error());
            }
            if (!db_kyoto.close()) {
                System.err.println("close db singer error: " + db_kyoto.error());
            }
        }
    }

    @Override
    public SingerResult getSinger(String idSinger) {
        SingerResult sr = new SingerResult();
        sr.result = -1;
        sr.singer = null;

        boolean success_db = true;
        if (!db_kyoto.open(PATH_FILE_DB, DB.OCREATE | DB.OREADER)) {
            success_db = false;
            System.err.println("open error: " + db_kyoto.error());
        }

        if (success_db == true && !idSinger.isEmpty()) {
            byte[] idBytes = idSinger.getBytes();
            byte[] data = db_kyoto.get(idBytes);
            Singer singer = (Singer) SerialAndDeSerial.deserialize(data);
            if (singer != null) {
                sr.result = 0;
                sr.singer = singer;
            }
            if (!db_kyoto.close()) {
                System.err.println("close db: " + db_kyoto.error());
            }
        }
        return sr;
    }

    @Override
    public List<Singer> getAllSingers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

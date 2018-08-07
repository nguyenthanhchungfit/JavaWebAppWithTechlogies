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
import models.Album;
import models.AlbumResult;
import server_data.DBAlbumModel;

/**
 *
 * @author cpu11165-local
 */
public class DBAlbumModelKyotoCabinet implements DBAlbumModel {

    private static final DB db_kyoto = new DB();
    private static final String PATH_FILE_DB = DataServerContract.PATH_KC_ALBUM_DB;
    
    @Override
    public boolean isExistedAlbum(String id) {
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
    public void InsertAlbum(Album album) {
        // open the database
        if (isExistedAlbum(album.id)) {
            System.out.println("Album is existed!!");
            return;
        }

        boolean success_db = true;
        if (!db_kyoto.open(PATH_FILE_DB, DB.OCREATE | DB.OWRITER)) {
            success_db = false;
            System.err.println("open error: " + db_kyoto.error());
        }
        
        if(success_db){
            byte[] idBytes = album.id.getBytes();
            try {
                byte[] dataAlbum = SerialAndDeSerial.serialize(album);
                if (!db_kyoto.add(idBytes, dataAlbum)) {
                    System.err.println("insert album: " + db_kyoto.error());
                }
            } catch (IOException ex) {
                Logger.getLogger(DBAlbumModelKyotoCabinet.class.getName()).log(Level.SEVERE, null, ex);
            }finally {
                if (!db_kyoto.close()) {
                    System.err.println("close db: " + db_kyoto.error());
                }
            }
            
        }
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
        
        // open the database
        boolean success_db = true;
        if (!db_kyoto.open(PATH_FILE_DB, DB.OREADER)) {
            success_db = false;
            System.err.println("open error: " + db_kyoto.error());
        }
        
        if(success_db == true && !id.isEmpty()){
            byte[] idByte = id.getBytes();
            byte[] dataAlbum = db_kyoto.get(idByte);
            album = (Album) SerialAndDeSerial.deserialize(dataAlbum);
            
            if (!db_kyoto.close()) {
                System.err.println("close db: " + db_kyoto.error());
            }
        }
        
        return album;
    }

    @Override
    public List<Album> getAllAlbums() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

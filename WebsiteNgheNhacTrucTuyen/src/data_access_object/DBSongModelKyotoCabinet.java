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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kyotocabinet.Cursor;
import kyotocabinet.DB;
import models.Song;
import models.SongResult;
import server_data.DBSongModel;

/**
 *
 * @author cpu11165-local
 */
public class DBSongModelKyotoCabinet implements DBSongModel {

    private static final DB db_kyoto = new DB();
    private static final String PATH_FILE_DB = DataServerContract.PATH_KC_SONG_DB;

    @Override
    public SongResult getSongById(String id) {

        // open the database
        boolean success_db = true;
        if (!db_kyoto.open(PATH_FILE_DB, DB.OREADER)) {
            success_db = false;
            System.err.println("open error: " + db_kyoto.error());
        }

        SongResult sr = new SongResult();
        sr.result = -1;
        sr.song = null;

        if (success_db == true && !id.isEmpty()) {
            byte[] idBytes = id.getBytes();
            byte[] dataSong = db_kyoto.get(idBytes);
            Song song = (Song) SerialAndDeSerial.deserialize(dataSong);
            if (song != null) {
                sr.result = 0;
                sr.song = song;
            }
            if (!db_kyoto.close()) {
                System.err.println("close db: " + db_kyoto.error());
            }
        }
        return sr;
    }

    @Override
    public void getNameInsensitive(String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isExistedSong(String id) {
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
    public void InsertSong(Song song) {
        // open the database
        if (isExistedSong(song.id)) {
            System.out.println("Song is existed!!");
            return;
        }

        boolean success_db = true;
        if (!db_kyoto.open(PATH_FILE_DB, DB.OCREATE | DB.OWRITER)) {
            success_db = false;
            System.err.println("open error: " + db_kyoto.error());
        }

        byte[] idBytes = song.id.getBytes();
        if (success_db) {
            try {
                byte[] song_encode = SerialAndDeSerial.serialize(song);
                if (!db_kyoto.add(idBytes, song_encode)) {
                    System.err.println("insert song: " + db_kyoto.error());
                }
            } catch (IOException ex) {
                Logger.getLogger(DBSongModelKyotoCabinet.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (!db_kyoto.close()) {
                    System.err.println("close db: " + db_kyoto.error());
                }
            }
        }

    }

    @Override
    public void InsertSongs(List<Song> songs) {
        for (Song song : songs) {
            this.InsertSong(song);
        }
    }

    @Override
    public boolean isExistedAlbumInSong(String idSong, String idAlbum) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Song> getSongsSearchAPIByName(String name) {
        ArrayList<Song> songs = new ArrayList<>();
        // open the database
        boolean success_db = true;
        if (!db_kyoto.open(PATH_FILE_DB, DB.OREADER)) {
            success_db = false;
            System.err.println("open error: " + db_kyoto.error());
        }

        if (success_db) {
            
             // traverse records
            Cursor cur = db_kyoto.cursor();
            cur.jump();
            byte[][] recv;
            while((recv = cur.get(true)) != null){
                Song song = (Song) SerialAndDeSerial.deserialize(recv[1]);
                Pattern ptn = Pattern.compile(name, Pattern.CASE_INSENSITIVE);
                Matcher mtch = ptn.matcher(song.name);
                if(mtch.find()){
                    songs.add(song);
                }
            }
            // close the database
            if (!db_kyoto.close()) {
                System.err.println("close error: " + db_kyoto.error());
            }
            cur.disable();
        }

        return songs;
    }

    @Override
    public List<Song> getAllSongs() {
        ArrayList<Song> songs = new ArrayList<>();
        // open the database
        boolean success_db = true;
        if (!db_kyoto.open(PATH_FILE_DB, DB.OREADER)) {
            success_db = false;
            System.err.println("open error: " + db_kyoto.error());
        }

        if (success_db) {
             // traverse records
            Cursor cur = db_kyoto.cursor();
            cur.jump();
            byte[][] recv;
            while((recv = cur.get(true)) != null){
                Song song = (Song) SerialAndDeSerial.deserialize(recv[1]);
                songs.add(song);
            }
            // close the database
            if (!db_kyoto.close()) {
                System.err.println("close error: " + db_kyoto.error());
            }
            cur.disable();
        }

        return songs;
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

}

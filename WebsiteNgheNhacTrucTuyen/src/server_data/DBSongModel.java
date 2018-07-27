/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_data;

import java.util.List;

import models.Song;
import models.SongResult;


/**
 *
 * @author Nguyen Thanh Chung
 */

public interface DBSongModel {

    
    public SongResult getSongById(String id);

    public void getNameInsensitive(String name);

    public boolean isExistedSong(String id);

    public void InsertSong(Song song);

    public void InsertSongs(List<Song> songs);

    // Kiểm tra album đã tồn tại trong song chưa
    public boolean isExistedAlbumInSong(String idSong, String idAlbum);

    public List<Song> getSongsSearchAPIByName(String name);

    public List<Song> getAllSongs();

    public long getTotalDocumentInDB();
    
    public void removeAllRecords();
}

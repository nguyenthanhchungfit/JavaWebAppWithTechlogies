/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_data;


import java.util.List;
import models.Referencer;
import models.Singer;
import models.SingerResult;

/**
 *
 * @author Nguyen Thanh Chung
 */
public interface DBSingerModel {

    public SingerResult getSinger(String idSinger);

    // Lấy thông tin ca sỹ 
    public Singer getSingerInformation(String idSinger);

    // Kiểm tra sự tồn tại của Singer
    public boolean isExistedSinger(String id);

    // Chèn đối tượng singer vào DB
    public void InsertSinger(Singer singer);

    // Chèn nhiều đối tượng vào singer
    public void InsertSingers(List<Singer> list);

    public boolean isExistedSongInSinger(String idSinger, String idSong);

    public void insertNewAlbumToAlbumSinger(String idSinger, Referencer newAlbum);
    
    public List<Singer> getAllSingers();
    
    public long getTotalDocumentInDB();
    
    public void removeAllRecords();

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_data;

import java.util.List;
import models.Album;
import models.AlbumResult;


/**
 *
 * @author cpu11165-local
 */
public interface DBAlbumModel {

    public boolean isExistedAlbum(String id);

    public void InsertAlbum(Album album);
    
    public void InsertAlbums(List<Album> albums);
    
    public AlbumResult getAlbumResult(String id);
    
    public Album getAlbum(String id);
    
    public List<Album> getAllAlbums();
    
    public long getTotalDocumentInDB();
    
    public void removeAllRecords();
}

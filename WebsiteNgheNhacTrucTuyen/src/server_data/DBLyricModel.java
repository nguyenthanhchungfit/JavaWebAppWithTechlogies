/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_data;

import java.util.ArrayList;
import java.util.List;
import models.DataLyric;
import models.Lyric;
import models.LyricResult;


/**
 *
 * @author cpu11165-local
 */
public interface DBLyricModel {

    public boolean isExistedLyric(String id);

    public void InsertLyric(Lyric lyric);
    
    public void InsertLyrics(List<Lyric> lyrics);

    public List<DataLyric> getDataLyricsById(String id);
    
    public long getTotalDocumentInDB();
    
    public void removeAllRecords();
    
    public LyricResult getLyricResult(String id);
    
    public Lyric getLyric(String id);
    
    public List<Lyric> getAllLyrics();
    
}

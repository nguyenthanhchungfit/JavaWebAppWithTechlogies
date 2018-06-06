/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data_server;

import java.util.List;
import models.ServicesDataCenter;
import models.Singer;
import models.SingerResult;
import models.Song;
import models.SongResult;
import org.apache.thrift.TException;

/**
 *
 * @author Nguyen Thanh Chung
 */
public class ServiceHandlers implements ServicesDataCenter.Iface {

    @Override
    public SongResult getSongData(String name_song) throws TException {
        return SongDB.getSongByName(name_song);
    }

    @Override
    public List<Song> getSongsData(String name_song) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SingerResult getSingerData(String id_singer) throws TException {
        Singer singer= SingerDB.getSingerInformation(id_singer);
        SingerResult sr = new SingerResult();
        if(singer == null){
            sr.result = 1;
        }else{
            sr.result = 0;
            sr.singer = singer;
        }
        return sr;
    }

    @Override
    public String getLyric(String id, int page) throws TException {
        return SongDB.getLyric(id, page);
    } 
    
}

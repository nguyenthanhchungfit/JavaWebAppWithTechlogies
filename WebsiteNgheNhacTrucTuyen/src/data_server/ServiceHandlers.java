/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data_server;

import java.util.List;
import models.ServicesDataCenter;
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
    public List<Song> getSongsDataByCatagory(String category) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

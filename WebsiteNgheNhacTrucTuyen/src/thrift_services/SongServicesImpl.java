/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thrift_services;

import data_server.DBSongModel;
import java.util.List;
import models.Song;
import models.SongResult;
import org.apache.thrift.TException;

/**
 *
 * @author cpu11165-local
 */
public class SongServicesImpl implements SongServices.Iface{



    @Override
    public SongResult getSongById(String id) throws TException {
        return DBSongModel.getSongById(id);
    }

    @Override
    public List<Song> getSongsSearchAPIByName(String name) throws TException {
        return DBSongModel.getSongsSearchAPIByName(name);
    }
    
}

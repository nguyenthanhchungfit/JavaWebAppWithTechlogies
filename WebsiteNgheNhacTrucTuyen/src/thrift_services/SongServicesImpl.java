/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thrift_services;

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
    public List<Song> getSongsByName(String name) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SongResult getSongById(String id) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

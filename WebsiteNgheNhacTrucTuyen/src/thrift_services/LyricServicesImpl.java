/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thrift_services;

import data_server.DBLyricModel;
import java.util.List;
import models.DataLyric;
import models.LyricResult;
import org.apache.thrift.TException;

/**
 *
 * @author cpu11165-local
 */
public class LyricServicesImpl implements LyricServices.Iface{

    @Override
    public LyricResult getLyricByIdAndPage(String id, String page) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DataLyric> getDataLyricsById(String id) throws TException {
        return DBLyricModel.getDataLyricsById(id);
    }
    
}

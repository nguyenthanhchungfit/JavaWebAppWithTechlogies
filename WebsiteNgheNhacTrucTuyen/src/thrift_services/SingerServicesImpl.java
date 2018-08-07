/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thrift_services;

import data_access_object.DBSingerModelMongo;
import server_data.DBSingerModel;
import java.util.List;
import models.Singer;
import models.SingerResult;
import org.apache.thrift.TException;

/**
 *
 * @author cpu11165-local
 */
public class SingerServicesImpl implements SingerServices.Iface {

    private DBSingerModel dbSingerModel = new DBSingerModelMongo();
    
    @Override
    public List<Singer> getSingersByName(String name) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SingerResult getSingerById(String id) throws TException {
        SingerResult sr = new SingerResult();
        Singer singer = dbSingerModel.getSingerInformation(id);
        if(singer == null){
            sr.result = -1;
        }else{
            sr.result = 0;
            sr.singer = singer;
        }
        return sr;
    }

    @Override
    public long getTotalNumberSingers() throws TException {
        return dbSingerModel.getTotalDocumentInDB();
    }
    
}

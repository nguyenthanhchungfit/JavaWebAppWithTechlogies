/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thrift_services;

import data_access_object.DBAlbumModelKyotoCabinet;
import data_access_object.DBAlbumModelMongo;
import models.AlbumResult;
import org.apache.thrift.TException;
import server_data.DBAlbumModel;

/**
 *
 * @author cpu11165-local
 */
public class AlbumServicesImpl implements AlbumServices.Iface{

    private static final  DBAlbumModel dbAlbumModel = new DBAlbumModelMongo();
    //private static final  DBAlbumModel dbAlbumModel = new DBAlbumModelKyotoCabinet();
    
    @Override
    public AlbumResult getAlbumById(String id) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long getTotalNumberAlbums() throws TException {
        return dbAlbumModel.getTotalDocumentInDB();
    }
    
}

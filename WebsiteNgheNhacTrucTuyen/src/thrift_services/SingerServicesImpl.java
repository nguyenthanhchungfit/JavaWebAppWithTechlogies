/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thrift_services;

import java.util.List;
import models.Singer;
import models.SingerResult;
import org.apache.thrift.TException;

/**
 *
 * @author cpu11165-local
 */
public class SingerServicesImpl implements SingerServices.Iface {

    @Override
    public List<Singer> getSingersByName(String name) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SingerResult getSingerById(String id) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

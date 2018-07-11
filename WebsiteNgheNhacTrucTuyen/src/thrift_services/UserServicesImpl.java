/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thrift_services;

import models.Customer;
import org.apache.thrift.TException;

/**
 *
 * @author cpu11165-local
 */
public class UserServicesImpl implements UserServices.Iface{

    @Override
    public boolean signup(Customer customer) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String login(String username, String password) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

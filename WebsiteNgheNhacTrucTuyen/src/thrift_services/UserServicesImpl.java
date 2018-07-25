/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thrift_services;

import Helpers.EncryptAndDecrypt;
import cache_data.DataCacher;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Customer;
import models.Session;
import org.apache.thrift.TException;
import server_user.DBAdminModel;
import server_user.DBCustomerModel;

/**
 *
 * @author cpu11165-local
 */
public class UserServicesImpl implements UserServices.Iface{

    private DBAdminModel dbAdmin = new DBAdminModel();
    private DBCustomerModel dbCustomer = new DBCustomerModel();
    private EncryptAndDecrypt encryptAndDecrypt = new EncryptAndDecrypt();
    private DataCacher dataCacher = DataCacher.getInstance();
    
    @Override
    public boolean signup(Customer customer) throws TException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String login(String username, String password) throws TException {
        String c_user = "";
        Session session = new Session();
        boolean flag = false;
        try {
            if(dbCustomer.isValidAccount(username, password) == 1){
                session.setType(1);
                flag = true;
            }else if(dbAdmin.isValidAccount(username, password) == 0){
                session.setType(0);
                flag = true;
            }
            if(flag){
                boolean isAdmin = (session.getType() == 0);
                c_user = encryptAndDecrypt.createUserID(username, isAdmin);
                session.setSessionID(c_user);
                session.setUsername(username);
                dataCacher.insertNewSession(session);
            }   
        } catch (GeneralSecurityException ex) {
            Logger.getLogger(UserServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UserServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return c_user;
    }

    @Override
    public long getTotalNumberUsers() throws TException {
        return dbCustomer.getTotalDocumentInDB();
    }
    
}

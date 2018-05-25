/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data_center;

import org.apache.thrift.TException;
import user_data.CommunicatedDataCenter;
import user_data.User;
import user_data.UserResult;
import user_data.UsersManager;

/**
 *
 * @author cpu11165-local
 */
public class ServiceHandlers implements CommunicatedDataCenter.Iface{

    private static UsersManager manager;
    
    static{
        manager = new UsersManager();
        manager.addNewUser(new User("ntchung", "123", "Nguyen Thanh Chung",
                "1997-06-19", "male", "01629009486", "chungnt@vng.com.vn", "TPHCM"));
        manager.addNewUser(new User("ntc", "123", "Nguyen Thanh Cong",
                "1992-03-08", "male", "0905675678", "congnt@vng.com.vn", "TPHCM"));
        manager.addNewUser(new User("ntchu", "123", "Nguyen Thanh Chu",
                "1994-05-25", "female", "01669977622", "chunt@vng.com.vn", "TPHCM"));
    }
    
    @Override
    public boolean isExistedUser(String username) throws TException {
        return manager.isExistedUser(username);
    }

    @Override
    public boolean isExistedPhone(String phone) throws TException {
        return manager.isExistedPhone(phone);
    }

    @Override
    public boolean isValidAccount(String username, String password) throws TException {
        return manager.isValidAccount(username, password);
    }

    @Override
    public boolean isExistedEmail(String email) throws TException {
        return manager.isExistedEmail(email);
    }
      
    @Override
    public boolean addNewUser(User user) throws TException {
        return manager.addNewUser(user);
    }

    @Override
    public UserResult getInformationUser(String username, String password) throws TException {
        return manager.getInformationUser(username, password);
    }
}

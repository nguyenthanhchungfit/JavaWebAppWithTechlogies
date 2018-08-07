/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mongo;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import models.Customer;
import server_user.DBCustomerModel;

/**
 *
 * @author cpu11165-local
 */
public class DBCustomerTest {
    public static void main(String[] args) throws java.text.ParseException, GeneralSecurityException, GeneralSecurityException, UnsupportedEncodingException {
        
        DBCustomerModel db = new DBCustomerModel();
        
        Customer newCustomer = new Customer();
        newCustomer.username = "thanhchungVNG";
        newCustomer.password = "123456";
        newCustomer.fullname = "Nguyễn Thành Chung";
        newCustomer.address = "P2, Q8, TPHCM";
        
        newCustomer.dob = "19-06-1997";
        newCustomer.sex = true;
        newCustomer.email = "nguyenthanhchungfit@gmail.com";
        
        System.out.println(newCustomer);
        
        
        db.insertNewCustomer(newCustomer);
        
    }
}

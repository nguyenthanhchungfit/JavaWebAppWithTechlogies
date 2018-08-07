/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mongo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import server_user.DBAdminModel;

/**
 *
 * @author cpu11165-local
 */
public class DBAdminTest {
    public static void main(String[] args) throws GeneralSecurityException, UnsupportedEncodingException, IOException {
        DBAdminModel db = new DBAdminModel();
        String username = "nguyenthanhchung";
        String password = "123456";
        db.insertNewAdmin(username, password);
    }
}

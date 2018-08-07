/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encrypt_decrypt;

import Helpers.EncryptAndDecrypt;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

/**
 *
 * @author cpu11165-local
 */
public class EncryptAndDecryptTest {
    
    public static void main(String[] args) throws GeneralSecurityException, UnsupportedEncodingException, IOException {
        EncryptAndDecrypt encryptAndDecrypt = new EncryptAndDecrypt();
        String originPassword = "adminaasdasda";
        String encryptPassword = encryptAndDecrypt.createUserID(originPassword, true);
        
        System.out.println(encryptPassword);
        System.out.println(encryptPassword.length());
        //System.out.println(encryptAndDecrypt.isValidPassword(encryptPassword, "THANHCHUNG"));
    }
}

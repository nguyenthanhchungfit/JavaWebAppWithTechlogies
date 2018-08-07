/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author cpu11165-local
 */
public class EncryptAndDecrypt {
    
    private static final String PASSWORD = "nguyenthanhchung";
    
    private static final byte[] SALT = ("myappmp3").getBytes();
    
    // Decreasing this speeds down startup time and can be useful during testing, but it also makes it easier for brute force attackers
    private static final int ITERATION_COUNT = 40000;
    
    // Other values give me java.security.InvalidKeyException: Illegal key size or default parameters
    private static final int KEY_LENGTH = 128;
    
    //SecretKeySpec key = createSecretKey();
    
    private SecretKeySpec createSecretKey(char[] password, byte[] salt, int iterationCount, int keyLength) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterationCount, keyLength);
        SecretKey keyTmp = keyFactory.generateSecret(keySpec);
        return new SecretKeySpec(keyTmp.getEncoded(), "AES");
    }
    
    public String encrypt(String property) throws GeneralSecurityException, UnsupportedEncodingException {
        SecretKeySpec key = createSecretKey(PASSWORD.toCharArray(), SALT, ITERATION_COUNT, KEY_LENGTH);
      
        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        pbeCipher.init(Cipher.ENCRYPT_MODE, key);
        AlgorithmParameters parameters = pbeCipher.getParameters();
        IvParameterSpec ivParameterSpec = parameters.getParameterSpec(IvParameterSpec.class);
        byte[] cryptoText = pbeCipher.doFinal(property.getBytes("UTF-8"));
        byte[] iv = ivParameterSpec.getIV();
        return base64Encode(iv) + ":" + base64Encode(cryptoText);
    }

    private  String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    private String decrypt(String string) throws GeneralSecurityException, IOException {
        SecretKeySpec key = createSecretKey(PASSWORD.toCharArray(), SALT, ITERATION_COUNT, KEY_LENGTH);
        String iv = string.split(":")[0];
        String property = string.split(":")[1];
        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        pbeCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(base64Decode(iv)));
        return new String(pbeCipher.doFinal(base64Decode(property)), "UTF-8");
    }

    private static byte[] base64Decode(String property) throws IOException {
        return Base64.getDecoder().decode(property);
    }
    
    public boolean isValidPassword(String encryptPassword, String password) throws GeneralSecurityException, IOException{
        String decryptPassword = this.decrypt(encryptPassword);
        return decryptPassword.equals(password);
    }
    
    public String createUserID(String username, boolean isAdmin) throws GeneralSecurityException, UnsupportedEncodingException{
        String pre_id = "";
        if(isAdmin){
            pre_id = this.encrypt("admin");
        }else{
            pre_id= this.encrypt("customer");
        }       
        return pre_id + username;
    }
            
}

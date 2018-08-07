/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cpu11165-local
 */
public class SerialAndDeSerial {
    public static Object deserialize(byte[] encode){
        if(encode == null) return null;
        Object obj = null;
        ObjectInputStream ois = null;
        try {
            ByteArrayInputStream bas = new ByteArrayInputStream(encode);
            ois = new ObjectInputStream(bas);
            obj = ois.readObject();
        } catch (IOException ex) {
            Logger.getLogger(SerialAndDeSerial.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SerialAndDeSerial.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                ois.close();
            } catch (IOException ex) {
                Logger.getLogger(SerialAndDeSerial.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return obj;
    }
    
    public static byte[] serialize(Object obj) throws IOException{
        byte[] encode = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            encode = bos.toByteArray();
            
        } catch (IOException ex) {
            Logger.getLogger(SerialAndDeSerial.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            oos.close();
        }
        return encode;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kcn;
import kyotocabinet.DB;

/**
 *
 * @author cpu11165-local
 */
public class KCNTestStorage {
    public static void main(String[] args) {
        // create the object
        DB db = new DB();
        

        // open the database
        if (!db.open("data.kch", DB.OWRITER | DB.OCREATE)) {
            System.err.println("open error: " + db.error());
        }
        String value = "Nguyễn Thành Chung - Zalo Developer Backend"; 
        
        for(int i = 0; i < 1000000; i++){
            String key = i + "";
            db.add(key, value);
        }
        
         // close the database
        if (!db.close()) {
            System.err.println("close error: " + db.error());
        }
    }
}

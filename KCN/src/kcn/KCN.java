/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kcn;

import kyotocabinet.Cursor;
import kyotocabinet.DB;

/**
 *
 * @author cpu11165-local
 */
public class KCN {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        // create the object
        DB db = new DB();
        

        // open the database
        if (!db.open("casket.kch", DB.OWRITER | DB.OCREATE)) {
            System.err.println("open error: " + db.error());
        }

        // store records
        if (!db.set("foo", "hop")
                || !db.set("bar", "step")
                || !db.set("baz", "jump")) {
            System.err.println("set error: " + db.error());
        }

        // retrieve records
        String value = db.get("foo");
        if (value != null) {
            System.out.println(value);
        } else {
            System.err.println("set error: " + db.error());
        }

        // traverse records
        Cursor cur = db.cursor();
        cur.jump();
        String[] rec;
        byte[][] recb;
        while ((recb = cur.get(true)) != null) {
            System.out.println(new String(recb[0]) + ":" + new String(recb[1]));
        }
        cur.disable();

        // close the database
        if (!db.close()) {
            System.err.println("close error: " + db.error());
        }
        
        
        if (!db.open("casket.kch", DB.OWRITER | DB.OCREATE)) {
            System.err.println("open error: " + db.error());
        }
        String value1 = db.get("foo");
        System.out.println(value1);
    }

}

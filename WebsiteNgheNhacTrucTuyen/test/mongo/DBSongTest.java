/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mongo;

import data_access_object.DBSongModelMongo;


/**
 *
 * @author cpu11165-local
 */
public class DBSongTest {
    public static void main(String[] args){
        long number = new DBSongModelMongo().getTotalDocumentInDB();
        System.out.println(number);
    }
}

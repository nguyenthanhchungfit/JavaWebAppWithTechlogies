/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mongo;

import data_server.DBSingerModel;
import models.Referencer;

/**
 *
 * @author cpu11165-local
 */
public class DBSingerTest {
    public static void main(String[] args) {
        DBSingerModel.insertNewAlbumToAlbumSinger("IWZFE0CD", new Referencer("aa", "bb"));
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mongo;

import server_data.DBSongModel;
import java.util.ArrayList;
import models.Song;

/**
 *
 * @author cpu11165-local
 */
public class DBSongTest {
    public static void main(String[] args){
        ArrayList<Song> listSongs =  (ArrayList<Song>) DBSongModel.getAllSongs();
        int i = 1;
        for(Song song : listSongs){
            System.out.println("******************** : " + i);
            System.out.println(song);
            System.out.println();
            i++;
        }
    }
}

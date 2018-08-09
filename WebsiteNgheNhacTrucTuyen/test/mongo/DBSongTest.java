/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mongo;

import data_access_object.DBSongModelMongo;
import java.util.ArrayList;
import models.Song;


/**
 *
 * @author cpu11165-local
 */
public class DBSongTest {
    public static void main(String[] args){
        ArrayList<Song> arr = (ArrayList<Song>) new DBSongModelMongo().getSongsSearchAPIByName("Anh");
        //ArrayList<Song> arr = (ArrayList<Song>) new DBSongModelMongo().getAllSongs();
        System.out.println(arr.size());
        for(Song song : arr){
            System.out.println(song.name);
        }
    }
}

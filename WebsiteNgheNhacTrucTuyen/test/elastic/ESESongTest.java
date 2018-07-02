/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elastic;

import Helpers.FormatJson;
import Helpers.FormatPureString;
import data_server.DBSongModel;
import elastic_search_engine.ESESong;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import models.Song;
import org.json.simple.parser.ParseException;

/**
 *
 * @author cpu11165-local
 */
public class ESESongTest {

    public static void main(String[] args) throws IOException, ParseException{
        
        ESESong esSong = new ESESong();
        
        ArrayList<Song> songs = (ArrayList<Song>) esSong.getSongsSearchByName("Th");
        
        for(Song song : songs){
            System.out.println(song);
        }
        
        
    }
}

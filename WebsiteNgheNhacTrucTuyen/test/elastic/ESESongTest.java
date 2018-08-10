/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elastic;

import elastic_search_engine.ESESong;
import java.io.IOException;
import java.util.ArrayList;
import models.Song;
import org.json.simple.parser.ParseException;

/**
 *
 * @author cpu11165-local
 */
public class ESESongTest {

    public static void main(String[] args) throws IOException, ParseException{
        
        ESESong esSong = new ESESong();
        
//        ArrayList<Song> songs = (ArrayList<Song>) esSong.getSongsSearchByName("Xa");
//        
//        for(Song song : songs){
//            System.out.println(song);
//        }
        esSong.deleteSong("Ej_wImUBTiEO63_kNG2F");
        esSong.deleteSong("Qj_wImUBTiEO63_khG35");
        esSong.deleteSong("RT_wImUBTiEO63_kiW2_");
        esSong.deleteSong("dT_wImUBTiEO63_k5G2W");
        esSong.deleteSong("hD_wImUBTiEO63_k_m2L");
        esSong.deleteSong("nD_xImUBTiEO63_kLG3M");
        
        //esSong.deleteSong("Hj_wImUBTiEO63_kRW2lr");
        //esSong.deleteSong("OT_wImUBTiEO63_kdW31");
        
    }
}

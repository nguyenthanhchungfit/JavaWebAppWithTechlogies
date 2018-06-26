/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elastic;

import elastic_search_engine.ESESongInsert;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author cpu11165-local
 */
public class ESESongTest {
    @SuppressWarnings("empty-statement")
    public static void main(String[] args){
        String id = "123";
        String name = "Nỗi đau xót xa";
        
        ArrayList<String> singers = new ArrayList<>();
        singers.add("Minh Vương M4U");
        singers.add("Thùy Chi");
        new ESESongInsert().InsertNewSong(id, name, singers);
    }
}

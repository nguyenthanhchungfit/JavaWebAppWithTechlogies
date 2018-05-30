/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlerzingmp3;

import java.util.ArrayList;

/**
 *
 * @author cpu11165-local
 */
public class DataCrawler {
    public String id;
    public String linkSong;
    public String songName;
    public String albumName;
    public ArrayList<Singer> singers;
    public ArrayList<String> lyrics;
    public ArrayList<String> kinds;
    public ArrayList<String> composers;

    
    public DataCrawler(){
        this.singers = new ArrayList<>();
        this.lyrics = new ArrayList<>();
        this.kinds = new ArrayList<>();
        this.composers = new ArrayList<>();
    }
    


    @Override
    public String toString() {
        return id;
    }
    
    
}

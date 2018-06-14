/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlerdatamp3zing;

/**
 *
 * @author cpu11165-local
 */
public class Song {
    public String name= null;
    public String cover= null;
    public String artist = null;
    public String sourceList = null; // link nghe nhac 128kbps;
    public String lyric = null;
    public String [] apiLink = null; // chua link tai 128kbps =>[0] va 320kbps =>[1]
    public Lyric  objLyric;
    public String toString(){
        
        String apis = "";
        
	String out =  "Name: "+ this.name+"\n";
        out +=  "cover: "+ this.cover+"\n";
        out +=  "artist: "+ this.artist+"\n";
        out +=  "sourceList: "+ this.sourceList+"\n";
//        for(String api : apiLink){
//            apis += api + "\n";
//        }
//        out += "api: " + apis;
        out +=  "lyric: "+ this.lyric+"\n";
	return out;
    }
}

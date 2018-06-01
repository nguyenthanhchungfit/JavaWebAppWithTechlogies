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
public class SongModel {
    private String id;
    private String name;
    private String album;
    private ArrayList<String> kinds;
    private ArrayList<String> lyrics;
    private ArrayList<SingerModel> singers;
    private ArrayList<String> composers;

    public SongModel() {
        kinds = new ArrayList<>();
        lyrics = new ArrayList<>();
        singers = new ArrayList<>();
        composers = new ArrayList<>();
    }

    
    
    public SongModel(String id, String name, String album, ArrayList<String> kinds, ArrayList<String> lyrics, ArrayList<SingerModel> singers, ArrayList<String> composers) {
        this.id = id;
        this.name = name;
        this.album = album;
        this.kinds = kinds;
        this.lyrics = lyrics;
        this.singers = singers;
        this.composers = composers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public ArrayList<String> getKinds() {
        return kinds;
    }

    public void setKinds(ArrayList<String> kinds) {
        this.kinds = kinds;
    }

    public ArrayList<String> getLyrics() {
        return lyrics;
    }

    public void setLyrics(ArrayList<String> lyrics) {
        this.lyrics = lyrics;
    }

    public ArrayList<SingerModel> getSingers() {
        return singers;
    }

    public void setSingers(ArrayList<SingerModel> singers) {
        this.singers = singers;
    }

    public ArrayList<String> getComposers() {
        return composers;
    }

    public void setComposers(ArrayList<String> composers) {
        this.composers = composers;
    }

    @Override
    public String toString() {
        return "SongModel{" + "id=" + id + "\nname=" + name + "\nalbum=" + album 
                + "\nkinds=" + kinds + "\nlyrics=" + lyrics + "\nsingers=" + singers 
                + "\ncomposers=" + composers + '}';
    }
    
    
}

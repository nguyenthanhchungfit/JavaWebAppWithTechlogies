/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawler_data;


import data_server.SingerDB;
import data_server.SongDB;
import java.io.IOException;
import java.util.ArrayList;
import models.Singer;
import models.Song;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author cpu11165-local
 */
public class ZingMP3Crawler {

    
    public void crawl(String songName) throws IOException{
        String eSong = songName.trim();
        eSong = eSong.replace(" ", "+");
        String query = "https://mp3.zing.vn/tim-kiem/bai-hat.html?q=" + eSong;
        
        Document doc = Jsoup.connect(query).get();
        Elements item_songs = doc.getElementsByClass("item-song");
        System.out.println(item_songs.size());
        for(Element ele : item_songs){
            String id = ele.attr("id");
            System.out.println(id);
            Element titleE = ele.select("a").first();
            String urlSong = titleE.attr("href");
            String title = titleE.attr("title");
            title = title.substring(0, title.indexOf(" -"));
            urlSong = "https://mp3.zing.vn" + urlSong;
            System.out.println(title);
            System.out.println(urlSong);

            Song song = new Song();
            initSong(song);
         
            song.setId(id);
            song.setName(title);
            crawlSong(urlSong, song);
            System.out.println("\n\n****************\n\n");
            
        }
    }
    
    private void crawlSong(String url, Song song) throws IOException{
        if(song == null) return;
        
        Document docSong = Jsoup.connect(url).
                    header("Accept-Encoding", "gzip, deflate")
                    .userAgent("Mozilla/5.0(X11; Ubuntu; Linu...)Gecko/20100101 Firefox/60.0")
                    .maxBodySize(0)
                    .ignoreHttpErrors(true)
                    .timeout(600000)
                    .get();

        Element songInfor = docSong.select("div.info-top-play").first();
            if(songInfor != null){
                Elements singersI = songInfor.child(0).getElementsByTag("h2");
                for(Element ele : singersI){
                    Singer singer = new Singer();
                    //System.out.println(ele.attr("data-id"));
                    singer.setId(ele.attr("data-id"));
                    String href = ele.child(0).attr("href");
                    singer.setName(ele.text());
                    //Craw data singer
                    //------------------------------------
                    String linkProfile = "https://mp3.zing.vn" + href + "/tieu-su";
                    crawlSinger(linkProfile, singer);
                    
                    song.getSingers().add(singer);
                    System.out.println("****Singer");
                    System.out.println(singer);
                    if(!SingerDB.isExistedSinger(singer.getId())){
                        System.out.println("=>>>>> SingerOK!");
                        SingerDB.InsertSinger(singer);
                    }
                }
            }
            
            if(songInfor != null){
                Elements composersI = songInfor.child(1).getElementsByTag("div");
                for(Element ele : composersI){
                    if(ele.hasAttr("id")){
                        if(ele.attr("id").equals("composer-container")){
                            String composer = ele.child(0).text();
                            song.getComposers().add(composer);
                        //System.out.println(composer);
                        }
                    }
                }
            }

            if(songInfor != null){
                Elements kindsI = songInfor.child(2).getElementsByTag("a");
                for(Element ele : kindsI){
                    String kind = ele.text();
                    song.getKinds().add(kind);
                }
            }
            
        
            
            
            Element lyricE = docSong.select("div#lyrics-song").first();
            if(lyricE != null){
                Elements lyricsE = lyricE.child(0).child(0).child(0).children();
                for(Element ele : lyricsE){
                    Element content = ele.getElementsByTag("p").first();
                    song.getLyrics().add(content.text());
                }
            }
            
            System.out.println("*****Song:");
            System.out.println(song);
            if(!SongDB.isExistedSong(song.getId())){
                SongDB.InsertSong(song);
            }
        
    }
    
    private void crawlSinger(String url, Singer singer) throws IOException{
        if(singer == null) return;
        Document docSinger = Jsoup.connect(url).
                            header("Accept-Encoding", "gzip, deflate")
                            .userAgent("Mozilla/5.0(X11; Ubuntu; Linu...)Gecko/20100101 Firefox/60.0")
                            .maxBodySize(0)
                            .timeout(600000)
                            .ignoreHttpErrors(true)
                            .get();
                    
        Element infor = docSinger.select("div.entry").first();
        if(infor!=null){
            Elements liE = infor.child(0).children();
            String realName = liE.get(0).text();
            String dob = liE.get(1).text();
            String country = liE.get(2).text();
            singer.setRealname(realName.substring(realName.indexOf(':') + 1));
            singer.setDob(dob.substring(dob.indexOf(':')+1));
            singer.setCountry(country.substring(country.indexOf(':') + 1));
                
            String description = infor.text();
            String sdescription = description.substring(realName.length() + dob.length()
                                          +  country.length() + 3);
            singer.setDescription(sdescription);        
        }
    }
    
    private void initSong(Song song){
        song.album = "";
        song.singers = new ArrayList<>();
        song.composers = new ArrayList<>();
        song.lyrics = new ArrayList<>();
        song.kinds = new ArrayList<>();
    }
}
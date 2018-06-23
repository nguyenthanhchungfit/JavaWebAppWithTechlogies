/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawler_data;


import data_server.DBAlbumModel;
import data_server.DBLyricModel;
import data_server.DBSingerModel;
import data_server.DBSongModel;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import models.Album;
import models.DataLyric;
import models.Kind;
import models.Lyric;
import models.ModelInitiation;
import models.Referencer;
import models.Singer;
import models.Song;
import models.Video;
import org.json.simple.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author cpu11165-local
 */
public class ZingMP3Crawler{

    
    public void crawl(String songName) throws IOException, ParseException{
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
            ModelInitiation.initSong(song);
         
            song.setId(id);
            song.setName(title);
            //crawlSong(urlSong, song);
            System.out.println("\n\n****************\n\n");
            
        }
    }
    
    public void crawlSongByUrl(String url) throws IOException, ParseException{
        
        Song song = new Song();
        List<Singer> singers = new ArrayList<>();
        List<Kind> kinds = new ArrayList<>();
        Album album = new Album();
        Lyric lyric = new Lyric();
        ModelInitiation.initSong(song);
        ModelInitiation.initAlbum(album);
        ModelInitiation.initLyric(lyric);
        
        
        
        Document docSong = Jsoup.connect(url).
                    header("Accept-Encoding", "gzip, deflate")
                    .userAgent("Mozilla/5.0(X11; Ubuntu; Linu...)Gecko/20100101 Firefox/60.0")
                    .maxBodySize(0)
                    .ignoreHttpErrors(true)
                    .timeout(600000)
                    .get();
        
        Element data_json = docSong.select("#zplayerjs-wrapper").first();
        String url_data_json = CrawlerContracts.HOST + "/xhr" + data_json.attr("data-xml");
        System.out.println(url_data_json);
        
        String dataJson = Jsoup.connect(url_data_json).get().body().html();
        
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(dataJson);
        String dJSONIDSong = "", dJSONNameSong = "", dJSONLinkDataMP3 = "",
                    dJSONKaraLink="", dJSONAlbumID = "", dJSONAlbumName = "",
                    dJSONAlbumImg = "", dJSONSongImg;
        long dJSONDuration = 0;
        ArrayList<String> dJSONSingerLinks = new ArrayList<>();
        
        JSONObject dataJsonObj = (JSONObject) jsonObject.get("data");
        System.out.println(dataJsonObj.toJSONString());
        dJSONIDSong = (String) dataJsonObj.get("id");
        System.out.println(dJSONIDSong);
        dJSONNameSong = (String) dataJsonObj.get("name");
        System.out.println(dJSONNameSong);
        dJSONSongImg = (String) dataJsonObj.get("thumbnail");
        
        dJSONKaraLink = (String) dataJsonObj.get("lyric");
        System.out.println(dJSONKaraLink);
        dJSONDuration = (long) dataJsonObj.get("duration");
        System.out.println(dJSONDuration);
        dJSONLinkDataMP3 = (String) ((JSONObject)dataJsonObj.get("source")).get("128");
        if(dJSONLinkDataMP3.indexOf("//") == 0){
            dJSONLinkDataMP3 = dJSONLinkDataMP3.replace("//", "");
        }
        System.out.println(dJSONLinkDataMP3);
        
        JSONObject albumJsonObj = (JSONObject)dataJsonObj.get("album");
        dJSONAlbumID = (String) albumJsonObj.get("id");
        System.out.println(dJSONAlbumID);
        dJSONAlbumName = (String) albumJsonObj.get("name");
        System.out.println(dJSONAlbumName);
        dJSONAlbumImg = (String) albumJsonObj.get("thumbnail_medium");
        
        // Set value from json
        song.setId(dJSONIDSong);
        song.setName(dJSONNameSong);
        album.id = dJSONAlbumID;
        album.name = dJSONAlbumName;
        album.image = album.id + ".jpg";
        album.songs.add(new Referencer(song.id, song.name));
        
        //return;

        Element songInfor = docSong.select("div.info-top-play").first();
            if(songInfor != null){
                Elements singersI = songInfor.child(0).getElementsByTag("h2");
                for(Element ele : singersI){
                    Singer singer = new Singer();
                    ModelInitiation.initSinger(singer);
                    //System.out.println(ele.attr("data-id"));
                    singer.setId(ele.attr("data-id"));
                    String href = ele.child(0).attr("href");
                    singer.setName(ele.text());
                    singer.imgCover = singer.imgAvatar = singer.id + ".jpg";
                    //Craw data singer
                    //------------------------------------
                    String linkProfile = "https://mp3.zing.vn" + href + "/tieu-su";
                    crawlSinger(linkProfile, singer);
                    
                    Referencer singerRef = new Referencer(singer.id, singer.name);
                    
                    song.getSingers().add(singerRef);

                    singer.songs.add(new Referencer(song.id, song.name));
                    singer.albums.add(new Referencer(singer.id, singer.name));
                    singers.add(singer);
                }
            }
            
            if(songInfor != null){
                Elements composersI = songInfor.child(1).getElementsByTag("div");
                for(Element ele : composersI){
                    if(ele.hasAttr("id")){
                        if(ele.attr("id").equals("composer-container")){
                            String composer = ele.child(0).text();
                            song.getComposers().add(composer);
                        }
                    }
                }
            }

            if(songInfor != null){
                Elements kindsI = songInfor.child(2).getElementsByTag("a");
                for(Element ele : kindsI){
                    String id = ele.attr("data-id");
                    String name = ele.text();
                    Kind kind = new Kind();
                    ModelInitiation.initKind(kind);
                    kind.setId(id);
                    kind.setName(name);
                    song.getKinds().add(new Referencer(id, name));
                    kind.songs.add(new Referencer(song.id, song.name));
                    kinds.add(kind);
                }
            }
            

            
            Element lyricE = docSong.select("div#lyrics-song").first();
            if(lyricE != null){
                Elements lyricsE = lyricE.child(0).child(0).child(0).children();
                lyric.id = song.id;
                song.lyrics = lyric.id;
                for(Element ele : lyricsE){
                    Element content = ele.getElementsByTag("p").first();
                    Element lyricsU = ele.getElementsByClass("fn-user").first();
                    DataLyric dataLyric = new DataLyric();
                    ModelInitiation.initDataLyric(dataLyric);
                    dataLyric.contributor = lyricsU.text();
                    dataLyric.content = content.html();
                    lyric.datas.add(dataLyric);
                }
            }
            
            // Gan data tham chieu
            song.album.id = album.id;
            song.album.name = album.name;
            song.lyrics = lyric.id;
            
            
            
            // Nap data
            song.kara = this.crawlKara(dJSONKaraLink);
            song.duration = (short) dJSONDuration;
            song.comment = song.id;
            song.image = song.id + ".jpg";
            
            System.out.println("*****Song:");
            System.out.println(song);
            System.out.println("*****Album:");
            System.out.println(album);
            System.out.println("*****Lyric:");
            System.out.println(lyric);
            System.out.println("*****Singers:");
            for(Singer singer : singers){
                System.out.println(singer);
            }
            
            System.out.println("*****Kinds:");
            for(Kind kind : kinds){
                System.out.println(kind);
            }
            
            
            

           // crawl nhac
           this.crawlAndSaveFile(new URL("https://" + dJSONLinkDataMP3),   CrawlerContracts.PATH_SONG_DATA + song.id + ".mp3" );
           
           // crawl image album
           this.crawlAndSaveFile(new URL(dJSONAlbumImg), CrawlerContracts.PATH_ALBUM + album.id + ".jpg");
           
           // crawl image song
           this.crawlAndSaveFile(new URL(dJSONSongImg), CrawlerContracts.PATH_SONG + song.id + ".jpg");
           
           
           // Chen DB
           DBSongModel.InsertSong(song);
           DBAlbumModel.InsertAlbum(album);
           DBLyricModel.InsertLyric(lyric);
           DBSingerModel.InsertSingers(singers);
        
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
        Element imgsDoc = docSinger.select("div.wrapper-page").first();
        String imgCover = imgsDoc.child(0).child(0).child(0).attr("src");
        String imgAVT = imgsDoc.child(0).child(0).child(1).child(0).child(0).child(0).attr("src");

        // CRAWL IMAGE SINGER
        this.crawlAndSaveFile(new URL(imgCover), CrawlerContracts.PATH_SINGER_COVER + singer.id + ".jpg");
        this.crawlAndSaveFile(new URL(imgAVT), CrawlerContracts.PATH_SINGER_AVATAR+ singer.id + ".jpg");
        
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
    
    private void crawlAndSaveFile(URL url, String pathSave) throws IOException{
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(pathSave);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    }
    
    private String crawlKara(String urlStr) throws IOException{
        String tempFileSave = "temp.txt";
        URL url = new URL(urlStr);
        this.crawlAndSaveFile(url, tempFileSave);
        
        FileReader fileReader = new FileReader(tempFileSave);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = "";
        String res = "";
        while((line = bufferedReader.readLine()) != null){
            res += line;
        }
        
        return res;
    }
    
}

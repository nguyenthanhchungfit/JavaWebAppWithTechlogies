/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlerzingmp3;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 *
 * @author cpu11165-local
 */
public class CrawlerZingMP3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException{
        // TODO code application logic here
        
        String urlSong = "https://mp3.zing.vn/bai-hat/Anh-Nang-Cua-Anh-Cho-Em-Den-Ngay-Mai-OST-Duc-Phuc/ZW78BW9D.html";
        //String url = "https://mp3.zing.vn/top100/Nhac-Tre/IWZ9Z088.html";    
        //Document doc = Jsoup.connect(url).get();
        
        File input = new File("resources/Top100Korea.html");
        Document doc = Jsoup.parse(input, "UTF-8");
        
        ArrayList<DataCrawler> datas = crawlZingTop(doc);
        
        int count = 0;
        
        for(DataCrawler data : datas){
            
            count++;
            System.out.println(data.linkSong);
            // CRAWSONG
            //-------------------------------------------------
            SongModel song = new SongModel();
            
            
            song.setName(data.songName);
            song.setAlbum(data.albumName);
            song.setId(data.id);
            
            
            //ignoreHttpErrors(true)
            Document docSong = Jsoup.connect(data.linkSong).
                    header("Accept-Encoding", "gzip, deflate")
                    .userAgent("Mozilla/5.0(X11; Ubuntu; Linu...)Gecko/20100101 Firefox/60.0")
                    .maxBodySize(0)
                    .ignoreHttpErrors(true)
                    .timeout(600000)
                    .get();
          
            Element songInfor = docSong.select("div.info-top-play").first();
            //System.out.println(songInfor.html());
            if(songInfor != null){
                Elements singersI = songInfor.child(0).getElementsByTag("h2");
                for(Element ele : singersI){
                    SingerModel singer = new SingerModel();
                    //System.out.println(ele.attr("data-id"));
                    singer.setId(ele.attr("data-id"));
                    String href = ele.child(0).attr("href");
                    singer.setName(ele.text());
                    //Craw data singer
                    //------------------------------------
                    String linkProfile = "https://mp3.zing.vn" + href + "/tieu-su";
                
                    Document docSinger = Jsoup.connect(linkProfile).
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
                
                        singer.setRealName(realName.substring(realName.indexOf(':') + 1));
                        singer.setDob(dob.substring(dob.indexOf(':')+1));
                        singer.setCountry(country.substring(country.indexOf(':') + 1));
                
                        String description = infor.text();
                        String sdescription = description.substring(realName.length() + dob.length()
                                          +  country.length() + 3);
                        singer.setDescription(sdescription);   
                        //System.out.println(sdescription);
                        song.getSingers().add(singer);
                        
                        if(!InsertData.isExistedSinger(singer.getId())){
                            System.out.println("=>>>>> SingerOK!");
                            InsertData.InsertSinger(singer);
                        }     
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
                //System.out.println(kind);
                }
            }
            
        
            
            
            Element lyricE = docSong.select("div#lyrics-song").first();
            if(lyricE != null){
                Elements lyricsE = lyricE.child(0).child(0).child(0).children();
                for(Element ele : lyricsE){
                    Element content = ele.getElementsByTag("p").first();
                    song.getLyrics().add(content.text());
                //System.out.println(content.text());
                }
            }
            
            
            
            
            
            System.out.println("\n****************************************");
            System.out.println("Count: " + count);
            System.out.println(song);
            System.out.println("****************************************\n");
            
            if(!InsertData.isExistedSong(song.getId())){
                System.out.println("=>>>>> SongOK!");
                InsertData.InsertSong(song);
            }
            
            if(count%20 == 0){
                Thread.sleep(1000);
            }
        }
        
             
        
        
        //crawlZingMp3Song(urlSong);    
    }
    
    
    private static void crawlZingMp3Song(String urlSong) throws IOException{
        Document doc = Jsoup.connect(urlSong).get();
        Element songInfor = doc.select("div.info-top-play").first();
        Elements singersI = songInfor.child(0).getElementsByTag("h2");
        for(Element ele : singersI){
            System.out.println(ele.attr("data-id"));
            String href = ele.child(0).attr("href");
            System.out.println(href);
            crawlZingMp3Singer("https://mp3.zing.vn" + href + "/tieu-su");
            
        }
        
        Elements composersI = songInfor.child(1).getElementsByTag("div");
        for(Element ele : composersI){
            if(ele.hasAttr("id")){
                if(ele.attr("id").equals("composer-container")){
                    System.out.println(ele.child(0).text());
                }
            }
        }
        
        Elements kindsI = songInfor.child(2).getElementsByTag("a");
        for(Element ele : kindsI){
            System.out.println(ele.text());
        }
        
        
        
        Element lyricE = doc.select("div#lyrics-song").first();
        Elements lyricsE = lyricE.child(0).child(0).child(0).children();
        for(Element ele : lyricsE){
            Element content = ele.getElementsByTag("p").first();
            System.out.println(content.text());
        }
    }
    
    private static void crawlZingMp3Singer(String urlSinger) throws IOException{
        Document doc = Jsoup.connect(urlSinger).get();
        Element infor = doc.select("div.entry").first();
        Elements liE = infor.child(0).children();
        for(Element ele : liE){
            System.out.println(ele.text());
        }    
        System.out.println(infor.text());
    }
    
        
    
    private static ArrayList<DataCrawler> crawlZingTop(Document doc) throws IOException{
        ArrayList<DataCrawler> datas = new ArrayList<>();     
        Element topsong = doc.select("div#topsong ul").first();
        Elements songs = topsong.children();     
        for(Element ele : songs){
            
            DataCrawler data = new DataCrawler();
            
            data.id = ele.attr("id");
            data.linkSong = ele.child(1).child(0).attr("href");
            data.songName = ele.child(1).child(1).child(0).text();
            Elements artist = ele.child(1).child(2).getElementsByTag("a");
            for(Element art : artist){
                if(art.hasAttr("class")){
                    data.albumName = art.attr("title");
                }else{
                    SingerModel singer = new SingerModel();
                    singer.setName(art.attr("title"));
                    data.singers.add(singer);
                }
            }
            
            datas.add(data);   
        }

        /*
        for(DataCrawler data : datas){
            System.out.println("*****       item           ********");
            System.out.println(data);
            System.out.println("************************************\n");
        }
        */
        return datas;
    }
}

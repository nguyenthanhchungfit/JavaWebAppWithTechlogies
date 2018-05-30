/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlerzingmp3;

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
    public static void main(String[] args) throws IOException{
        // TODO code application logic here
        
        String urlSong = "https://mp3.zing.vn/bai-hat/Minh-Cuoi-Nhau-Di-Huynh-James-Pjnboys/ZW9AFO8O.html";
        crawlZingMp3Song(urlSong);
        
    }
    
    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }
    
    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
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
    
    
    private static ArrayList<DataCrawler> crawlZingTop() throws IOException{
        ArrayList<DataCrawler> datas = new ArrayList<>();
        String url = "https://mp3.zing.vn/top100/Nhac-Tre/IWZ9Z088.html";    
        Document doc = Jsoup.connect(url).get();
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
                    Singer singer = new Singer();
                    singer.setName(art.attr("title"));
                    data.singers.add(singer);
                }
            }
            
            datas.add(data);   
        }

        
        for(DataCrawler data : datas){
            System.out.println("*****       item           ********");
            System.out.println(data);
            System.out.println("************************************\n");
        }
        
        return datas;
    }
}

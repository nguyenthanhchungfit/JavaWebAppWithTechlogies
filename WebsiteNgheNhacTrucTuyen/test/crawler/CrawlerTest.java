/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawler;

import crawler_data.ThreadCrawlZingMp3;
import crawler_data.ZingMP3Crawler;
import java.io.IOException;
import java.util.ArrayList;
import models.Album;
import models.Lyric;
import models.Singer;
import models.Song;
import org.json.simple.parser.ParseException;

/**
 *
 * @author cpu11165-local
 */
public class CrawlerTest {
    public static void main(String[] args) throws IOException, ParseException {
//        String nameSong = "Xe đạp";
//        ThreadCrawlZingMp3 myThread = new ThreadCrawlZingMp3(nameSong);
//        myThread.start();

          String url = "https://mp3.zing.vn/bai-hat/Dung-Nhu-Thoi-Quen-JayKii-Sara-Luu/ZW9C0WDI.html";
          ZingMP3Crawler crawler = new ZingMP3Crawler();
          crawler.crawlSongByUrl(url);
    }
}

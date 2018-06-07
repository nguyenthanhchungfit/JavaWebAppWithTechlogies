/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawler_data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author cpu11165-local
 */
public class ZingMP3Crawler {
    public static void main(String[] args) throws IOException{
        String url = "https://mp3.zing.vn/tim-kiem/bai-hat.html?q=xe+đạp";
        Document doc = Jsoup.connect(url).get();
        System.out.println(doc);
    }
}

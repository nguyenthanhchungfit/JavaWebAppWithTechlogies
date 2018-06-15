/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlerdatamp3zing;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;





/**
 *
 * @author cpu11165-local
 */
public class CrawlerDataMp3Zing {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MalformedURLException, IOException, ParseException {
        //Song song = gnGetLinkMp3.getSong("https://mp3.zing.vn/bai-hat/Roi-Bo-Hoa-Minzy/ZW9C0DOU.html");
        
        //System.out.println(song);
        //URL url = new URL("http://zmp3-mp3-s1.zadn.vn/6aced1fc06b8efe6b6a9/244466968514412344?authen=exp=1529112049~acl=/6aced1fc06b8efe6b6a9/*~hmac=a5b1846378650962cc4f44b2390d43d7");
        //ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        //FileOutputStream fos = new FileOutputStream("test/file.mp3");
        //fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        
        Document doc = Jsoup.connect("https://mp3.zing.vn/xhr/media/get-source?type=audio&key=LHJGykHaxkZWWnctmtFGkmyZpvRmVLbni").get();
        String data = doc.body().html();
      
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(data);
        System.out.println(jsonObject.toJSONString());
        long err = (long) jsonObject.get("err");
        System.out.println("Err: " + err);
    }
    
}

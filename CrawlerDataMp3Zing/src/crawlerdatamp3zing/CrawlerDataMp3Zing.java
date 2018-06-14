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
public class CrawlerDataMp3Zing {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Song song = gnGetLinkMp3.getSong("https://mp3.zing.vn/bai-hat/Roi-Bo-Hoa-Minzy/ZW9C0DOU.html");
        
        System.out.println(song);
    }
    
}

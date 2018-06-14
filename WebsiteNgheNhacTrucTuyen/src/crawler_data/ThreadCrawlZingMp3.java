/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawler_data;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cpu11165-local
 */
public class ThreadCrawlZingMp3 extends Thread{

    private String nameSong;
    private ZingMP3Crawler crawler;
    
    public ThreadCrawlZingMp3(String nameSong){
        crawler = new ZingMP3Crawler();
        this.nameSong = nameSong;
    }
    
    @Override
    public void run() {
        try {
            crawler.crawl(this.nameSong);
        } catch (IOException ex) {
            Logger.getLogger(ThreadCrawlZingMp3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

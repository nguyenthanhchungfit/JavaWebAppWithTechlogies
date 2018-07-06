/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawler_data;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cpu11165-local
 */
public class ThreadCrawlResourceZingMP3 extends Thread{

    private String urlSongData = "";
    private String pathFileSongData = "";
    private String imgAlbum = "";
    private String pathFileImgAlbum = "";
    private static ZingMP3Crawler crawler = new ZingMP3Crawler();
    
    public ThreadCrawlResourceZingMP3(String urlSongData, String pathFileSongData, String urlImgAlbum, String pathFileImgAlbum){
        this.urlSongData = urlSongData;
        this.pathFileSongData = pathFileSongData;
        this.imgAlbum = urlImgAlbum;
        this.pathFileImgAlbum = pathFileImgAlbum;
    }
    
    @Override
    public void run() {
        try {
            if(!this.urlSongData.isEmpty() && !this.pathFileSongData.isEmpty()){
                crawler.crawlAndSaveFile(new URL(this.urlSongData), this.pathFileSongData);
            }
            if(!this.imgAlbum.isEmpty() && !this.pathFileImgAlbum.isEmpty()){
                crawler.crawlAndSaveFile(new URL(this.imgAlbum), this.pathFileImgAlbum);
            }
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(ThreadCrawlResourceZingMP3.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ThreadCrawlResourceZingMP3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

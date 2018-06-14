/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawler;

import crawler_data.ThreadCrawlZingMp3;

/**
 *
 * @author cpu11165-local
 */
public class CrawlerTest {
    public static void main(String[] args) {
        String nameSong = "Xe đạp";
        ThreadCrawlZingMp3 myThread = new ThreadCrawlZingMp3(nameSong);
        myThread.start();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawler_data;

import java.io.IOException;

/**
 *
 * @author cpu11165-local
 */
public interface CrawlerSongIface {
    void crawSongByName(String name) throws IOException;
    void crawSongByUrl(String url);
    void crawSinger(String url);
    
}

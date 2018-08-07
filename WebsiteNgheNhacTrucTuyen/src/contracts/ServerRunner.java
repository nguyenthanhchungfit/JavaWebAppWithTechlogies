/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contracts;

import admin_server.LogsConsumer;
import cache_data.CacheUpdateDataConsumer;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import kafka.LookupSongConsumer;
import org.json.simple.parser.ParseException;

/**
 *
 * @author cpu11165-local
 */
public class ServerRunner {
    
    public static void main(String[] args) throws IOException, ParseException, Exception {
        RunnableLookupSongConsumer lookupSongConsumerThread = new RunnableLookupSongConsumer();
        lookupSongConsumerThread.run();
        
        RunnableCacheUpdateDataConsumer cacheUpdateDataConsumerThread = new RunnableCacheUpdateDataConsumer();
        cacheUpdateDataConsumerThread.run();
        
        RunnableLogsConsumer logsConsumerThread = new RunnableLogsConsumer();
        logsConsumerThread.run();
   
    }
    
    static class RunnableLookupSongConsumer implements Runnable{
        @Override
        public void run() {
            try {
                LookupSongConsumer.run();
            } catch (IOException ex) {
                Logger.getLogger(ServerRunner.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(ServerRunner.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    static class RunnableCacheUpdateDataConsumer implements Runnable{

        @Override
        public void run() {
            CacheUpdateDataConsumer.run();
        }
    
    }
    
    static class RunnableLogsConsumer implements Runnable{
        @Override
        public void run() {
            try {
                LogsConsumer.run();
            } catch (Exception ex) {
                Logger.getLogger(ServerRunner.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
}

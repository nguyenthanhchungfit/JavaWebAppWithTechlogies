/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web_server_cache;

import java.util.HashMap;
import java.util.Map;
import models.ModelInitiation;
import models.Referencer;
import models.Song;
import models.SongResult;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.transport.TSocket;
import redis.clients.jedis.Jedis;
import thrift_services.SongServices;

/**
 *
 * @author cpu11165-local
 */
public class SongCache {
    private static final String host = "localhost";
    private static final int port = 6379;
    private static final int port_data_server = 8001;
    private static int max_age = 10000;
    private static final Jedis jedis = new Jedis(host, port, max_age);

    public SongCache(){
        jedis.configSet("maxmemory-policy", "allkeys-lru");
        jedis.configSet("maxmemory", "100mb");
    }
    
    public void insertNewCache(Song song){
        String keySong = "song:" + song.id;
        
        Map<String, String> mapSong = new HashMap<>();
        mapSong.put("id", song.id);
        mapSong.put("name", song.name);
        mapSong.put("lyric", song.lyrics);
        mapSong.put("kara", song.kara);
        mapSong.put("duration", ((Short)song.duration).toString());
        mapSong.put("views", ((Long)song.views).toString());
        mapSong.put("comment", song.comment);
        mapSong.put("image", song.image);
        
        String keySongAlbum = keySong + ":album";
        mapSong.put("album", keySongAlbum);
        mapSong.put("amount_composer", ((Integer)song.composers.size()).toString());
        mapSong.put("amount_singer", ((Integer)song.singers.size()).toString());
        mapSong.put("amount_kind", ((Integer)song.kinds.size()).toString());
        
        
        jedis.hmset(keySong, mapSong);
        jedis.expire(keySong, max_age);
        
        // Tạo key album 
        Map<String, String> mapSongAlbum = new HashMap<>();
        mapSongAlbum.put("id", song.album.id);
        mapSongAlbum.put("name", song.album.name);
        jedis.hmset(keySongAlbum, mapSongAlbum);
        jedis.expire(keySongAlbum, max_age);
        
       
        // Lưu tác giả
        int index = 0;
        for(String composer : song.composers){
            String keyComposer = keySong + ":composer:" + index;
            Map<String, String> mapComposer = new HashMap<>();
            mapComposer.put("name", composer);
            jedis.hmset(keyComposer, mapComposer);
            jedis.expire(keyComposer, max_age);
            index++;
        }
        
        
        // Lưu loại kind
        index = 0;
        for(Referencer ref : song.kinds){
            String keyKind = keySong + ":kind:" + index;
            Map<String, String> mapKind = new HashMap<>();
            mapKind.put("id", ref.id);
            mapKind.put("name", ref.name);
            jedis.hmset(keyKind, mapKind);
            jedis.expire(keyKind, max_age);
            index++;
        }
        
       
        // Lưu loại kind
        index = 0;
        for(Referencer ref : song.singers){
            String keySinger = keySong + ":singer:" + index;
            Map<String, String> mapSinger = new HashMap<>();
            mapSinger.put("id", ref.id);
            mapSinger.put("name", ref.name);
            jedis.hmset(keySinger, mapSinger);
            jedis.expire(keySinger, max_age);
            index++;
        }
        
         
    }
    
    public boolean isExisted(String key){
        return jedis.exists(key);
    }
    
    public Long getTimeToLive(String key){
        return jedis.ttl(key);
    }
    
    public Song getCacheSong(String key){ 
        if(!this.isExisted(key)){
            return null;
        }
        
        Song song = new Song();
        ModelInitiation.initSong(song);
        
        song.id = jedis.hget(key, "id");
        song.name = jedis.hget(key, "name");
        song.lyrics = jedis.hget(key, "lyric");
        song.kara = jedis.hget(key, "kara");
        song.duration = Short.parseShort(jedis.hget(key, "duration"));
        song.views = Long.parseLong(jedis.hget(key, "views"));
        song.comment = jedis.hget(key, "comment");
        song.image = jedis.hget(key, "image");
        
        
        String keySong = "song:" + song.id;
        jedis.expire(keySong, max_age);
        
        // get Album
        String keySongAlbum = keySong + ":album";
        song.album.id = jedis.hget(keySongAlbum, "id");
        song.album.name = jedis.hget(keySongAlbum, "name");
        jedis.expire(keySongAlbum, max_age);
        
        // get Composer
        int composer_size = Integer.parseInt(jedis.hget(keySong, "amount_composer"));
        for(int i =0; i < composer_size; i++){
            String keyComposer = keySong + ":composer:" + i;
            jedis.expire(keyComposer, max_age);
            String name = jedis.hget(keyComposer, "name");
            song.composers.add(name);
        }
        
        // get Kind
        int kind_size = Integer.parseInt(jedis.hget(keySong, "amount_kind"));
        for(int i =0; i < kind_size; i++){
            String keyKind= keySong + ":kind:" + i;
            jedis.expire(keyKind, max_age);
            String id = jedis.hget(keyKind, "id");
            String name = jedis.hget(keyKind, "name");
            song.kinds.add(new Referencer(id, name));
        }
        
        // getSinger
        int singer_size = Integer.parseInt(jedis.hget(keySong, "amount_singer"));
        for(int i =0; i < singer_size; i++){
            String keySinger = keySong + ":singer:" + i;
            jedis.expire(keySinger, max_age);
            String id = jedis.hget(keySinger, "id");
            String name = jedis.hget(keySinger, "name");
            song.singers.add(new Referencer(id, name));
        }
             
        return song;
    }
    
    public void setTimeout(int timeout){
        max_age = timeout;
    }
    
    public void deleteCacheSongAt(String key){
        String keySong = "song:" + key;
        jedis.del(keySong);
    }
    
    
    private Song getSongFromDataServerById(String id){
        System.out.println("GET SONG:" + id +", REQUEST TO DATA SERVER");
        Song song = null;
        try{
            TSocket transport  = new TSocket(host, port_data_server);
            transport.open();
            
            TBinaryProtocol protocol = new TBinaryProtocol(transport);
            TMultiplexedProtocol mpSongServices = new TMultiplexedProtocol(protocol, "SongServices");
            SongServices.Client songServices = new SongServices.Client(mpSongServices);
                    
            SongResult songResult = songServices.getSongById(id);
            if(songResult.result == 0){
                song = songResult.song;
            }
            transport.close(); 
        }catch(TException ex){
            ex.printStackTrace();
        }  
        return song;
    }
    
    public void updateCacheSongAt(String key){
        Song song = this.getSongFromDataServerById(key);
        if(song != null){
            this.insertNewCache(song);
        }
    }
    
}

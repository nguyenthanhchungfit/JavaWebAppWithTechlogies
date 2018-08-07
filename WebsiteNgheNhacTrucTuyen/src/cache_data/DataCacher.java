/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cache_data;

import Helpers.FormatPureString;
import contracts.DataServerContract;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.DataLyric;
import models.Lyric;
import models.ModelInitiation;
import models.Referencer;
import models.Session;
import models.Singer;
import models.SingerResult;
import models.Song;
import models.SongResult;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import redis.clients.jedis.Jedis;
import server_user.DBUserContract;
import thrift_services.LyricServices;
import thrift_services.SingerServices;
import thrift_services.SongServices;

/**
 *
 * @author cpu11165-local
 */
public class DataCacher {

    private static final String HOST = "localhost";
    private static final int PORT = 6379;

    private static final String HOST_DATA_SERVER = DataServerContract.HOST_SERVER;
    private static final int PORT_DATA_SERVER = DataServerContract.PORT;

    public static final String KEY_AMOUNT_USER = "amount:user";
    public static final String KEY_AMOUNT_SONG = "amount:song";
    public static final String KEY_AMOUNT_SINGER = "amount:singer";
    public static final String KEY_AMOUNT_ALBUM = "amount:album";
    public static final String KEY_AMOUNT_LYRIC = "amount:lyric";

    private static int max_age = 10000;
    private static final Jedis jedis = new Jedis(HOST, PORT, max_age);
    private static DataCacher dataCacher = new DataCacher();

    private DataCacher() {
        jedis.configSet("maxmemory-policy", "allkeys-lru");
        jedis.configSet("maxmemory", "100mb");
    }

    public static DataCacher getInstance() {
        return dataCacher;
    }

    public boolean isExisted(String key) {
        return jedis.exists(key);
    }

    public Long getTimeToLive(String key) {
        return jedis.ttl(key);
    }

    public void setTimeout(int timeout) {
        max_age = timeout;
    }

    /*------------- Song ---------------------*/
    public void insertNewSongCache(Song song) {
        String keySong = "song:" + song.id;

        Map<String, String> mapSong = new HashMap<>();
        mapSong.put("id", song.id);
        mapSong.put("name", song.name);
        mapSong.put("lyric", song.lyrics);
        mapSong.put("kara", song.kara);
        mapSong.put("duration", ((Short) song.duration).toString());
        mapSong.put("views", ((Long) song.views).toString());
        mapSong.put("comment", song.comment);
        mapSong.put("image", song.image);

        String keySongAlbum = keySong + ":album";
        mapSong.put("album", keySongAlbum);
        mapSong.put("amount_composer", ((Integer) song.composers.size()).toString());
        mapSong.put("amount_singer", ((Integer) song.singers.size()).toString());
        mapSong.put("amount_kind", ((Integer) song.kinds.size()).toString());

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
        for (String composer : song.composers) {
            String keyComposer = keySong + ":composer:" + index;
            Map<String, String> mapComposer = new HashMap<>();
            mapComposer.put("name", composer);
            jedis.hmset(keyComposer, mapComposer);
            jedis.expire(keyComposer, max_age);
            index++;
        }

        // Lưu loại kind
        index = 0;
        for (Referencer ref : song.kinds) {
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
        for (Referencer ref : song.singers) {
            String keySinger = keySong + ":singer:" + index;
            Map<String, String> mapSinger = new HashMap<>();
            mapSinger.put("id", ref.id);
            mapSinger.put("name", ref.name);
            jedis.hmset(keySinger, mapSinger);
            jedis.expire(keySinger, max_age);
            index++;
        }

    }

    public Song getCacheSong(String key) {
        if (!this.isExisted(key)) {
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

        jedis.expire(key, max_age);

        // get Album
        String keySongAlbum = key + ":album";
        song.album.id = jedis.hget(keySongAlbum, "id");
        song.album.name = jedis.hget(keySongAlbum, "name");
        jedis.expire(keySongAlbum, max_age);

        // get Composer
        int composer_size = Integer.parseInt(jedis.hget(key, "amount_composer"));
        for (int i = 0; i < composer_size; i++) {
            String keyComposer = key + ":composer:" + i;
            jedis.expire(keyComposer, max_age);
            String name = jedis.hget(keyComposer, "name");
            song.composers.add(name);
        }

        // get Kind
        int kind_size = Integer.parseInt(jedis.hget(key, "amount_kind"));
        for (int i = 0; i < kind_size; i++) {
            String keyKind = key + ":kind:" + i;
            jedis.expire(keyKind, max_age);
            String id = jedis.hget(keyKind, "id");
            String name = jedis.hget(keyKind, "name");
            song.kinds.add(new Referencer(id, name));
        }

        // getSinger
        int singer_size = Integer.parseInt(jedis.hget(key, "amount_singer"));
        for (int i = 0; i < singer_size; i++) {
            String keySinger = key + ":singer:" + i;
            jedis.expire(keySinger, max_age);
            String id = jedis.hget(keySinger, "id");
            String name = jedis.hget(keySinger, "name");
            song.singers.add(new Referencer(id, name));
        }

        return song;
    }

    public void updateCacheSongAt(String key) {
        Song song = this.getSongFromDataServerById(key);
        if (song != null) {
            this.insertNewSongCache(song);
        }
    }

    public void deleteCacheSongAt(String key) {
        jedis.del(key);
    }

    private Song getSongFromDataServerById(String key) {
        Song song = null;
        String id = key.replace("song:", "");
        try {
            TSocket socket = new TSocket(HOST_DATA_SERVER, PORT_DATA_SERVER);
            TTransport transport = new TFramedTransport(socket);
            transport.open();

            TBinaryProtocol protocol = new TBinaryProtocol(transport);

            TMultiplexedProtocol mpSongServices = new TMultiplexedProtocol(protocol, "SongServices");
            SongServices.Client songServices = new SongServices.Client(mpSongServices);

            SongResult songResult = songServices.getSongById(id);
            if (songResult.result == 0) {
                song = songResult.song;
            }
            transport.close();
        } catch (TException ex) {
            ex.printStackTrace();
        }
        return song;
    }

    /*------------- End Song ---------------------*/
 /*--------------- Singer ----------------------*/
    public void insertNewSingerCache(Singer singer) {
        String keySinger = "singer:" + singer.id;

        Map<String, String> mapSinger = new HashMap<>();
        mapSinger.put("id", singer.id);
        mapSinger.put("name", singer.name);
        mapSinger.put("realname", singer.realname);
        mapSinger.put("dob", singer.dob);
        mapSinger.put("country", singer.country);
        mapSinger.put("description", singer.description);
        mapSinger.put("img_avatar", singer.imgAvatar);
        mapSinger.put("img_cover", singer.imgCover);
        mapSinger.put("amount_song", singer.songs.size() + "");
        mapSinger.put("amount_album", singer.albums.size() + "");
        mapSinger.put("amount_video", singer.videos.size() + "");
        jedis.hmset(keySinger, mapSinger);
        jedis.expire(keySinger, max_age);

        // Lưu album
        int index = 0;
        for (Referencer ref : singer.albums) {
            String keyAlbum = keySinger + ":album:" + index;
            Map<String, String> mapAlbum = new HashMap<>();
            mapAlbum.put("id", ref.id);
            mapAlbum.put("name", ref.name);
            jedis.hmset(keyAlbum, mapAlbum);
            jedis.expire(keyAlbum, max_age);
            index++;
        }

        // Lưu loại song
        index = 0;
        for (Referencer ref : singer.songs) {
            String keySong = keySinger + ":song:" + index;
            Map<String, String> mapSong = new HashMap<>();
            mapSong.put("id", ref.id);
            mapSong.put("name", ref.name);
            jedis.hmset(keySong, mapSong);
            jedis.expire(keySong, max_age);
            index++;
        }

        // Lưu loại video
        index = 0;
        for (Referencer ref : singer.videos) {
            String keyVideo = keySinger + ":video:" + index;
            Map<String, String> mapVideo = new HashMap<>();
            mapVideo.put("id", ref.id);
            mapVideo.put("name", ref.name);
            jedis.hmset(keyVideo, mapVideo);
            jedis.expire(keyVideo, max_age);
            index++;
        }
    }

    public Singer getCacheSinger(String key) {
        if (!this.isExisted(key)) {
            return null;
        }

        Singer singer = new Singer();
        ModelInitiation.initSinger(singer);

        singer.id = jedis.hget(key, "id");
        singer.name = jedis.hget(key, "name");
        singer.realname = jedis.hget(key, "realname");
        singer.dob = jedis.hget(key, "dob");
        singer.country = jedis.hget(key, "country");
        singer.description = jedis.hget(key, "description");
        singer.imgAvatar = jedis.hget(key, "img_avatar");
        singer.imgCover = jedis.hget(key, "img_cover");
        jedis.expire(key, max_age);

        // getSong
        int song_size = Integer.parseInt(jedis.hget(key, "amount_song"));
        for (int i = 0; i < song_size; i++) {
            String keySong = key + ":song:" + i;
            jedis.expire(keySong, max_age);
            String id = jedis.hget(keySong, "id");
            String name = jedis.hget(keySong, "name");
            singer.songs.add(new Referencer(id, name));
        }

        // getAlbum
        int album_size = Integer.parseInt(jedis.hget(key, "amount_album"));
        for (int i = 0; i < album_size; i++) {
            String keyAlbum = key + ":album:" + i;
            jedis.expire(keyAlbum, max_age);
            String id = jedis.hget(keyAlbum, "id");
            String name = jedis.hget(keyAlbum, "name");
            singer.albums.add(new Referencer(id, name));
        }

        // getVideo
        int video_size = Integer.parseInt(jedis.hget(key, "amount_video"));
        for (int i = 0; i < video_size; i++) {
            String keyVideo = key + ":video:" + i;
            jedis.expire(keyVideo, max_age);
            String id = jedis.hget(keyVideo, "id");
            String name = jedis.hget(keyVideo, "name");
            singer.videos.add(new Referencer(id, name));
        }

        return singer;
    }

    public void updateCacheSingerAt(String key) {
        Singer singer = this.getSingerFromDataServerById(key);
        if (singer != null) {
            this.insertNewSingerCache(singer);
        }
    }

    public void deleteCacheSingerAt(String key) {
        jedis.del(key);
    }

    private Singer getSingerFromDataServerById(String id) {
        System.out.println("GET SINGER : ID=" + id);
        Singer singer = null;
        try {
            TSocket socket = new TSocket(HOST_DATA_SERVER, PORT_DATA_SERVER);
            TTransport transport = new TFramedTransport(socket);
            transport.open();

            TBinaryProtocol protocol = new TBinaryProtocol(transport);
            TMultiplexedProtocol mpSingerServices = new TMultiplexedProtocol(protocol, "SingerServices");
            SingerServices.Client singerServices = new SingerServices.Client(mpSingerServices);
            SingerResult sr = singerServices.getSingerById(id);
            if (sr.result == 0) {
                singer = sr.singer;
            }
            transport.close();
        } catch (TException ex) {
            ex.printStackTrace();
        }
        return singer;
    }

    /*--------------- End Singer ----------------------*/
 /*---------------- Lyric --------------------------*/
    public void insertNewLyricCache(Lyric lyric) {
        String keyLyric = "lyric:" + lyric.id;
        Map<String, String> mapLyric = new HashMap<>();
        mapLyric.put("id", lyric.id);
        mapLyric.put("amount_data_lyrics", lyric.datas.size() + "");

        jedis.hmset(keyLyric, mapLyric);
        jedis.expire(keyLyric, max_age);

        int index = 0;
        for (DataLyric dataLyric : lyric.datas) {
            String keyData = keyLyric + ":data:" + index;
            Map<String, String> mapDataLyric = new HashMap<>();
            mapDataLyric.put("contributor", dataLyric.contributor);
            mapDataLyric.put("content", dataLyric.content);

            jedis.hmset(keyData, mapDataLyric);
            jedis.expire(keyData, max_age);
            index++;
        }

    }

    public Lyric getCacheLyric(String key) {
        if (!this.isExisted(key)) {
            return null;
        }

        Lyric lyric = new Lyric();
        ModelInitiation.initLyric(lyric);

        lyric.id = jedis.hget(key, "id");
        jedis.expire(key, max_age);
        int dataLyricSize = Integer.parseInt(jedis.hget(key, "amount_data_lyrics"));

        for (int i = 0; i < dataLyricSize; i++) {
            String keyData = key + ":data:" + i;
            String contributor = jedis.hget(keyData, "contributor");
            String content = jedis.hget(keyData, "content");
            lyric.datas.add(new DataLyric(contributor, content));
            jedis.expire(keyData, max_age);
        }

        return lyric;
    }

    public void updateCacheLyricAt(String key) {
        Lyric lyric = this.getyricsFromDataServerById(key);
        if (lyric != null) {
            this.insertNewLyricCache(lyric);
        }
    }

    public void deleteCacheLyricAt(String key) {
        jedis.del(key);
    }

    private Lyric getyricsFromDataServerById(String key) {
        Lyric lyric = new Lyric();
        String id = key.replace("lyric:", "");
        lyric.id = id;
        ArrayList<DataLyric> dataLyrics = null;
        try {
            TSocket socket = new TSocket(HOST_DATA_SERVER, PORT_DATA_SERVER);
            TTransport transport = new TFramedTransport(socket);
            transport.open();

            TBinaryProtocol protocol = new TBinaryProtocol(transport);
            TMultiplexedProtocol mpLyricServices = new TMultiplexedProtocol(protocol, "LyricServices");
            LyricServices.Client lyricServices = new LyricServices.Client(mpLyricServices);
            dataLyrics = (ArrayList<DataLyric>) lyricServices.getDataLyricsById(id);
            transport.close();
        } catch (TException ex) {
            ex.printStackTrace();
        }
        lyric.datas = dataLyrics;
        return lyric;
    }

    /*---------------- End Lyric --------------------------*/
 /*------------- Session ---------------------*/
    public void insertNewSession(Session newSession) {
        String keySession = "session:" + newSession.getSessionID();

        Map<String, String> mapSession = new HashMap<>();
        mapSession.put("username", newSession.getUsername());
        mapSession.put("type", newSession.getType() + "");
        mapSession.put("max-age", newSession.getMaxAge() + "");
        String expireDate = DBUserContract.DATE_TIME_FORMATTER.format(newSession.getExpires());
        mapSession.put("expires", expireDate);
        String lastAccessDate = DBUserContract.DATE_TIME_FORMATTER.format(newSession.getLastAccess());
        mapSession.put("last-access", lastAccessDate);

        jedis.hmset(keySession, mapSession);
        jedis.expire(keySession, newSession.getMaxAge());
    }

    public void deleteCacheSessionAt(String c_user) {
        String keySession = "session:" + c_user;
        jedis.del(keySession);
    }

    public Session getCacheSession(String c_user) {
        String keySession = "session:" + c_user;
        if (!this.isExisted(keySession)) {
            return null;
        }
        Session session = new Session();
        session.setSessionID(c_user);
        session.setUsername(jedis.hget(keySession, "username"));
        session.setType(Integer.parseInt(jedis.hget(keySession, "type")));
        session.setMaxAge(Integer.parseInt(jedis.hget(keySession, "max-age")));
        try {
            session.setExpires(DBUserContract.DATE_TIME_FORMATTER.parse(jedis.hget(keySession, "expires")));
            session.setLastAccess(DBUserContract.DATE_TIME_FORMATTER.parse(jedis.hget(keySession, "last-access")));
        } catch (ParseException ex) {
            Logger.getLogger(DataCacher.class.getName()).log(Level.SEVERE, null, ex);
        }

        jedis.expire(keySession, session.getMaxAge());
        // Update time
        Date dateNow = new Date();
        Date dateExpire = new Date(dateNow.getTime() + session.getMaxAge());
        jedis.hset(keySession, "last-access", DBUserContract.DATE_TIME_FORMATTER.format(dateNow));
        jedis.hset(keySession, "expires", DBUserContract.DATE_TIME_FORMATTER.format(dateExpire));
        return session;
    }

    public void updateTime(String c_user) {
        String keySession = "session:" + c_user;
        if (this.isExisted(keySession)) {
            int max_age = Integer.parseInt(jedis.hget(keySession, "max-age"));
            Date dateNow = new Date();
            Date dateExpire = new Date(dateNow.getTime() + max_age);
            jedis.hset(keySession, "last-access", DBUserContract.DATE_TIME_FORMATTER.format(dateNow));
            jedis.hset(keySession, "expires", DBUserContract.DATE_TIME_FORMATTER.format(dateExpire));
        }

    }

    /*------------- End Session -----------------*/
 /*-------------- Amount -----------------------*/
    public void insertNewAmount(String key, long number) {
        jedis.set(key, number + "");
    }

    public long getAmount(String key) {
        long number = 0;
        String numberStr = jedis.get(key);
        try {
            number = Long.parseLong(numberStr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return number;
    }

    /*-------------- End Amount -----------------------*/
}

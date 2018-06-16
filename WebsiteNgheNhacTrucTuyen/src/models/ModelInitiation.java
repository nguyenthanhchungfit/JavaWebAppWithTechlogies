/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.ArrayList;

/**
 *
 * @author cpu11165-local
 */
public class ModelInitiation {
    public static void initSong(Song song){
        song.album = new Referencer();
        song.singers = new ArrayList<>();
        song.composers = new ArrayList<>();  
        song.kinds = new ArrayList<>();
        song.id = song.name = song.kara = song.comment = song.image = song.lyrics = "";
        song.views = song.duration = 0;
        
    }

    public static void initVideo(Video video){
        video.singers = new ArrayList<>();
        video.qualities = new ArrayList<>();
        video.id = video.name = video.lyrics = video.comment = video.image = "";
        video.views = video.duration = 0;
    }
    
    public static void initLyric(Lyric lyric){
        lyric.id = "";
        lyric.datas = new ArrayList<>();
    }
    
    public static void initSinger(Singer singer){
        singer.id = singer.name = singer.realname = singer.dob = singer.country 
                = singer.description = singer.imgAvatar = singer.imgCover = "";
        singer.albums = new ArrayList<>();
        singer.songs = new ArrayList<>();
        singer.videos = new ArrayList<>();
    }
    
    public static void initUser(User user){
        user.password = user.username = user.name = user.dob = user.address
                    = user.phone = user.image = "";
        user.sex = false;
        user.favor_list_singers = new ArrayList<>();
        user.favor_list_songs = new ArrayList<>();
    }
    
    public static void initAlbum(Album album){
        album.id = album.name = album.image = "";
        album.songs = new ArrayList<>();
    }
    
    public static void initKind(Kind kind){
        kind.id = kind.name = "";
        kind.amount_songs = 0;
        kind.songs = new ArrayList<>();
    }
    
    public static void initComment(Comment comment){
        comment.id = "";
        comment.datas = new ArrayList<>();
    }
    
    public static void initDataComment(DataComment dataComment){
        dataComment.username = dataComment.date = dataComment.content = "";
    }
    
    public static void initDataLyric(DataLyric dataLyric){
        dataLyric.content = dataLyric.contributor = "";
    }
}

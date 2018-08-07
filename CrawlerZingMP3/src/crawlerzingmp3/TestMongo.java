/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlerzingmp3;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author cpu11165-local
 */
public class TestMongo {
    
    public static void main(String[] args){
//        SingerModel singer = new SingerModel("SS01", "Sơn tùng MTP", "Nguyễn Sơn Tùng", "19/05/1994", "Thái Bình - Việt Nam",
//                            "SƠn Tùng là một ca sỹ trẻ nổi tiếng!");
//        SongModel song = new SongModel("S01", "Chạy ngay đi", "Chạy ngay đi", new ArrayList<String>(Arrays.asList("VietPop", "Nhạc trẻ")), 
//                new ArrayList<String>(Arrays.asList("Chạy ngay đi trước khi con tim khô héo vl")),
//                new ArrayList<SingerModel>(Arrays.asList(singer)), 
//                new ArrayList<String>(Arrays.asList("Sơn Tùng","Neymar")));
//        
//        InsertData.InsertSinger(singer);
//        InsertData.InsertSong(song);

          System.out.println(InsertData.isExistedSong("S02"));
    }

}

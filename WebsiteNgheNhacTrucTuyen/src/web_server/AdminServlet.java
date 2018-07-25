/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web_server;

import cache_data.DataCacher;
import contracts.DataServerContract;
import contracts.MP3ServerContract;
import contracts.UserServerContract;
import hapax.Template;
import hapax.TemplateDictionary;
import hapax.TemplateLoader;
import hapax.TemplateResourceLoader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.transport.TSocket;
import thrift_services.AlbumServices;
import thrift_services.LyricServices;
import thrift_services.SingerServices;
import thrift_services.SongServices;
import thrift_services.UserServices;

/**
 *
 * @author cpu11165-local
 */
public class AdminServlet extends HttpServlet {
    
    private static final String host_data_server = DataServerContract.HOST_SERVER;
    private static final int port_data_server = DataServerContract.PORT;
    
    private static final String host_user_server = UserServerContract.HOST_SERVER;
    private static final int port_user_server = UserServerContract.PORT;
    
    private static final String server_name = MP3ServerContract.SERVRE_NAME;
    
    private DataCacher dataCacher = DataCacher.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        long amountSong = getAmountSongs();
        long amountUser = getAmountUsers();
        long amountSinger = getAmountSingers();
        long amountAlbum = getAmountAlbums();
        long amountLyric = getAmountLyrics();
        
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        TemplateLoader templateLoader = TemplateResourceLoader.create("public/hapax/");

        try {
            Template template = templateLoader.getTemplate("admin.xtm");
            TemplateDictionary templateDictionary = new TemplateDictionary();
            // Asign Value
            templateDictionary.setVariable("amount_users", amountUser +"");
            templateDictionary.setVariable("amount_songs", amountSong + "");
            templateDictionary.setVariable("amount_singers", amountSinger + "");
            templateDictionary.setVariable("amount_albums", amountAlbum + "");
            templateDictionary.setVariable("amount_lyrics", amountLyric + "");
            
            out.println(template.renderToString(templateDictionary));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Amount_User
    private long getAmountUsersFromUserServer(){
        long amount = 0;
        try{
            TSocket transport = new TSocket(host_user_server, port_user_server);
            transport.open();
            
            TBinaryProtocol protocol = new TBinaryProtocol(transport);
            TMultiplexedProtocol mpUserServices = new TMultiplexedProtocol(protocol, "UserServices");
            UserServices.Client userServices = new UserServices.Client(mpUserServices);
            amount = userServices.getTotalNumberUsers();      
            transport.close();
        }catch(TException ex){
            ex.printStackTrace();
        }
        return amount;
    }
    
    private long getAmountUsers(){
        long amount = 0;
        String keyUser = DataCacher.KEY_AMOUNT_USER;
        if(dataCacher.isExisted(keyUser)){
            amount = dataCacher.getAmount(keyUser);
        }else{
            amount = getAmountUsersFromUserServer();
            dataCacher.insertNewAmount(keyUser, amount);
        }
        return amount;
    }
    
    // Amount_Songs
    private long getAmountSongsFromDataServer(){
        long amount = 0;
        try{
            TSocket transport = new TSocket(host_data_server, port_data_server);
            transport.open();

            TBinaryProtocol protocol = new TBinaryProtocol(transport);
            TMultiplexedProtocol mpSongServices = new TMultiplexedProtocol(protocol, "SongServices");
            SongServices.Client songServices = new SongServices.Client(mpSongServices);
            
            amount = songServices.getTotalNumberSongs();
            transport.close();
        }catch(TException ex){
            ex.printStackTrace();
        }
        return amount;
    }
    
    private long getAmountSongs(){
        long amount = 0;
        String keySong = DataCacher.KEY_AMOUNT_SONG;
        if(dataCacher.isExisted(keySong)){
            amount = dataCacher.getAmount(keySong);
        }else{
            amount = getAmountSongsFromDataServer();
            dataCacher.insertNewAmount(keySong, amount);
        }
        return amount;
    }
    
    // Amount_Singers
    private long getAmountSingersFromDataServer(){
        long amount = 0;
        try{
            TSocket transport = new TSocket(host_data_server, port_data_server);
            transport.open();

            TBinaryProtocol protocol = new TBinaryProtocol(transport);
            TMultiplexedProtocol mpSingerServices = new TMultiplexedProtocol(protocol, "SingerServices");
            SingerServices.Client singerServices = new SingerServices.Client(mpSingerServices);
            amount = singerServices.getTotalNumberSingers();
            transport.close();
            
        }catch(TException ex){
            ex.printStackTrace();
        }
        return amount;
    }
    
    private long getAmountSingers(){
        long amount = 0;
        String keySinger = DataCacher.KEY_AMOUNT_SINGER;
        if(dataCacher.isExisted(keySinger)){
            amount = dataCacher.getAmount(keySinger);
        }else{
            amount = getAmountSingersFromDataServer();
            dataCacher.insertNewAmount(keySinger, amount);
        }
        return amount;
    }
    
    // Amount_Albums
    private long getAmountAlbumsFromDataServer(){
        long amount = 0;
        try{
            TSocket transport = new TSocket(host_data_server, port_data_server);
            transport.open();

            TBinaryProtocol protocol = new TBinaryProtocol(transport);
            TMultiplexedProtocol mpALbumServices = new TMultiplexedProtocol(protocol, "AlbumServices");
            AlbumServices.Client albumServices = new AlbumServices.Client(mpALbumServices);
            
            amount = albumServices.getTotalNumberAlbums();
            transport.close();
        }catch(TException ex){
            ex.printStackTrace();
        }
        return amount;
    }
    
    private long getAmountAlbums(){
        long amount = 0;
        String keyAlbum = DataCacher.KEY_AMOUNT_ALBUM;
        if(dataCacher.isExisted(keyAlbum )){
            amount = dataCacher.getAmount(keyAlbum );
        }else{
            amount = getAmountAlbumsFromDataServer();
            dataCacher.insertNewAmount(keyAlbum, amount);
        }
        return amount;
    }
    
    // Amount_Lyrics
    private long getAmountLyricsFromDataServer(){
        long amount = 0;
        try{
            TSocket transport = new TSocket(host_data_server, port_data_server);
            transport.open();

            TBinaryProtocol protocol = new TBinaryProtocol(transport);
            TMultiplexedProtocol mpLyricServices = new TMultiplexedProtocol(protocol, "LyricServices");
            LyricServices.Client lyricServices = new LyricServices.Client(mpLyricServices);
            
            amount = lyricServices.getTotalNumberLyrics();
            transport.close();
        }catch(TException ex){
            ex.printStackTrace();
        }
        return amount;
    }
    
    private long getAmountLyrics(){
        long amount = 0;
        String keyLyric = DataCacher.KEY_AMOUNT_LYRIC;
        if(dataCacher.isExisted(keyLyric)){
            amount = dataCacher.getAmount(keyLyric);
        }else{
            amount = getAmountLyricsFromDataServer();
            dataCacher.insertNewAmount(keyLyric, amount);
        }
        return amount;
    }
    

}

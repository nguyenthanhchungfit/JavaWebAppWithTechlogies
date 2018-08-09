/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thrift_services;

import Helpers.FormatPureString;
import contracts.DataServerContract;
import data_access_object.DBSongModelMongo;
import server_data.DBSongModel;
import elastic_search_engine.ESESong;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import kafka.ProducerKafka;
import models.Song;
import models.SongResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TException;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.json.simple.parser.ParseException;

/**
 *
 * @author cpu11165-local
 */
public class SongServicesImpl implements SongServices.Iface {

    private ESESong eseSong = new ESESong();
    private DBSongModel dbSongModel = new DBSongModelMongo();
    //private DBSongModel dbSongModel = new DBSongModelKyotoCabinet();

    private static final String SERVER_NAME = DataServerContract.SERVRE_NAME;

    private static final Logger logger = LogManager.getLogger(SongServicesImpl.class.getName());
    private static Stopwatch stopwatch = SimonManager.getStopwatch(DataServerContract.STOP_WATCH_SONG_SERVICE);

    @Override
    public SongResult getSongById(String id) throws TException {
        SongResult sr = dbSongModel.getSongById(id);
        return sr;
    }

    @Override
    public List<Song> getSongsSearchAPIByName(String name) throws TException {
        Split split = stopwatch.start();
        String messageLog = "";

        List<Song> listSong = dbSongModel.getSongsSearchAPIByName(name);
        if (listSong.isEmpty()) {
            ProducerKafka.send("song_lookup", ProducerKafka.count + "", name);
            try {
                Thread.sleep(2000);
                listSong = dbSongModel.getSongsSearchAPIByName(name);
            } catch (InterruptedException ex) {
                split.stop();
                messageLog = FormatPureString.formatStringMessageLogs(SERVER_NAME, split.runningFor(), ex.getMessage());
                logger.error(messageLog);
                //Logger.getLogger(SongServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                split.stop();
                messageLog = FormatPureString.formatStringMessageLogs(SERVER_NAME, split.runningFor(), "Search Song Results: " + listSong.size());
                logger.info(messageLog);
                return listSong;
            }
        } else {
            for (int i = 0; i < listSong.size(); i++) {
                String songName = listSong.get(i).name.toLowerCase();
                String namef = name.toLowerCase();
                if (!songName.startsWith(namef)) {
                    ProducerKafka.send("song_lookup", ProducerKafka.count + "", name);
                    break;
                }
            }
            split.stop();
            messageLog = FormatPureString.formatStringMessageLogs(SERVER_NAME, split.runningFor(), "Search Song Results: " + listSong.size());
            logger.info(messageLog);
            return listSong;
        }
    }

    @Override
    public List<Song> getSongsSearchESEByName(String name) throws TException {
        Split split = stopwatch.start();
        String messageLog = "";
        try {
            return eseSong.getSongsSearchByName(name);
        } catch (IOException ex) {
            messageLog = FormatPureString.formatStringMessageLogs(SERVER_NAME, split.runningFor(), ex.getMessage());
            logger.error(messageLog);
            //Logger.getLogger(SongServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            messageLog = FormatPureString.formatStringMessageLogs(SERVER_NAME, split.runningFor(), ex.getMessage());
            logger.error(messageLog);
            //Logger.getLogger(SongServicesImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    @Override
    public long getTotalNumberSongs() throws TException {
        Split split = stopwatch.start();
        String messageLog = "";
        long docs = dbSongModel.getTotalDocumentInDB();
        messageLog = FormatPureString.formatStringMessageLogs(SERVER_NAME, split.runningFor(), "GET THE NUMBER OF SONG TOTALS: " + docs);
        return docs;
    }

}

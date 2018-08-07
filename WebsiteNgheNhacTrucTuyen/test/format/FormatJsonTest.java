/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package format;

import Helpers.FormatJson;
import data_access_object.DBLyricModelMongo;
import java.util.ArrayList;
import models.DataLyric;


/**
 *
 * @author cpu11165-local
 */
public class FormatJsonTest {
    public static void main(String[] args) {
        ArrayList<DataLyric> dataLyrics = (ArrayList<DataLyric>) new DBLyricModelMongo().getDataLyricsById("ZW9C0WDI");
        String res =  FormatJson.convertDataLyricsToJSON(dataLyrics);
        System.out.println(res);
    }
}

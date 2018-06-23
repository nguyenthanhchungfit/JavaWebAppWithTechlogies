/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import java.util.ArrayList;
import models.DataLyric;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author cpu11165-local
 */
public class FormatJson {
    public static String convertDataLyricsToJSON(ArrayList<DataLyric> dataLyrics){
        JSONArray entriesArray = new JSONArray();
        
        for(DataLyric dataLyric : dataLyrics){
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("contributor", dataLyric.contributor);
            jsonObj.put("content", dataLyric.content);
            entriesArray.add(jsonObj);
        }
        
        return entriesArray.toJSONString();
    }
}

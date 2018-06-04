/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web_server;


import java.util.ArrayList;
import java.util.List;
import models.Singer;

/**
 *
 * @author Nguyen Thanh Chung
 */
public class Helper {
    public static String beautifyString(List<String> arr){
        
        if(arr.size() > 0){
            String res = arr.get(0);
            for(int i = 1; i < arr.size(); i++){
                res += ", " + arr.get(i);
            }
            return res;
        }else{
            return "";
        }
    }
    
//    public static ArrayList<String> convertFromListModel(List<Singer> singers){
//        ArrayList<String> arr = new ArrayList<>();
//        for(Singer singer : singers){
//            arr.add(singer.name);
//        }
//    }
}

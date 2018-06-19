/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import java.util.List;
import models.Referencer;

/**
 *
 * @author cpu11165-local
 */
public class FormatPureString {
    public static String formatStringFromRefs(List<Referencer> refs){
        String res = "";
        if(refs.size() > 0){
            res = refs.get(0).name;
            for(int i = 1; i< refs.size(); i++){
                res += ", " + refs.get(i).name;
            }
        }
        return res;
    }
    
    public static String formatStringFromStrings(List<String> refs){
        String res = "";
        if(refs.size() > 0){
            res = refs.get(0);
            for(int i = 1; i< refs.size(); i++){
                res += ", " + refs.get(i);
            }
        }
        return res;
    }
}

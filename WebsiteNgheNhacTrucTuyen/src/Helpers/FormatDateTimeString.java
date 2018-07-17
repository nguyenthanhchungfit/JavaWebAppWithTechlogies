/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author cpu11165-local
 */
public class FormatDateTimeString {
    private static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd-MM-yyyy");
    private static SimpleDateFormat DATE_TIME_FORMATTER = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
    
    public static String formatDate(Date date){
        if(date != null){
            return DATE_FORMATTER.format(date);
        }else{
            return "";
        }
    }
    
    public static String formatDateTime(Date date){
        if(date != null){
            return DATE_TIME_FORMATTER.format(date);
        }else{
            return "";
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_user;

import java.text.SimpleDateFormat;


/**
 *
 * @author cpu11165-local
 */
public class DBUserContract {
    public static final String HOST = "localhost";
    public static final int PORT = 27017;
    public static final String USERNAME = "thanhchung";
    public static final String PASSWORD = "123";
    public static final String DATABASE_NAME = "user_app_mp3";
    public static final String COLLECTION_CUSTOMERS = "customer";
    public static final String COLLECTION_ADMINS = "admin";
    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd-MM-yyyy");
    public static final SimpleDateFormat DATE_TIME_FORMATTER = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
}

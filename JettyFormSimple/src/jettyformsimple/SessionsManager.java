/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jettyformsimple;

import java.util.HashMap;

/**
 *
 * @author cpu11165-local
 */
public class SessionsManager {
    private static HashMap<String, Account> sessionMap;
    
    static{
        sessionMap = new HashMap<String, Account>();
    }

    public static void addNewSession(String session, Account account){
        sessionMap.put(session, account);
    }
    
    public static void removeSession(String session){
        sessionMap.remove(session);
    }
    
    public static boolean isExistedSession(String session){
        return sessionMap.containsKey(session);
    }
    
    public static Account getAccount(String session){
        return sessionMap.get(session);
    }
    
//    public static void changeAccount(String session, Account account){
//        sessionMap.replace(, account)
//    }
    
}

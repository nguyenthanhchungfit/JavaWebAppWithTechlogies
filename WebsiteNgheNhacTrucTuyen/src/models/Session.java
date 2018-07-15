/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.Date;

/**
 *
 * @author cpu11165-local
 */
public class Session {
    private String sessionID;
    private String username;
    private int type;
    private Date expires;
    private Date lastAccess;
    private int maxAge;
    
    public static final int MAX_AGE = 1800;

    public Session() {
        maxAge = MAX_AGE;
        Date date = new Date();
        lastAccess = date;
        expires = new Date(date.getTime() + maxAge * 1000);
    }

    
    public Session(String sessionID, String username, int type, Date expires, Date last_acess, int max_age) {
        this.sessionID = sessionID;
        this.username = username;
        this.type = type;
        this.expires = expires;
        this.lastAccess = last_acess;
        this.maxAge = max_age;
    }

    
    @Override
    public String toString() {
        return "Session{" + "sessionID=" + sessionID + ", username=" + username + ", type=" + type + ", expires=" + expires + ", last_access=" + lastAccess + ", max_age=" + maxAge + '}';
    }

    
    
    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    public Date getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(Date last_access) {
        this.lastAccess = last_access;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int max_age) {
        this.maxAge = max_age;
    }
    
    
}

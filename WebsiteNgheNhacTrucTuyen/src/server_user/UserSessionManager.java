/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_user;

import cache_data.DataCacher;
import java.util.ArrayList;
import java.util.Date;
import models.Session;

/**
 *
 * @author cpu11165-local
 */
public class UserSessionManager {

    private ArrayList<Session> sessions = new ArrayList<>();
    private static UserSessionManager instance = new UserSessionManager();
    private static final DataCacher dataCacher = DataCacher.getInstance();

    static {
        // init instance
        
    }

    private UserSessionManager() {

    }

    public static UserSessionManager getInstance() {
        return instance;
    }

    public boolean isAvailableSession(String sessionID) {
        Date dateNow = new Date();
        for (int i = 0; i < sessions.size(); i++) {
            Session session = sessions.get(i);
            if (session.getSessionID().equals(sessionID)) {
                if (dateNow.after(session.getExpires())) {
                    dataCacher.deleteCacheSessionAt(sessionID);
                } else {
                    dataCacher.updateTime(sessionID);
                    this.updateTime(i);
                    return true;
                }
            }
        }
        return false;
    }

    private void updateTime(String sessionID) {
        Date dateNow = new Date();
        for (int i = 0; i < sessions.size(); i++) {
            Session session = sessions.get(i);
            if (session.getSessionID().equals(sessionID)) {
                session.setLastAccess(dateNow);
                session.setExpires(new Date(dateNow.getTime() + session.getMaxAge() * 1000));
            }
        }
    }

    private void updateTime(int index) {
        Date dateNow = new Date();
        Session session = sessions.get(index);
        session.setLastAccess(dateNow);
        session.setExpires(new Date(dateNow.getTime() + session.getMaxAge() * 1000));
    }

}

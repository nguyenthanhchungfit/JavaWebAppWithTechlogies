/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_user;

import redis.clients.jedis.Jedis;

/**
 *
 * @author cpu11165-local
 */
public class SessionUserCache {
    private static final String host = "localhost";
    private static final int port = 6379;
    private static int max_age = 10000;
    private static final Jedis jedis = new Jedis(host, port, max_age);
    
    public SessionUserCache(){
        jedis.configSet("maxmemory-policy", "allkeys-lru");
        jedis.configSet("maxmemory", "100mb");
    }
    
    
    
    
}

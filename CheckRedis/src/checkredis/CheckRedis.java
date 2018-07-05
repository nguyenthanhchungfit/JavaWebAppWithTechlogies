/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkredis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import redis.clients.jedis.Jedis;



/**
 *
 * @author cpu11165-local
 */
public class CheckRedis {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //Connecting to Redis server on localhost 
        Jedis jedis = new Jedis("localhost"); 
        System.out.println("Connection to server sucessfully"); 
        //check whether server is running or not 
        System.out.println("Server is running: "+jedis.ping()); 
        
//        //set the data in redis string 
//        jedis.set("tutorialname", "Redis tutorial"); 
//        // Get the stored data and print it 
//        System.out.println("Stored string in redis:: "+ jedis.get("tutorialname")); 
        
//        Map<String, String> map = new HashMap<>();
//        map.put("id", "123");
//        map.put("user", "tchung");
//        map.put("age", "21");
//        map.put("password", "123");
//        
//        jedis.hmset("user:123", map);
        
        Map<String, String> res =  jedis.hgetAll("user:123");
        Iterator it = res.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println(pair.getKey() + " : " + pair.getValue());
        }
    }
    
}

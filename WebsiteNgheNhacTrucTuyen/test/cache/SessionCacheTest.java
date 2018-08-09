/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cache;

import cache_data.DataCacher;
import models.Session;

/**
 *
 * @author cpu11165-local
 */
public class SessionCacheTest {

    public static void main(String[] args) throws InterruptedException {
        DataCacher dataCacher = DataCacher.getInstance();
        String key = "8grxlFLnNXR3s0/LyvBq6w==:wBDgEnLiyAuVfkLmBTSGhA==nguyenthanhchung";
        for (int i = 0; i < 100; i++) {
            System.out.println(dataCacher.isExisted(key));
            Session session = dataCacher.getCacheSession(key);
            System.out.println(session);
            Thread.sleep(100);
        }
        
    }
}

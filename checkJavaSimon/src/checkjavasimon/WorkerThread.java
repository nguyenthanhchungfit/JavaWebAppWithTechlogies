/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkjavasimon;

/**
 *
 * @author cpu11165-local
 */

import org.javasimon.SimonManager;  
import org.javasimon.Split;  
import org.javasimon.Stopwatch;  

public class WorkerThread extends Thread{
    Stopwatch stopwatch = SimonManager.getStopwatch("use.multiThreaded");  
    Split split = null;  
      
    @Override  
    public void run()  
    {  
        for(int i = 0; i < 5; i++)  
        {  
            split = stopwatch.start();  
            doVitalTask();  
            split.stop();  
        }  
          
    }  
      
    private static void doVitalTask()  
    {  
        try  
        {  
            Thread.sleep(100);  
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
        }  
    }  
}

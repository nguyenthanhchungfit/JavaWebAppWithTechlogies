/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkjavasimon;

import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;

/**
 *
 * @author cpu11165-local
 */
public class CheckJavaSimon {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Stopwatch stopwatch = SimonManager.getStopwatch("use.basic");
        Split split = null;  
        for(int i = 0; i < 10; i++)  
        {  
            split = stopwatch.start();  
            doVitalTask();  
            split.stop();
            System.out.println("****" + split.runningFor());
        }  

        System.out.println(stopwatch.getCounter());
        System.out.println(stopwatch.getMax() / 1000 / 1000);       //Convert nano-secs to milli-secs  
        System.out.println(stopwatch.getMin() / 1000 / 1000);
        System.out.printf("%.2f%n", stopwatch.getMean() / 1024 / 1024);
        System.out.println(stopwatch.getTotal() / 1000 / 1000);
        
        System.out.println("***************************************");
        
        SimonManager.clear();
        stopwatch = SimonManager.getStopwatch("use.basic");
        for(int i = 0; i < 2; i++)  
        {  
            split = stopwatch.start();  
            doVitalTask();  
            split.stop();
            System.out.println("****" + split.runningFor());
        }
        
        System.out.println(stopwatch.getCounter());
        
    }

    private static void doVitalTask() {
        try  
        {  
            Thread.sleep(200);  
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
        }  

    }

}

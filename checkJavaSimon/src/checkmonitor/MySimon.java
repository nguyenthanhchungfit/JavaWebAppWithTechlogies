/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkmonitor;
import java.util.HashMap;
import java.util.Set;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;

/**
 *
 * @author cpu11165-local
 */
public class MySimon {
    public final String stop_si1_name = "simon1";
    public final String stop_si2_name = "simon2";
    static int i = 1;
    
    private HashMap<String, Stopwatch> swt = new HashMap<>();
    private HashMap<String, String> swtTest;
    
    private static MySimon obj = new MySimon();
    
    private MySimon(){
        System.out.println("Call constructor");
        swtTest = new HashMap<>();
    }
    
    public static MySimon getSimonObj(){
        return obj;
    }
    
    public Stopwatch getStopWatch(String name){
        Stopwatch sw = null;
        printCurrentHashState();
        if(swt.containsKey(name)){
            sw =  swt.get(name);
            System.out.println("GET HASH " + name + "***" + sw.toString());
        } else {
            System.out.println("PUT HASH " + name);
            sw = SimonManager.getStopwatch(name);
            swt.put(name, sw);
        }
        return sw;
    }
    
    public int count(String name){
        return 0;
    }
    
    public void clearStopwatch(){
        SimonManager.clear();
        swt.clear();
    }
    
    public void printCurrentHashState(){
        Set<String> keys = swtTest.keySet();
        System.out.print("Curent State: ");
        for(String key : keys){
            System.out.print(key + "\t");
        }
        System.out.println();
    }
    
    public void addNewTest(){
        swtTest.put(i+"", "a");
        i++;
    }
    
}

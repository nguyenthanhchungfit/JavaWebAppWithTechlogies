/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkmonitor;

import java.util.Scanner;
import org.javasimon.Stopwatch;

/**
 *
 * @author cpu11165-local
 */
public class Monitor {
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        MySimon mysimon = MySimon.getSimonObj();
        
        String cmd = "";
        while(true){    
            System.out.print("Enter cmd: ");
            cmd  = sc.nextLine();
            if(cmd.equals("quit")){
                break;
            }else if(cmd.equals("ok")){
                //Stopwatch sw1 = MySimon.getStopWatch(MySimon.stop_si1_name);
                //Stopwatch sw2 = MySimon.getStopWatch(MySimon.stop_si2_name);
                
                //System.out.println("sw1: " + sw1.getCounter()); 
                //System.out.println("sw2: " + sw2.getCounter());
                //MySimon.clearStopwatch();
                mysimon.printCurrentHashState();
            }
        }
    }
}

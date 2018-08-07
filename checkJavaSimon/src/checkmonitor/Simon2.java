/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkmonitor;

import java.util.Scanner;
import org.javasimon.Split;
import org.javasimon.Stopwatch;

/**
 *
 * @author cpu11165-local
 */
public class Simon2 {
    
    
    public static void main(String[] args) {
        
        MySimon mysimon = MySimon.getSimonObj();
        Stopwatch stopwatch = mysimon.getStopWatch(mysimon.stop_si2_name);
        
        int i = 1;
        Scanner sc = new Scanner(System.in);
        String cmd = "";
        while(true){
            Split split = stopwatch.start();
            System.out.print("Enter cmd: ");
            cmd  = sc.nextLine();
            if(cmd.equals("quit")){
                break;
            }else if(cmd.equals("test")){
                mysimon.addNewTest();
                mysimon.printCurrentHashState();
            }
            split.stop();
            System.out.println("Time " + i + " : " + split.runningFor());
        }
    }
}

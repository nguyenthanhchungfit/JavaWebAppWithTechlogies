/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threadsinjava;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author cpu11165-local
 */
public class ThreadsInJava {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(6);
        for (int i = 0; i < 10; i++) {
            Runnable worker = new WorkerThread("" + i);
            // call phương thức execute của ExecutorService
            executor.execute(worker);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
 
        System.out.println("Finished all threads");
    }
    
}

class Multi extends Thread{
    private String nameThread;
    
    public Multi(String nameThread){
        this.nameThread = nameThread;
    }

    public String getNameThread() {
        return nameThread;
    }

    public void setNameThread(String nameThread) {
        this.nameThread = nameThread;
    }

    @Override
    public void run() {
        for(int i = 0; i < 100; i++){
            System.out.println(nameThread + " : " +i);
        }
    }
}


class WorkerThread implements Runnable {
    private String message;
 
    public WorkerThread(String s) {
        this.message = s;
    }
 
    public void run() {
        System.out.println(Thread.currentThread().getName() 
                + " (Start) message = " + message);
        processMessage();
        System.out.println(Thread.currentThread().getName() 
                + " (End)");
    }
 
    private void processMessage() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


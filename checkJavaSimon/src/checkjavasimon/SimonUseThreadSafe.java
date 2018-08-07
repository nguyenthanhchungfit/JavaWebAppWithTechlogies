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
import java.util.ArrayList;

import org.javasimon.SimonManager;
import org.javasimon.Stopwatch;
import org.javasimon.StopwatchSample;

public class SimonUseThreadSafe {

    static ArrayList<WorkerThread> workerThreadList = new ArrayList<WorkerThread>();

    public static void main(String[] args) throws Exception {
        createWorkerThreads();

        Stopwatch stopwatch = SimonManager.getStopwatch("use.multiThreaded");
        StopwatchSample stopwatchSample = stopwatch.sample();
        for (int i = 0; i < 5; i++) {
            stopwatchSample = stopwatch.sample();

            System.out.println(stopwatchSample.getCounter());
            System.out.println(stopwatchSample.getMax() / 1024 / 1024);
            System.out.println(stopwatchSample.getMin() / 1024 / 1024);
            System.out.printf("%.2f%n", stopwatchSample.getMean() / 1024 / 1024);
            System.out.println(stopwatchSample.getTotal() / 1024 / 1024);
            Thread.sleep(100);
            System.out.println("***************************");
        }
    }

    private static void createWorkerThreads() {
        for (int i = 0; i < 5; i++) {
            WorkerThread workerThread = new WorkerThread();
            workerThreadList.add(workerThread);
            workerThread.start();
        }
    }

    private static void joinWorkerThreads() {
        for (int i = 0; i < workerThreadList.size(); i++) {
            try {
                workerThreadList.get(i).join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

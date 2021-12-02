package com.bolzano;

public class timer implements Runnable{
    public timer(){}

    public void run(){
        long startTime = System.currentTimeMillis();
        long endTime = startTime + 7200000L;

        while (System.currentTimeMillis() < endTime) {
            // Still within time threshold, wait a little longer
            try {
                Thread.sleep(500L);  // Sleep 1/2 second
            } catch (InterruptedException e) {
                // Someone woke us up during sleep, that's OK
            }
        }

        System.out.println("We apologise for the amount of time this application is taking to run, please wait for few moments longer.");
    }
}

package com.example.performancetestappnativeandroid;

public class Stopwatch {
    private long startTime;
    private long stopTime;

    public Stopwatch(long startTime) {
        this.startTime = startTime;
        this.stopTime = 0;
    }

    public void stop(long stopTime) {
        this.stopTime = stopTime;
    }

    public String getTimeString() {
        long returnTime = (stopTime == 0) ? 0 : (stopTime - startTime);
        return "" + returnTime;
    }

    public long getTimeLong() {
        long returnTime = (stopTime == 0) ? 0 : (stopTime - startTime);
        return stopTime - startTime;
    }
}

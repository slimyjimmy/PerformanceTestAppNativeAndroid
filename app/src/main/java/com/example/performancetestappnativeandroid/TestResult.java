package com.example.performancetestappnativeandroid;

public class TestResult {
    private double duration;
    private String message;

    public TestResult(double duration, String message) {
        this.duration = duration;
        this.message = message;
    }

    public double getDuration() {
        return duration;
    }

    public String getMessage() {
        return message;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

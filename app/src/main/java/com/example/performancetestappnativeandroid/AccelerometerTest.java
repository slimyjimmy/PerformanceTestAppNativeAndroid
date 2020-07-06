package com.example.performancetestappnativeandroid;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class AccelerometerTest extends Activity implements SensorEventListener {
    private static Stopwatch stopwatch;

    public static Stopwatch getStopwatch() {
        return stopwatch;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            ((SensorManager) getSystemService(Context.SENSOR_SERVICE)).unregisterListener(this);
            stopwatch.stop(System.nanoTime());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    class CustomException extends Exception {
        public CustomException(String errorMessage) {
            super(errorMessage);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            test();
        } catch (CustomException e) {
            e.printStackTrace();
        }
    }
    private void test() throws CustomException {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer == null) {
            throw new CustomException("moin");
        }
        stopwatch = new Stopwatch(System.nanoTime());
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }
}

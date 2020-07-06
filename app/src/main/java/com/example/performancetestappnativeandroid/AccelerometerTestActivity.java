package com.example.performancetestappnativeandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AccelerometerTestActivity extends AppCompatActivity implements SensorEventListener {

    private Stopwatch stopwatch;
    private ArrayList<TestResult> testResultList = new ArrayList<TestResult>();
    private int numberOfIterations;
    private int numberOfIterationsLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer_test);


        Button buttonStartBenchmark = findViewById(R.id.button_start_benchmark);
        buttonStartBenchmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testResultList.clear();
                EditText editTextNumberOfIterations = findViewById(R.id.editText_numberOfIterations);
                numberOfIterations = Integer.parseInt(editTextNumberOfIterations.getText().toString());
                numberOfIterationsLeft = numberOfIterations;
                test();
            }
        });

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            ((SensorManager) getSystemService(Context.SENSOR_SERVICE)).unregisterListener(this);
            stopwatch.stop(System.nanoTime());
            TestResult testResult = new TestResult(stopwatch.getTimeLong(), "Test finished successfully (x: " + sensorEvent.values[0]+ ", y: " + sensorEvent.values[1] + ", z: " + sensorEvent.values[2] + ")");
            testResultList.add(testResult);
            if (--numberOfIterationsLeft <= 0) {
                double durationSum = 0.0;
                for (int i = 0; i < testResultList.size(); i++) {
                    durationSum += testResultList.get(i).getDuration();
                }
                double durationAvg = durationSum / testResultList.size();
                testResultList.add(new TestResult(durationAvg, "ALL TESTS FINISHED SUCCESSFULLY AVERAGING"));
                updateTestResults();
            } else {
                test();
            }
        }
    }

    public void updateTestResults() {
        ListView listViewTestResults = findViewById(R.id.list_testResults);
        String[] testResultsArray = new String[testResultList.size()];
        for (int i = 0; i < testResultList.size(); i++) {
            testResultsArray[i] = testResultList.get(i).getMessage() + " in " + testResultList.get(i).getDuration() + "ns";
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, testResultsArray);
        listViewTestResults.setAdapter(adapter);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void test() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer == null) {
            System.out.println("Alarm");
        }
        stopwatch = new Stopwatch(System.nanoTime());
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }
}
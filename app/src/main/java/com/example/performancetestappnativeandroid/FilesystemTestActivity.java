package com.example.performancetestappnativeandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FilesystemTestActivity extends AppCompatActivity {

    private static final String FILENAME = "file_system_benchmark_2";

    private Stopwatch stopwatch;
    private ArrayList<TestResult> testResultList = new ArrayList<TestResult>();
    private int numberOfIterations;
    private int numberOfIterationsLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filesystem_test);
        initalizeResource();
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

    private void initalizeResource() {
        FileOutputStream outputStream;

        try {
            InputStream res = this.getResources().openRawResource(R.raw.file_system_benchmark_2);
            Bitmap bm = BitmapFactory.decodeStream(res);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100 , baos);
            byte[] b = baos.toByteArray();
            String encoded = Base64.encodeToString(b, Base64.DEFAULT);

            outputStream = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            outputStream.write(encoded.getBytes());
            outputStream.close();
            res.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void test() {
        stopwatch = new Stopwatch(System.nanoTime());
        String ret = "";
        InputStream inputStream = null;
        try {
            // Load file content to string
            inputStream = getApplicationContext().openFileInput(FILENAME);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }
                ret = stringBuilder.toString();
            }

            // Decode base64 string
            byte[] image = Base64.decode(ret, Base64.DEFAULT);
            stopwatch.stop(System.nanoTime());
            //benchmarkFragment.onBenchmarkCompleted("Image loaded");
            testResultList.add(new TestResult(stopwatch.getTimeLong(), "Test finished successfully"));
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
        } catch (FileNotFoundException e) {
            //benchmarkFragment.onBenchmarkFailed("File not found!");
            testResultList.clear();
            testResultList.add(new TestResult(0.0, "TEST COULDN'T BE FINISHED: FILE NOT FOUND"));
        } catch (IOException e) {
            testResultList.clear();
            testResultList.add(new TestResult(0.0, "TEST COULDN'T BE FINISHED: CAN NOT READ FILE"));
//            benchmarkFragment.onBenchmarkFailed("Can not read file: " + e.toString());
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
//                    benchmarkFragment.onBenchmarkFailed("Failed closing file: " + e.toString());
                    testResultList.clear();
                    testResultList.add(new TestResult(0.0, "TEST COULDN'T BE FINISHED: FAILED CLOSING FILE"));
                }
            }
        }
    }

    private void updateTestResults() {
        ListView listViewTestResults = findViewById(R.id.list_testResults);
        String[] testResultsArray = new String[testResultList.size()];
        for (int i = 0; i < testResultList.size(); i++) {
            testResultsArray[i] = testResultList.get(i).getMessage() + " in " + testResultList.get(i).getDuration() + "ns";
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, testResultsArray);
        listViewTestResults.setAdapter(adapter);
    }
}
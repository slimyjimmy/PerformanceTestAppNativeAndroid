package com.example.performancetestappnativeandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

public class GeolocationTestActivity extends AppCompatActivity {
    private Stopwatch stopwatch;
    public static final int BENCHMARK_PERMISSION_ACCESS_FINE_LOCATION = 1;

    private static final long MIN_TIME_BETWEEN_UPDATES = 0; // 0 sec

    private FusedLocationProviderClient mFusedLocationClient;
    private Location mCurrentLocation;
    private LocationCallback mLocationCallback;


    private ArrayList<TestResult> testResultList = new ArrayList<TestResult>();
    private int numberOfIterations;
    private int numberOfIterationsLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geolocation_test);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( this, new String[] {  Manifest.permission.ACCESS_FINE_LOCATION  },
                    BENCHMARK_PERMISSION_ACCESS_FINE_LOCATION );
        } else {
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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        test();
    }

    private void test() {
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        stopwatch = new Stopwatch(System.nanoTime());

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(MIN_TIME_BETWEEN_UPDATES);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setNumUpdates(1);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult result) {
                super.onLocationResult(result);
                mCurrentLocation = result.getLocations().get(0);
                mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                if(mCurrentLocation != null) {
                    stopwatch.stop(System.nanoTime());
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
                }
            }
        };

        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
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
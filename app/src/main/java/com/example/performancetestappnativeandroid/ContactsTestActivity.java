package com.example.performancetestappnativeandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactsTestActivity extends AppCompatActivity {

    public static final int BENCHMARK_PERMISSION_READ_CONTACTS = 1;
    public static final int BENCHMARK_PERMISSION_WRITE_CONTACTS = 2;
    private Stopwatch stopwatch;
    private ArrayList<TestResult> testResultList = new ArrayList<TestResult>();
    private int numberOfIterations;
    private int numberOfIterationsLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_test);
        boolean bothGranted = false;
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.READ_CONTACTS ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.READ_CONTACTS  },
                    BENCHMARK_PERMISSION_READ_CONTACTS );
        } else {
            bothGranted = true;
        }
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.WRITE_CONTACTS ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.WRITE_CONTACTS  },
                    BENCHMARK_PERMISSION_WRITE_CONTACTS );
        } else {
            bothGranted = true;
        }
        if (bothGranted) {
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
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        test();
    }

    private void test() {
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.READ_CONTACTS ) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        stopwatch = new Stopwatch(System.nanoTime());
        ContentValues contentValues = new ContentValues();
        Uri rawContactUri = getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, contentValues);
        long rawContactId = ContentUris.parseId(rawContactUri);

        ContentValues[] contactDetails = new ContentValues[2];
        contentValues = new ContentValues();
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        contentValues.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, "mobile" + ((int)(Math.random() * 1000)));
        contactDetails[0] = contentValues;

        ContentValues contentValuesPhone = new ContentValues();
        contentValuesPhone.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        contentValuesPhone.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        contentValuesPhone.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME);
        contentValuesPhone.put(ContactsContract.CommonDataKinds.Phone.NUMBER, "123456789");
        contactDetails[1] = contentValuesPhone;

        getContentResolver().bulkInsert(ContactsContract.Data.CONTENT_URI, contactDetails);

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
package com.example.performancetestappnativeandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.gson.internal.$Gson$Preconditions;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setEventListeners();
    }

    private void setEventListeners() {
        /*Button buttonRedditTest = findViewById(R.id.button_reddit_test);
        buttonRedditTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RedditTestActivity.class);
                startActivityForResult(intent, 1);
            }
        });*/

        Button buttonAccelerometerTest = findViewById(R.id.button_accelerometer_test);
        buttonAccelerometerTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AccelerometerTestActivity.class);
                startActivity(intent);
            }
        });

        Button buttonContactsTest = findViewById(R.id.button_contacts_test);
        buttonContactsTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ContactsTestActivity.class);
                startActivity(intent);
            }
        });

        Button buttonGeolocationTest = findViewById(R.id.button_geolocation_test);
        buttonGeolocationTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GeolocationTestActivity.class);
                startActivity(intent);
            }
        });

        Button buttonFileSystemTest = findViewById(R.id.button_filesystem_test);
        buttonFileSystemTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FilesystemTestActivity.class);
                startActivity(intent);
            }
        });
    }
}
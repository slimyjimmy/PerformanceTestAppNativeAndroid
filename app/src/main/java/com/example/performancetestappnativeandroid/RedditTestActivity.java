package com.example.performancetestappnativeandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class RedditTestActivity extends AppCompatActivity {

    private static String url = "https://www.reddit.com/r/JorjaSmith/.json";
    public JsonObject jsonObject;
    //public URLConnection request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reddit_test);

        JsonObject jsonObject = null;
        try {
            getJsonFromURL(url);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /*String[] pictureURLs = null;
        try {
            pictureURLs = getPictureURLSFromJson(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Bitmap[] pictures = getPicturesFromURLS(pictureURLs);
        displayPictures(pictures);*/
    }


    private void getJsonFromURL(final String stringUrl) throws IOException, JSONException {
        /*final CustomAsyncTask asyncTask = new CustomAsyncTask();
        Thread newThread = new Thread(new Runnable() {
            @Override
            public void run() {
                JsonObject jsonObject = asyncTask.execute(stringUrl).get();
            }
        }).start();*/
        CustomAsyncTask asyncTask = new CustomAsyncTask();
        asyncTask.execute(stringUrl);
        /*URL url = null;
        url = new URL(stringUrl);
        request = url.openConnection();
        request.connect();


        // Convert to a JSON object to print data
        JsonParser jp = new JsonParser(); //from gson
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
        JsonObject rootobj = root.getAsJsonObject();
        return rootobj;*/
    }


    private  String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }


    private String[] getPictureURLSFromJson(JsonObject jsonObject) throws JSONException {
        JsonObject data = (JsonObject) jsonObject.get("data");
        JsonArray children = (JsonArray) data.get("children");
        String[] pictureURLS = new String[children.size()];
        for (int i = 0; i < children.size(); i++) {
            JsonObject child = (JsonObject) children.get(i);
            JsonObject data2 = (JsonObject) child.get("data");
            JsonObject url = (JsonObject) data2.get("URL");
            pictureURLS[i] = url.getAsString();
        }
        return pictureURLS;
    }
    private Bitmap[] getPicturesFromURLS(String[] pictureURLs) {
        Bitmap[] pictures = new Bitmap[pictureURLs.length];
        for (int i = 0; i < pictureURLs.length; i++) {
            try {
                URL url = new URL(pictureURLs[i]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                pictures[i] = myBitmap;
            } catch (IOException e) {
                // Log exception
                System.out.println("11111111111111111111111!!1ALARM!");
            }
        }
        return pictures;
    }
    private void displayPictures(Bitmap[] pictures) {
        ListView listView = findViewById(R.id.listView_images);
        listView.setAdapter(new CustomAdapter(this, pictures));
    }


    private class CustomAsyncTask extends AsyncTask<String, Void, Bitmap[]> {

        @Override
        protected Bitmap[] doInBackground(String... urlsAsStrings) {
            String urlAsString = urlsAsStrings[0];
            URLConnection request = null;
            URL url = null;
            try {
                url = new URL(urlAsString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                request = url.openConnection();
                request.setConnectTimeout(5000);
                request.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }


            // Convert to a JSON object to print data
            JsonParser jp = new JsonParser(); //from gson
            JsonElement root = null; //Convert the input stream to a json element
            try {
                root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            JsonObject rootobj = root.getAsJsonObject();
            jsonObject = rootobj;

            //teil2
            String[] pictureURLs = null;
            try {
                pictureURLs = getPictureURLSFromJson(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //teil3
            Bitmap[] pictures = getPicturesFromURLS(pictureURLs);
            return pictures;
            //return 0;
            //return rootobj;
        }

        @Override
        protected void onPostExecute(Bitmap[] bitmaps) {
            super.onPostExecute(bitmaps);
            ListView listView = findViewById(R.id.listView_images);
            listView.setAdapter(new CustomAdapter(RedditTestActivity.this, bitmaps));
        }
    }
}
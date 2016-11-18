package hu.eke.colorlist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private final static String JSON_URL = "https://adobe.github.io/Spry/data/json/array-02.js";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onLoadClick(View view) {
        // Start download in background
        downloadJson();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Cancel download if it is running
    }

    // Gradle-ben lévő useLibrary nélkül már nem is elérhető 23 feletti target mellett
    // infó: https://developer.android.com/about/versions/marshmallow/android-6.0-changes.html#behavior-apache-http-client
    private String downloadJsonLegacy() {
        String jsonResponse = null;
        HttpClient httpclient = new DefaultHttpClient();
        try {
            HttpGet httpGet = new HttpGet(JSON_URL);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            jsonResponse = httpclient.execute(httpGet,
                    responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpclient.getConnectionManager().shutdown();
        }

        return jsonResponse;
    }

    // Ez az új preferált módja a netes kommunikációnak
    private String downloadJson() {
        String jsonResponse = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(JSON_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            // Read the whole InputStream
            Scanner s = new Scanner(in).useDelimiter("\\A");
            jsonResponse = s.hasNext() ? s.next() : null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return jsonResponse;
    }

    private ArrayList<ColorItem> parseJson(String jsonString) {
        ArrayList<ColorItem> items = null;
        try {
            Gson gson = new Gson();
            Type collectionType = new TypeToken<ArrayList<ColorItem>>() {
            }.getType();
            items = gson.fromJson(jsonString, collectionType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }
}

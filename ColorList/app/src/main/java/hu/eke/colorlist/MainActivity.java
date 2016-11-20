package hu.eke.colorlist;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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

    ArrayList<ColorItem> colorArray;
    ColorItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Storage storage = new Storage(this);
        TextView textView = (TextView) findViewById(R.id.hello_text);
        textView.setText(storage.getEmail());
        Button logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storage.clear();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Adat
        colorArray = new ArrayList<>();
        colorArray.add(new ColorItem("Piros", "#ff0000"));
        colorArray.add(new ColorItem("Fehér", "#ffffff"));
        colorArray.add(new ColorItem("Zöld", "#00ff00"));

        // Adapter
        adapter = new ColorItemAdapter(this, colorArray);

        // AdapterView
        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
    }

    public void onLoadClick(View view) {
        // Start download in background
        GetColorsTask task = new GetColorsTask();
        task.execute();
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

    private class GetColorsTask extends AsyncTask<URL, Integer, String> {
        protected String doInBackground(URL... urls) {
            /*int count = urls.length;
            long totalSize = 0;
            for (int i = 0; i < count; i++) {
                totalSize += Downloader.downloadFile(urls[i]);
                publishProgress((int) ((i / (float) count) * 100));
                // Escape early if cancel() is called
                if (isCancelled()) break;
            }*/
            return downloadJson();
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(String result) {
            Log.v("Download", result);
            ArrayList<ColorItem> newItems = parseJson(result);
            colorArray.addAll(newItems);
            adapter.notifyDataSetChanged();
        }
    }
}

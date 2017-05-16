package com.example.jek.classworkfourmay;


import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

/**
 * Created by jek on 04.05.2017.
 */

public class MyAsyncTask extends AsyncTask<Integer, Integer, Void> {
    public static final String TAG = MyAsyncTask.class.getSimpleName();
    private MainActivity ma;

    public MyAsyncTask(String s, MainActivity ma) {
        Log.d(TAG, "AsyncTask creadet" + s);
        this.ma = ma;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d(TAG, "onPreExecute " + Thread.currentThread().getName());
        ma.showProgressBar();
    }

    @Override
    protected void onPostExecute(Void v) {
        super.onPostExecute(v);
        Log.d(TAG, "onPostExecute " + Thread.currentThread().getName());
        ma.hideProgressBar();
       /* ma.onUserRecieved(user);*/
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Void doInBackground(Integer[] params) {
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL("https://en.wikipedia.org/w/api.php?action=query&titles=Android&prop=revisions&rvprop=content&format=json");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.getResponseCode();
            Log.d(TAG, "code: " + httpURLConnection.getResponseCode());
            String response = handleResponse(httpURLConnection.getInputStream());
            Log.d(TAG, "response = " + response);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String handleResponse (InputStream inputStream){
        StringBuffer buffer = new StringBuffer();
        if (inputStream == null) {
            return null;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
}
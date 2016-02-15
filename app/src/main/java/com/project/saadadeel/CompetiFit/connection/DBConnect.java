package com.project.saadadeel.CompetiFit.connection;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by saadadeel on 01/02/2016.
 */
public class DBConnect extends AsyncTask<String, Void, Void> {

    User usr;

    public DBConnect(){
    }

    @Override
    protected Void doInBackground(String... params) {
        System.out.println("//////////////////////////////////////");
        String data = null;
        try {
            data = getData("http://178.62.68.172:32787/serverTest", 3000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("CONNECTED");
        this.usr = new Gson().fromJson(data, User.class);
        return null;
    }

    private User getUser(){
        return this.usr;
    }

    public String getData(String u, int timeout) throws IOException {

        URL url = new URL("http://178.62.68.172:32787/serverTest");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-length", "0");
            urlConnection.setUseCaches(false);
            urlConnection.setAllowUserInteraction(false);
            urlConnection.setConnectTimeout(timeout);
            urlConnection.setReadTimeout(timeout);
            urlConnection.connect();
            int status = urlConnection.getResponseCode();

            switch (status) {
                case 200:
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    br.close();
                    return sb.toString();
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (urlConnection != null) {
                try {
                    urlConnection.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    return null;
    }
}

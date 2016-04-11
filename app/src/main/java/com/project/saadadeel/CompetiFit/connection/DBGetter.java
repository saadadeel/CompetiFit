package com.project.saadadeel.CompetiFit.connection;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Base64;

import com.google.gson.Gson;
import com.project.saadadeel.CompetiFit.Models.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by saadadeel on 22/03/2016.
 */
public class DBGetter extends AsyncTask<String, Void, String> {
    String data;
    String params;
    public DBResponse delegate = null;
    User usr;
    String token = " ";

    public DBGetter(String params, String token) {
        this.params = params;
        this.token = token;
    }
    private String getParams(){
        return this.params;
    }

    @Override
    protected String doInBackground(String... params) {
        String data = null;
        try {
            data = getData("http://178.62.68.172:32900" + getParams(), 3000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("CONNECTED to DBGetter");
//        this.usr = new Gson().fromJson(data, User.class);
        return data;
    }

    protected void onPostExecute(String data){
        delegate.processFinish(data);
    }

    public String getData(String u, int timeout) throws IOException {
//
//        String credentials = usr.getUsername()+ ":" + usr.getUserPassword();

        String Auth ="Basic "+ token;
        System.out.println(Auth);
        URL url = new URL(u);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-length", "0");
            urlConnection.setRequestProperty("Authorization", Auth);
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
                    System.out.println(sb);
                    return sb.toString();

                case 400:
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder sb1 = new StringBuilder();
                    String line1;
                    while ((line1 = br1.readLine()) != null) {
                        sb1.append(line1+"\n");
                    }
                    br1.close();
                    return sb1.toString();
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
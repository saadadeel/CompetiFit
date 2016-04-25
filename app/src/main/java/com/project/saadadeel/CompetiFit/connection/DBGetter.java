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
    public DBGetter(String params){
        this.params = params;
    }
    private String getParams(){
        return this.params;
    }

    @Override
    protected String doInBackground(String... params) {
        String data = null;
        try {
            data = getData("http://178.62.68.172:32917" + getParams(), 10 * 1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    protected void onPostExecute(String data){
        delegate.processFinish(data);
    }

    public String getData(String u, int timeout) throws IOException {

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
            int responseCode = urlConnection.getResponseCode();

                if(responseCode==200){
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;

                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    br.close();
                    System.out.print(sb.toString());
                    return sb.toString();
                }else if (responseCode==400){
                    return "error";
                }else{
                    return "no data found";
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
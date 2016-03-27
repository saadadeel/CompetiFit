package com.project.saadadeel.CompetiFit.connection;

import android.os.AsyncTask;

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
public class DBGetter extends AsyncTask<String, Void, User> {
    String data;
    String params;
    public DBResponse delegate = null;
    public DBGetter(String params) {
        this.params = params;
    }
    User usr;

    private String getParams(){
        return this.params;
    }

    @Override
    protected User doInBackground(String... params) {
        String data = null;
        try {
            data = getData("http://178.62.68.172:32852" + getParams(), 3000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("CONNECTED to DBGetter");
        this.usr = new Gson().fromJson(data, User.class);
        return this.usr;
    }

    protected void onPostExecute(User user){
        delegate.processFinish(user);
    }

    public String getData(String u, int timeout) throws IOException {

        URL url = new URL(u);
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
                    System.out.println(sb);
                    return sb.toString();

                case 0001:
                    return "password incorrect";
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
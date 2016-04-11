package com.project.saadadeel.CompetiFit.connection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.project.saadadeel.CompetiFit.Models.Races;
import com.project.saadadeel.CompetiFit.Models.Runs;
import com.project.saadadeel.CompetiFit.Models.User;
import com.project.saadadeel.CompetiFit.UserMain;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import android.util.Base64;

/**
 * Created by saadadeel on 01/02/2016.
 */
public class DBConnect implements DBResponse{
    public User user;
    boolean haveUser;
    String params;
    boolean taskDone = false;
    JSONObject custom;
    boolean haveObject = false;
    Races race;
    boolean haveRace = false;
    Runs run;
    boolean haveRun = false;

    String token = "";

    public DBConnect(){}

    public DBConnect(User u, String token) {
        this.user = u;
        this.haveUser = true;

        this.token = token;
    }
    public DBConnect(JSONObject o, String token){
        this.custom = o;
        this.haveObject = true;

        this.token = token;
    }
    public DBConnect(Races race, String token){
        this.race = race;
        this.haveRace = true;

        this.token = token;
    }

    public DBConnect(Runs r, String token){
        this.run = r;
        this.haveRun = true;

        this.token = token;
    }

    public User post(String params){
        this.setParams(params);
        new post().execute();
        return this.user;
    }

    public void setParams(String p){
        this.params = p;
    }

    public String getParams(){
        return this.params;
    }

    public User getUser(){
        return this.user;
    }
    public void setUser(User u){this.user = u;}

    public void setTaskDone(Boolean td){
        this.taskDone = td;
    }

    public Boolean getTaskDone(){
       return this.taskDone;
    }

    public User getResult(){
        return this.user;
    }

    @Override
    public void processFinish(String data) {
        this.user=new Gson().fromJson(data, User.class);
        setTaskDone(true);
    }

    class post extends AsyncTask<String, Void, String> {

        private Boolean taskComplete;

        public post(){
        }

        @Override
        protected void onPreExecute() {
            System.out.println("Task is in pre execute");
        }

        @Override
        protected String doInBackground(String... params) {
            System.out.println("//////////////////////////////////////");
            Boolean loggedIn = null;
            try {
                loggedIn = postData("http://178.62.68.172:32900" + getParams(), 8000);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("CONNECTED to Sign Up");
            System.out.println("///////////////// ");
            System.out.println(loggedIn);

            return null;
        }

        protected void onPostExecute(String test){
            System.out.println("Task is Done");

        }

        public Boolean postData(String u, int timeout) throws IOException {

            URL url = new URL(u);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            String credentials = user.getUsername()+ ":" + user.getUserPassword();
            String Auth ="Basic "+ token;

            try {
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty ("Authorization", Auth);
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setUseCaches(false);
                urlConnection.setAllowUserInteraction(false);
                urlConnection.setConnectTimeout(timeout);
                urlConnection.setReadTimeout(timeout);

                if(haveObject) {
                    OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                    wr.write(custom.toString());
                    wr.flush();
                }
                if(haveUser){
                    Gson h = new Gson();
                    OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                    wr.write(h.toJson(user));
                    wr.flush();
                }
                if(haveRace){
                    Gson h = new Gson();
                    OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                    wr.write(h.toJson(race));
                    wr.flush();
                }
                if(haveRun){
                    Gson h = new Gson();
                    OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                    wr.write(h.toJson(run));
                    wr.flush();
                    System.out.println("This is a run model");
                }

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
                        System.out.print(sb.toString());
                        return true;

                    case 400:
                        return false;
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
}

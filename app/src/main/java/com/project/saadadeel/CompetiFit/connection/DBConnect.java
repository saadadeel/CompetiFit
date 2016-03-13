package com.project.saadadeel.CompetiFit.connection;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.project.saadadeel.CompetiFit.Models.Races;
import com.project.saadadeel.CompetiFit.Models.User;

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

    public DBConnect(){}

    public DBConnect(User u) {
        this.user = u;
        this.haveUser = true;
    }
    public DBConnect(JSONObject o){
        this.custom = o;
        this.haveObject = true;
    }
    public DBConnect(Races race){
        this.race = race;
        this.haveRace = true;
    }

    public void get(String params) {
        this.setParams(params);
        getter g = new getter();
        g.delegate = this;
        g.execute();
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
    public void processFinish(User u) {
        this.user = u;
        setTaskDone(true);
    }

    class getter extends AsyncTask<String, Void, String> {
        String data;
        public DBResponse delegate = null;
        public getter() {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            System.out.println("//////////////////////////////////////");
            try {
                data = getData("http://178.62.68.172:32824" + getParams(), 3000);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("CONNECTED");
            return null;
        }

        protected void onPostExecute(String test){
            User result = new Gson().fromJson(data, User.class);
            setTaskDone(true);
            setUser(result);
            System.out.println(taskDone);
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
                            sb.append(line + "\n");
                        }
                        br.close();
                        System.out.println("Inside data" + sb.toString());
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

    ////////////////////////////////////////////////////

    class post extends AsyncTask<String, Void, String> {

        private Boolean taskComplete;

        public post(){
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            System.out.println("//////////////////////////////////////");
            Boolean loggedIn = null;
            try {
                loggedIn = postData("http://178.62.68.172:32838" + getParams(), 8000);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("CONNECTED to Sign Up");
            System.out.println("///////////////// ");
            System.out.println(loggedIn);

            return null;
        }

        protected void onPostExecute(String test){
        }

        public Boolean postData(String u, int timeout) throws IOException {

            URL url = new URL(u);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
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
                    System.out.println("yooo");
                    wr.flush();
                }
                if(haveRace){
                    Gson h = new Gson();
                    OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                    wr.write(h.toJson(race));
                    wr.flush();
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

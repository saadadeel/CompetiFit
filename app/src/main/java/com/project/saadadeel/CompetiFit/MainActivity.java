package com.project.saadadeel.CompetiFit;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.project.saadadeel.CompetiFit.connection.DBConnect;
import com.project.saadadeel.CompetiFit.connection.DBResponse;
import com.project.saadadeel.CompetiFit.connection.User;

import org.json.JSONException;
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

public class MainActivity extends AppCompatActivity implements DBResponse{

    public String username;
    public String password;
    public Boolean auth;
    public User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void nextPage(View view) {
        // Handle event when Sign in Button is clicked
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);

    }

    public void mainPage(View view) {
        // Handle event when Login Button is clicked
        EditText Uname = (EditText)findViewById(R.id.emailInput);
        this.username = Uname.getText().toString();

        System.out.println(this.username + "//////////////");

        EditText Pword = (EditText)findViewById(R.id.passwordInput);
        this.password = Pword.getText().toString();

        new login().execute();
    }

    public void showMain(){
        Intent intent = new Intent(this, UserMain.class);
        intent.putExtra("username", this.username);
        startActivity(intent);
    }

    public String getUsername(){return this.username;}

    public String getPassword(){return this.password;}

    public void setPermissionAllowed(){this.auth = true;}

    public void setPermissionDenied(){this.auth = false;}

    @Override
    public void processFinish(User u) {

    }

    ///////////////// Connect to Database /////////////////////////

   class login extends AsyncTask<String, Void, String> {

        User usr;

        public login(){
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            System.out.println("//////////////////////////////////////");
            Boolean loggedIn = null;
            try {
                loggedIn = postData("http://178.62.68.172:32821/login/submit", 8000);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("CONNECTED");
            System.out.println("///////////////// " + getUsername() + "    " + getPassword());
            System.out.println(loggedIn);

            if(loggedIn){
                setPermissionAllowed();
            }else{setPermissionDenied();}
            return null;
        }

        protected void onPostExecute(String test){
            if(auth) {
                showMain();
            }else{

            }
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

                JSONObject cred   = new JSONObject();

                cred.put("username", getUsername());
                cred.put("password", getPassword());

                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(cred.toString());
                wr.flush();

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
            } catch (JSONException e) {
                e.printStackTrace();
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

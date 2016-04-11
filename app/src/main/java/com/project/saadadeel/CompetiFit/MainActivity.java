package com.project.saadadeel.CompetiFit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.project.saadadeel.CompetiFit.Models.User;

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

public class MainActivity extends AppCompatActivity{

    public String username;
    public String password;
    public Boolean auth;
    public User user;
    public SharedPreferences sharedPreferences;
    public String myPref = "myPref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(myPref, Context.MODE_PRIVATE);

        if(sharedPreferences.getString("TOKEN",null)==null){
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        }
        else{
            this.auth = true;
//            if(isNetworkAvailable()) {
                Intent intent = new Intent(this, UserMain.class);
                intent.putExtra("username", this.username);
                startActivity(intent);
//            }else{
//                Toast.makeText(this, "No Internet Connection Available",
//                        Toast.LENGTH_LONG).show();
//            }
        }
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
        if(isNetworkAvailable()) {
            Intent intent = new Intent(this, SignUp.class);
            startActivity(intent);
        }else{
            Toast.makeText(this, "No Internet Connection Available",
                    Toast.LENGTH_LONG).show();
        }

    }

    public void mainPage(View view) {
        // Handle event when Login Button is clicked
        EditText Uname = (EditText)findViewById(R.id.emailInput);
        this.username = Uname.getText().toString();

        System.out.println(this.username + "//////////////");

        EditText Pword = (EditText)findViewById(R.id.passwordInput);
        this.password = Pword.getText().toString();

        if(isNetworkAvailable()){
            new login().execute();
        }else{
            Toast.makeText(this, "No Internet Connection Available",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void showMain(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("USERNAME", this.username);
        editor.putString("PASSWORD", this.password);
        editor.commit();

        Intent intent = new Intent(this, UserMain.class);
        intent.putExtra("username", this.username);
        startActivity(intent);
    }

    public void setAuthDenied(){
        TextView loginStatus = (TextView)findViewById(R.id.loginStatus);
        loginStatus.setText("Password incorrect");
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public String getUsername(){return this.username;}

    public String getPassword(){return this.password;}

    public void setPermissionAllowed(){this.auth = true;}

    public void setPermissionDenied(){this.auth = false;}

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
                loggedIn = postData("http://178.62.68.172:32900/login/submit", 8000);
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
                SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                String credentials = this.usr.getUsername() + ":" + password;
                String token = Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT);
                prefsEditor.putString("TOKEN", token);
                prefsEditor.commit();
                showMain();
            }else{
                setAuthDenied();
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
                        User user = new Gson().fromJson(sb.toString(), User.class);
                        this.usr = user;
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

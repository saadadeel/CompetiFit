package com.project.saadadeel.CompetiFit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.project.saadadeel.CompetiFit.Models.User;
import com.project.saadadeel.CompetiFit.connection.DBGetter;
import com.project.saadadeel.CompetiFit.connection.DBResponse;

public class login extends AppCompatActivity implements DBResponse{

    public String username;
    public String password;
    public Boolean auth;
    public User user;
    public SharedPreferences sharedPreferences;
    public String myPref = "myPref";
    String errorMessage = "";
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(myPref, Context.MODE_PRIVATE);
        if(sharedPreferences.getString("user",null)==null){
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        } else{
            this.username = sharedPreferences.getString("USERNAME", "");
            Intent intent = new Intent(context, UserMain.class);
            intent.putExtra("username", this.username);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    // Handle event when Sign in Button is clicked
    public void nextPage(View view) {
        if(isInternetAvailable()) {
            Intent intent = new Intent(this, SignUp.class);
            startActivity(intent);
        }else{
            Toast.makeText(context, "No Internet Connection Available", Toast.LENGTH_LONG).show();
        }
    }

    // Handle event when Login Button is clicked
    public void mainPage(View view) {
        EditText usernameInput = (EditText)findViewById(R.id.usernameInput);
        EditText passwordInput = (EditText)findViewById(R.id.passwordInput);
        this.username = usernameInput.getText().toString();
        this.password = passwordInput.getText().toString();

        if(isInternetAvailable()){
            DBGetter db = new DBGetter("/login/"+this.username+":"+this.password);
            db.delegate = login.this;
            db.execute();
        }else{
            Toast.makeText(this, "No Internet Connection Available",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void showMain(){
        Intent intent = new Intent(this, UserMain.class);
        intent.putExtra("username", this.username);
        startActivity(intent);
    }

    public void setAuthDenied(){
        TextView loginStatus = (TextView)findViewById(R.id.loginStatus);
        loginStatus.setText(errorMessage);
    }

    private boolean isInternetAvailable() {
        ConnectivityManager conManager =
                (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            return true;
        }
        return false;
    }

    public String getUsername(){return this.username;}

    @Override
    public void processFinish(String response) {
        response = response.trim();
        if(response.equals("error")) {
            errorMessage = "Password Incorrect";
            setAuthDenied();
        }
        else if(response.equals("no data found")) {
            errorMessage = "Incorrect Username";
            setAuthDenied();
        }
        else{
            SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
            String credentials = this.username + ":" + this.password;
            String token = Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT);
            prefsEditor.putString("TOKEN", token);
            prefsEditor.putString("USERNAME", this.username);
            prefsEditor.commit();

            showMain();
        }
    }
}

package com.project.saadadeel.CompetiFit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

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
        // Handle event when Login Button is clicked
        if (isLogin("email", "password")) {
            Intent intent = new Intent(this, SignUp.class);
            startActivity(intent);
        }
    }

    public void mainPage(View view) {
        // Handle event when Login Button is clicked
        if (isLogin("email", "password")) {
            Intent intent = new Intent(this, UserMain.class);
            startActivity(intent);
        }
    }

    public boolean isLogin(String username, String password) {
        // Handle event when Login Button is clicked
        if(username.equals("email") && password.equals("password")){
         return true;
        }
        else return false;
    }
}

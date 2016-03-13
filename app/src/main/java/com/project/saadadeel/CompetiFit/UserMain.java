package com.project.saadadeel.CompetiFit;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.project.saadadeel.CompetiFit.RunTracker.Pop;
import com.project.saadadeel.CompetiFit.ScrollingPageAdapter.ViewPagerAdapter;
import com.project.saadadeel.CompetiFit.Models.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UserMain extends AppCompatActivity {
    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"League","Activity","Races","Profile"};
    int Numboftabs =4;
    Timer timer = new Timer();

    String username;
    User usr;
    boolean isRefresher = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_user_main);

        Intent intent = getIntent();
        String username = intent.getExtras().getString("username");
        setUsername(username);

        new DB().execute();
        initiateRefresher();
    }

    public void initiateRefresher(){
        TimerTask secondCounter = new TimerTask() {
            @Override
            public void run() {
                isRefresher = true;
                System.out.println("update /////////////////// yo");
//                finish();
//                startActivity(getIntent());
                new DB().execute();
            }
        };
        timer.scheduleAtFixedRate(secondCounter, 3*60*1000, 3*60*1000);
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

    public void setUser(User u){this.usr = u;}

    public void setUsername(String un){this.username = un;}

    public String getUsername(){return this.username;}

    /////////////Database connector///////////

    class DB extends AsyncTask<String, Void, String> {

        User usr;

        public DB(){
        }

        @Override
        protected String doInBackground(String... params) {
            System.out.println("//////////////////////////////////////");

            String data = null;
            try {
                data = getData("http://178.62.68.172:32838/user/details/"+ getUsername(), 3000);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("CONNECTED");
            this.usr = new Gson().fromJson(data, User.class);
            System.out.println("////////////////////////" + usr.getUsername() + "///////////////");
            return null;
        }

        protected void onPostExecute(String test){
            setUser(usr);

            if(isRefresher){
                adapter.setArgs(usr);
            } else {
                // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
                adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs, usr);

                // Assigning ViewPager View and setting the adapter
                pager = (ViewPager) findViewById(R.id.pager);
                pager.setAdapter(adapter);

                // Assiging the Sliding Tab Layout View
                tabs = (SlidingTabLayout) findViewById(R.id.tabs);
                tabs.setDistributeEvenly(true);

                // Setting Custom Color for the Scroll bar indicator of the Tab View
                tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
                    @Override
                    public int getIndicatorColor(int position) {
                        return getResources().getColor(R.color.colorPrimary);
                    }
                });

                // Setting the ViewPager For the SlidingTabsLayout
                tabs.setViewPager(pager);

                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(UserMain.this, Pop.class);
                        intent.putExtra("user", usr);
                        intent.putExtra("runs", usr.getRuns());
                        intent.putExtra("isRace", false);
                        startActivity(intent);
                    }
                });
            }
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
}
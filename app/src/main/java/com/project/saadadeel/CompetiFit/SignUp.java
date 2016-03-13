package com.project.saadadeel.CompetiFit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.project.saadadeel.CompetiFit.connection.DBConnect;
import com.project.saadadeel.CompetiFit.Models.User;

public class SignUp extends AppCompatActivity {

    private User user;
    String username;
    String fName;
    String lName;
    String password;
    int level;
    Spinner spinner;
    Spinner spinner1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.spinner = (Spinner)findViewById(R.id.spinner);
        this.spinner1 = (Spinner)findViewById(R.id.spinner1);
        setSupportActionBar(toolbar);
    }

    public void nextPage(View view) {
        setUser();
        DBConnect db = new DBConnect(this.user);
        this.user = db.post("/user/signIn");
        showNextPage();
    }

    public void showNextPage() {
        Intent intent = new Intent(this, Tutorial.class);
        startActivity(intent);
    }

    public void setUser() {
        EditText Uname = (EditText) findViewById(R.id.usernameInput);
        this.username = Uname.getText().toString();

        EditText pWord = (EditText) findViewById(R.id.passwordInput);
        this.password = pWord.getText().toString();

        EditText Fname = (EditText) findViewById(R.id.fnameInput);
        this.fName = Fname.getText().toString();

        EditText Lname = (EditText) findViewById(R.id.lnameInput);
        this.lName = Lname.getText().toString();

        int dist = Integer.parseInt(spinner.getSelectedItem().toString());
        int time = Integer.parseInt(spinner1.getSelectedItem().toString());

        this.levelSetter(dist, time);

        this.user = new User(username, password, fName, lName, level);
    }

    public void levelSetter(int distance, int time){
        if(distance<=1){
            this.level = 2;
        }
        if(distance>1 && distance <=3){
            this.level = 4;
        }
    }
}



















//    class signUp extends AsyncTask<String, Void, String> {
//
//        User usr;
//
//        public signUp(){
//        }
//
//        @Override
//        protected void onPreExecute() {
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            System.out.println("//////////////////////////////////////");
//            Boolean loggedIn = null;
//            try {
//                loggedIn = postData("http://178.62.68.172:32816/user/signIn", 8000);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            System.out.println("CONNECTED to Sign Up");
//            System.out.println("///////////////// ");
//            System.out.println(loggedIn);
//
//            return null;
//        }
//
//        protected void onPostExecute(String test){
//            showNextPage();
//        }
//
//        public Boolean postData(String u, int timeout) throws IOException {
//
//            URL url = new URL(u);
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//
//            try {
//                urlConnection.setRequestMethod("POST");
//                urlConnection.setRequestProperty("Content-Type", "application/json");
//                urlConnection.setDoOutput(true);
//                urlConnection.setDoInput(true);
//                urlConnection.setUseCaches(false);
//                urlConnection.setAllowUserInteraction(false);
//                urlConnection.setConnectTimeout(timeout);
//                urlConnection.setReadTimeout(timeout);
//
//                JSONObject details   = new JSONObject();
//
//                details.put("username", user.getUsername());
//                details.put("password", user.getUserPassword());
//                details.put("firstName", user.getUserFirstName());
//                details.put("lastName", user.getUserLastName());
//
//                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
//                wr.write(details.toString());
//                wr.flush();
//
//                // urlConnection.connect();
//                int status = urlConnection.getResponseCode();
//
//
//                switch (status) {
//                    case 200:
//                        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//                        StringBuilder sb = new StringBuilder();
//                        String line;
//
//                        while ((line = br.readLine()) != null) {
//                            sb.append(line+"\n");
//                        }
//                        br.close();
//                        System.out.print(sb.toString());
//                        return true;
//
//                    case 400:
////                        while ((line = br.readLine()) != null) {
////                            sb.append(line+"\n");
////                        }
////                        br.close();
////                        System.out.print(sb.toString());
//                        return false;
//                }
//
//            } catch (MalformedURLException ex) {
//                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//            } catch (IOException ex) {
//                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } finally {
//                if (urlConnection != null) {
//                    try {
//                        urlConnection.disconnect();
//                    } catch (Exception ex) {
//                        Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//            }
//            return null;
//        }
//    }
//}

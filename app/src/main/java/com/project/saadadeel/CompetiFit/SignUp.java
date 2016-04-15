package com.project.saadadeel.CompetiFit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.project.saadadeel.CompetiFit.connection.DBConnect;
import com.project.saadadeel.CompetiFit.Models.User;
import com.project.saadadeel.CompetiFit.connection.DBResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUp extends AppCompatActivity implements DBResponse{

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

    public void nextPage(View view) throws JSONException {
        setUser();
//        JSONObject u = new JSONObject();
//        u.put("fname", this.user.getUserFirstName());
//        u.put("lname", this.user.getUserLastName());
//        u.put("username", this.user.getUsername());
//        u.put("pword", this.user.getUserFirstName());

        DBConnect db = new DBConnect(this.user, " ");
        db.delegate = this;
        this.user = db.post("/user/signIn");
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

    @Override
    public void processFinish(String data) {
        String m = "user exists";
        if(m.equals(data.trim())){
            Toast.makeText(this, "This username already exists",
                    Toast.LENGTH_LONG).show();
        }else{
            showNextPage();
        }
    }
}

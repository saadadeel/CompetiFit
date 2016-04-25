package com.project.saadadeel.CompetiFit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
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
    String usernameInput;
    String fNameInput;
    String lNameInput;
    String passwordInput;
    String repeatPasswordInput;
    Spinner spinner;
    Spinner spinner1;

    SharedPreferences sharedPreferences;

    @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void nextPage(View view) throws JSONException {
        setUser();

        if(this.usernameInput.equals("") || this.passwordInput.equals("")
                || this.repeatPasswordInput.equals("") || this.fNameInput.equals("") ||
                this.lNameInput.equals("")){
            Toast.makeText(this, "Please fill in all fields",
                    Toast.LENGTH_LONG).show();
        }
        else if(!this.passwordInput.equals(this.repeatPasswordInput)){
            Toast.makeText(this, "Passwords entered do not match",
                    Toast.LENGTH_LONG).show();
        } else{
            DBConnect db = new DBConnect(" ");
            db.delegate = this;
            db.post("/user",this.user);
        }
    }

    public void setUser() {
        this.spinner = (Spinner)findViewById(R.id.distanceInput);
        this.spinner1 = (Spinner)findViewById(R.id.timeInput);
        EditText Uname = (EditText) findViewById(R.id.usernameInput);
        EditText pWord = (EditText) findViewById(R.id.passwordInput);
        EditText repeatPWord = (EditText) findViewById(R.id.passwordRepeatInput);
        EditText Fname = (EditText) findViewById(R.id.fnameInput);
        EditText Lname = (EditText) findViewById(R.id.lnameInput);

        this.usernameInput = Uname.getText().toString();
        this.passwordInput = pWord.getText().toString();
        this.repeatPasswordInput = repeatPWord.getText().toString();
        this.fNameInput = Fname.getText().toString();
        this.lNameInput = Lname.getText().toString();

        double distanceInput = Double.parseDouble(spinner.getSelectedItem().toString());
        double timeInput = Double.parseDouble(spinner1.getSelectedItem().toString());

        double avgSpeed = distanceInput/(timeInput/60);

        int level = (int)(avgSpeed*distanceInput/10);
        if(level==0){
            level=1;
        }
        this.user = new User(usernameInput, passwordInput, fNameInput,
                lNameInput, level, distanceInput, avgSpeed);
    }

    @Override
    public void processFinish(String data) {
        if(data.trim().equals("user exists")){
            Toast.makeText(this, "This username already exists",
                    Toast.LENGTH_LONG).show();
        }else{
            SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
            String credentials = this.usernameInput + ":" + this.passwordInput;
            String token = Base64.encodeToString(credentials.getBytes(), Base64.DEFAULT);

            prefsEditor.putString("TOKEN", token);
            prefsEditor.putString("USERNAME", this.usernameInput);
            prefsEditor.commit();

            Intent intent = new Intent(this, Tutorial.class);
            startActivity(intent);
        }
    }
}
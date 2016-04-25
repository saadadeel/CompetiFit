package com.project.saadadeel.CompetiFit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.project.saadadeel.CompetiFit.RunTracker.Pop;

public class Tutorial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void goToMain(View view) {
        Intent intent = new Intent(Tutorial.this, UserMain.class);
        Tutorial.this.finish();
        startActivity(intent);
    }
}
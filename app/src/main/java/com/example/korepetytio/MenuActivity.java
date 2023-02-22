package com.example.korepetytio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void allTechers(View v) {
        startActivity(new Intent(this, AllTeachhersActivity.class));
    }

    public void profile(View v) {
        startActivity(new Intent(this, MyProfileActivity.class));
    }
}
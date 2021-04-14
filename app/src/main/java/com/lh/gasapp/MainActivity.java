package com.lh.gasapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.lh.gasapp.homeActivity.Home;
import com.lh.gasapp.login.saveLogin;

public class MainActivity extends AppCompatActivity {
    private saveLogin SaveSharedPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        if(SaveSharedPreference.getUserName(MainActivity.this).length() == 0)
//        {
//            startActivity(new Intent(getApplicationContext(), Login.class));
//        }
//        else
//        {
//            Intent homeIntent = new Intent(getApplicationContext(), Home.class);
//            startActivity(homeIntent);
//        }
        Intent homeIntent = new Intent(getApplicationContext(), Home.class);
        startActivity(homeIntent);
    }
}
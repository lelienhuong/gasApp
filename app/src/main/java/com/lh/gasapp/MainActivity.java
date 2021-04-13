package com.lh.gasapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lh.gasapp.firebaseWrapper.MyValueEventListener;
import com.lh.gasapp.login.Login;
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
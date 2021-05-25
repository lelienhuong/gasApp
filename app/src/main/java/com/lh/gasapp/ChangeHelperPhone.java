package com.lh.gasapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lh.gasapp.homeActivity.Home;
import com.lh.gasapp.login.saveLogin;
import com.lh.gasapp.notification.Alarm;

public class ChangeHelperPhone extends AppCompatActivity {
    EditText phone;
    Button button;
    private saveLogin SaveSharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_helper_phone);
        phone = findViewById(R.id.phoneHelper);
        button = findViewById(R.id.button1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        phone.setText(SaveSharedPreference.getPhoneHelper(ChangeHelperPhone.this));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phone.getText().toString().trim();
                SaveSharedPreference.setPhoneHelper(getApplicationContext(),phoneNumber);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
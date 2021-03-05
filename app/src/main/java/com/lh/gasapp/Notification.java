package com.lh.gasapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class Notification extends AppCompatActivity {
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.warning);
        mediaPlayer.start();

        // Important: have to do the following in order to show without unlocking
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        btn = findViewById(R.id.music_stop);
        Intent intent= getIntent();
        Double oldData = intent.getDoubleExtra("oldData",-1);
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                Home.isCheck = true;
                Intent intent = new Intent(getApplicationContext(), Home.class);
                intent.putExtra("oldData",oldData);
                startActivity(intent);
            }
        });
     }
}
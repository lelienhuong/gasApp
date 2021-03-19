package com.lh.gasapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.time.LocalTime;
import java.util.Date;

public class Notification extends AppCompatActivity {
    private Button btn;
    private static final int PERMISSION_REQUEST_CODE = 1;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
//        LocalTime timeStart = LocalTime.now();
//      Date date2 = null;
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.warning);

               mediaPlayer.start();

//        long diff = date2.getTime() - date.getTime();
//
//        long diffSeconds = diff / 1000;
//
//        long diffMinutes = diff / (60 * 1000);
//
//        long diffHours = diff / (60 * 60 * 1000);
         //   makeCall();
//        String dial = "tel:" + "+84764690776";
//        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
            // Important: have to do the following in order to show without unlocking

            btn = findViewById(R.id.music_stop);
            Intent intent = getIntent();
            Double oldData = intent.getDoubleExtra("oldData", -1);
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//            btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    mediaPlayer.stop();
//                    Home.isCheck = true;
//                    Intent intent = new Intent(getApplicationContext(), Home.class);
//                    intent.putExtra("oldData", oldData);
//                    startActivity(intent);
//                }
//            });
////            while(LocalTime.now().getMinute() - timeStart.getMinute() < 2) {
////                Log.d("HELLLOOO", "HIIIIIIIII");
////            }
////               makeCall();
////        int time = 5*3600; //trong 5 ph
////        try {
////            Thread.sleep(5*3600);
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
////        makeCall();
////            if(LocalTime.now().getMinute() - timeStart.getMinute() < 2){
////                finish();
////                Log.d("Huong","OKKKKKK");
////                startActivity(getIntent());
////            }else {
////                makeCall();
////            }
//            }
//        }, 60000);
//            makeCall();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                Home.isCheck = true;
                Intent intent = new Intent(getApplicationContext(), Home.class);
                intent.putExtra("oldData", oldData);
                startActivity(intent);
            }
        });
        CountDownTimer timer = new CountDownTimer(10000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                makeCall();
//                if (mediaPlayer.isPlaying()) {
//                    mediaPlayer.stop();
//                    Home.isCheck = true;
//                    Intent intent = new Intent(getApplicationContext(), Home.class);
//                    intent.putExtra("oldData",oldData);
//                    startActivity(intent);
//                    mediaPlayer.release();
//                }
            }
        };
        timer.start();
    }




    public void makeCall(){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + "0764690776"));
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        if (result == PackageManager.PERMISSION_GRANTED){
            startActivity(intent);
        } else {
            requestPermission();
        }
    }

    private void requestPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CALL_PHONE)){}
        else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeCall();
                }
                break;
        }
    }
}
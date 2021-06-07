package com.lh.gasapp.notification;

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
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.lh.gasapp.MainActivity;
import com.lh.gasapp.firebase.valueEventListener.MyValueEventListener;
import com.lh.gasapp.homeActivity.Home;
import com.lh.gasapp.R;
import com.lh.gasapp.login.saveLogin;

import java.util.ArrayList;

public class Alarm extends AppCompatActivity {
    public static Alarm instance = null;
    public static MediaPlayer mediaPlayer;

    private Button btn;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private saveLogin SaveSharedPreference;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
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

      boolean isRunningState  = getIntent().getBooleanExtra("stateRunningAlarm",false);
        mediaPlayer = MediaPlayer.create(this, R.raw.warning);
        mediaPlayer.start();
        btn = findViewById(R.id.music_stop);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Home.instance.finish();
                mediaPlayer.stop();
                long previousTime = System.currentTimeMillis();
                Intent intent = new Intent(getApplicationContext(), Home.class);
                intent.putExtra("previousTime", previousTime);
                intent.putExtra("stateRunningAlarm", isRunningState);
                intent.putIntegerArrayListExtra("gasValues", MyValueEventListener.gasValues);
                startActivity(intent);
                finish();
            }
        });

        CountDownTimer timer = new CountDownTimer(10000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                if (mediaPlayer.isPlaying()) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.SEND_SMS)
                                == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
                            String[] permissions = {Manifest.permission.SEND_SMS, Manifest.permission.CALL_PHONE};
                            requestPermissions(permissions, PERMISSION_REQUEST_CODE);
                        }else {
                            int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS);
                            if (result == PackageManager.PERMISSION_GRANTED) {
                                try {
                                    SmsManager smsManager = SmsManager.getDefault();
                                    String announceMessage = "";
                                    Boolean statusPeople = getIntent().getBooleanExtra("statePeople", false);
                                    if (statusPeople) {
                                        announceMessage = "Khí gas bị rò rỉ và hiện tại đang có người trong khu vực!";
                                    } else {
                                        announceMessage = "Khí gas bị rò rỉ và hiện tại chưa phát hiện người trong khu vực!";
                                    }
                                    smsManager.sendTextMessage(SaveSharedPreference.getPhoneHelper(getApplication()), null, announceMessage, null, null);
                                    makeCall();
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }

                            }

                        }
                    }
                }
            }
        };
        timer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
            }
        });
    }


    public void makeCall() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        String phoneHelper = SaveSharedPreference.getPhoneHelper(Alarm.this);
        intent.setData(Uri.parse("tel:" + phoneHelper));
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent);
        } else {
            requestPermission();
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
//                    SmsManager smsManager = SmsManager.getDefault();
//                    String announceMessage ="";
//                    Boolean statusPeople = getIntent().getBooleanExtra("statePeople",false);
//                    if (statusPeople) {
//                        announceMessage = "Khí gas bị rò rỉ và hiện tại đang có người trong khu vực!";
//                    } else {
//                        announceMessage = "Khí gas bị rò rỉ và hiện tại chưa phát hiện người trong khu vực!";
//                    }
//                    smsManager.sendTextMessage(SaveSharedPreference.getPhoneHelper(getApplication()), null, announceMessage, null, null);
                    makeCall();
                }
                break;

        }
    }
}
package com.lh.gasapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lh.gasapp.model.RasData;

import java.util.Calendar;


public class Home extends AppCompatActivity {
    public static boolean isCheck = false;
    public TextView tv_gas,tv_level,tv_people,tv_status;
    private double oldData = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tv_gas = findViewById(R.id.tv_gas);
        tv_level = findViewById(R.id.tv_level);
        tv_people = findViewById(R.id.tv_people);
        tv_status = findViewById(R.id.tv_status);
        Intent intent= getIntent();
        oldData = intent.getDoubleExtra("oldData",-1);
        Log.d("OLDDATAAAAAAA", String.valueOf(oldData));
        //them truong hop luc chua co sẵn dữ lieu
        FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RasData rasData = snapshot.getValue(RasData.class);
                if(oldData == -1 ) {
                    oldData = rasData.getGasData();
                    Log.d("ol", "ok");
                }else{
                    isCheck = true;
                }
                if(rasData.getGasData() != oldData){
                    isCheck = false;
                    oldData = rasData.getGasData();
                }

                if(rasData.getGasData() > 400 && isCheck == false){
                    Intent intent = new Intent(getApplicationContext(), com.lh.gasapp.Notification.class);
                    intent.putExtra("oldData", oldData);
                    startActivity(intent);
                    notificationDialog();
                }
                tv_gas.setText(String.valueOf(rasData.getGasData()));
                if(rasData.getGasData() > 400){
                    tv_level.setText("Nguy hiểm");
                }else{
                    tv_level.setText("Bình thường");
                }
                if(rasData.isPeople()){ //true la co nguoi
                    tv_people.setText("Có người trong khu vực");
                }else{
                    tv_people.setText("Không có người trong khu vực");
                }
                if(rasData.isStatusHuman()){ // true là binh thuong, false la co van de
                    tv_status.setText("Bình thường");
                }else{
                    tv_status.setText("Không bình thường");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void notificationDialog() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "tutorialspoint_01";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            // Configure the notification channel.
            notificationChannel.setDescription("Sample Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Tutorialspoint")
                //.setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("WARNING GAS LEAK!")
                .setContentText("Khí gas ở mức NGUY HIỂM ! ")
                .setContentInfo("Information");
        notificationManager.notify(1, notificationBuilder.build());
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }

}


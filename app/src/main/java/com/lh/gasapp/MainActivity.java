package com.lh.gasapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.lh.gasapp.login.Login;
import com.lh.gasapp.login.saveLogin;

public class MainActivity extends AppCompatActivity {
    EditText email, pass;
    TextView tv_register,sign_gg;
    Button btn_login;
    FirebaseAuth fAuth;
    private static final String TAG="";
    private int RC_SIGN_IN;
    private GoogleSignInClient mGoogleSignInClient;
    private saveLogin SaveSharedPreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(SaveSharedPreference.getUserName(MainActivity.this).length() == 0)
        {
            // call Login Activity
            startActivity(new Intent(getApplicationContext(), Login.class));
        }
        else
        {
            // Stay at the current activity.
            startActivity(new Intent(getApplicationContext(), Home.class));
        }
    }
}
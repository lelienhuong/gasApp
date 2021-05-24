package com.lh.gasapp.login;

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
import com.lh.gasapp.homeActivity.Home;
import com.lh.gasapp.R;
import com.lh.gasapp.SignUp;

public class Login extends AppCompatActivity {
    EditText textfield_email, textfield_password;
    TextView tv_register, sign_gg;
    Button button_login;
    FirebaseAuth fAuth;
    private static final String TAG = "";
    private int RC_SIGN_IN;
    private GoogleSignInClient mGoogleSignInClient;
    private saveLogin SaveSharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        fAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        add();
    }

    private void add() {
        textfield_email = findViewById(R.id.textfield_email);
        textfield_password = findViewById(R.id.textfield_password);
        fAuth = FirebaseAuth.getInstance();
        tv_register = findViewById(R.id.tv_register);
        button_login = findViewById(R.id.button_login);
        sign_gg = findViewById(R.id.sign_gg2);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = textfield_email.getText().toString();
                String password = textfield_password.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    textfield_email.setError("Require Input Email!!");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    textfield_password.setError("Require Input Password!!");
                    return;
                }
                login(email, password);
            }
        });
        sign_gg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, SignUp.class));
            }
        });
    }

    private void signIn() {
        Intent signIntent = mGoogleSignInClient.getSignInIntent();
        Log.d(TAG, "firebaseAuthWithGoogle:" + mGoogleSignInClient);
        startActivityForResult(signIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
            Toast.makeText(Login.this, "Signed In Successfully", Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(acc);
        } catch (ApiException e) {
            Log.d("LOI", String.valueOf(e));
            Toast.makeText(Login.this, "Signed In Failed", Toast.LENGTH_SHORT).show();
//            FirebaseGoogleAuth(null);
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acct) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        fAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Login.this, "Successful", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = fAuth.getCurrentUser();
                    startActivity(new Intent(getApplicationContext(), Home.class));
                } else {
                    Toast.makeText(Login.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void login(String email, String password) {
        fAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Login w/ correct information
                            FirebaseUser user = fAuth.getCurrentUser();
                            SaveSharedPreference.setUserName(getApplicationContext(), FirebaseAuth.getInstance().getUid());
                            startActivity(new Intent(getApplicationContext(), Home.class));
                            finish();
                            // TODO: update UI when success sign in
                        } else {
                            // login fail
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

}
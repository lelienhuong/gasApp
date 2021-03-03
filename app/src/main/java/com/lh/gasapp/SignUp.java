package com.lh.gasapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import static android.widget.Toast.LENGTH_SHORT;


public class SignUp extends AppCompatActivity {
    private FirebaseAuth firebaseAuth,mAuth;
    private static final String TAG="";
    private final  static  int RC_SING_IN=111;
    private GoogleSignInClient mGoogleSignInClient;
    EditText name,pass,email;
    TextView tv_login,sign_gg;
    Button btn_re;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        addControls();
        request();
        addEvents();
    }

    private void request() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void addEvents() {
//        if(firebaseAuth.getCurrentUser()!=null){
//            startActivity(new Intent(SignUp.this,Home.class));
//            finish();
//        }
        btn_re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_1=email.getText().toString().trim();
                String pass_1=pass.getText().toString().trim();
                Log.d("TAG", email_1);
                if(TextUtils.isEmpty(email_1)){
                    email.setError("Require Input Email!!");
                    return;
                }
                if(TextUtils.isEmpty(pass_1)){
                    pass.setError("Require Input Password!!");
                    return;
                }
                firebaseAuth.createUserWithEmailAndPassword(email_1,pass_1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignUp.this,"Create User", LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Home.class));
                        }
                        else{
                            Toast.makeText(SignUp.this, "Error "+task.getException() , Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        sign_gg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    private void signIn() {
        Intent signIntent = mGoogleSignInClient.getSignInIntent();
        Log.d(TAG, "firebaseAuthWithGoogle:"+mGoogleSignInClient);
        startActivityForResult(signIntent,RC_SING_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SING_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId()+"Success");
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(SignUp.this, "Login Google Success", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), Home.class));
                }
                else {
                    Toast.makeText(SignUp.this, "Error " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addControls() {
        sign_gg=findViewById(R.id.sign_gg);
        name=findViewById(R.id.name1);
        pass=findViewById(R.id.pass1);
        email=findViewById(R.id.email1);
        tv_login=findViewById(R.id.tv_login);
        btn_re=findViewById(R.id.button1);
        firebaseAuth= FirebaseAuth.getInstance();
        mAuth= FirebaseAuth.getInstance();

    }

}
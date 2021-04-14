package com.lh.gasapp.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.PublicKey;

public class FirebaseWrapper {
    public static final String DEFAULT_USERID = "ERL43dCv3RXCMy8LRLzyIt6WtLH3";

    private static DatabaseReference firebaseReference;

    private FirebaseWrapper(DatabaseReference firebaseReference) {
        this.firebaseReference = firebaseReference;
    }

    public static DatabaseReference getReferrence(){

        //return FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getUid());


        return FirebaseDatabase.getInstance().getReference(DEFAULT_USERID);
    }


}

package com.tpad.hikr;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.tpad.hikr.R;


import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tpad.hikr.DataClasses.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final String TAG = SplashActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 123;
    static final int delayLength = 3000;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if(firebaseUser == null){
            signIn();
            showSnackbar(R.string.unknown_error);
        }
        else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                showSnackbar(R.string.success);
                Intent intent = new Intent(SplashActivity.this, MainNavActivity.class);
                SplashActivity.this.startActivity(intent);
                SplashActivity.this.finish();
                }
            }, delayLength);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult called");
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        try {
            if (requestCode == RC_SIGN_IN) {
                IdpResponse response = IdpResponse.fromResultIntent(data);
                // Successfully signed in
                if (resultCode == ResultCodes.OK) {
                    Intent intent = new Intent(SplashActivity.this, MainNavActivity.class);
                    startActivity(intent);
                    return;
                }
                else {
                    // Sign in failed
                    if (response == null) {
                        // User pressed back button
                        signIn();
                        showSnackbar(R.string.sign_in_cancelled);
                        return;
                    }

                    if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                        showSnackbar(R.string.no_internet_connection);
                        finish();
                        return;
                    }

                    if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                        signIn();
                        showSnackbar(R.string.unknown_error);
                        return;
                    }
                }

            }
        }
        catch (Exception e){
            Log.d(TAG, e.getMessage());
        }
    }

    public void signIn(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                        new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()))
                        .build(),
                RC_SIGN_IN);
    }

    public void showSnackbar(int id){
        Snackbar.make(coordinatorLayout, getString(id), Snackbar.LENGTH_LONG).show();
    }

    public void showSnackbar(String text){
        Snackbar.make(coordinatorLayout, text, Snackbar.LENGTH_LONG).show();
    }
}



package com.example.hundred;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class LogoActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 101;
    //Defining view objects
    private ImageView imgLoading;
    private TextView notLoging;
    private LinearLayout signInMenu;
    private ProgressDialog progressDialog;
    private SignInButton singInButton;
    private String userPhone;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseFirestore mFirestore;
    private FirebaseAnalytics mFirebaseAnalytics;

    private FirebaseAuth mAuth;

    User user = new User();
    Utilities utilities = new Utilities();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        //reference views
        imgLoading = (ImageView) findViewById(R.id.animSelec);
        signInMenu = (LinearLayout) findViewById(R.id.singInMenu);

        /**
        singInButton = (SignInButton) findViewById(R.id.google_button);
        singInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
         **/


        progressDialog = new ProgressDialog(this);

        mFirestore = FirebaseFirestore.getInstance();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        mAuth = FirebaseAuth.getInstance();

    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();


        if (currentUser != null) {
            updateUI(currentUser);
            loadingAnimation();
            signInMenu.setAlpha(0f);
            signInMenu.setEnabled(false);
        } else {
            signIn();
            signInMenu.setAlpha(1f);
            signInMenu.setEnabled(true);
        }


    }

    private void loadingAnimation(){
        //loading animation
        imgLoading.setAlpha(0.7f);
        Drawable drawableLoading = imgLoading.getDrawable();
        if ( drawableLoading instanceof AnimatedVectorDrawableCompat){
            AnimatedVectorDrawableCompat avdConstantRotation = (AnimatedVectorDrawableCompat) drawableLoading;
            avdConstantRotation.start();
        } else if (drawableLoading instanceof AnimatedVectorDrawable) {
            AnimatedVectorDrawable avdLoading = (AnimatedVectorDrawable) drawableLoading;
            avdLoading.start();
        }
    }

    public void playAnonymous(View view){

        Bundle params = new Bundle();
        params.putLong("count", 1);
        mFirebaseAnalytics.logEvent("playAnonymous", params);
        updateUILogNull(null);
        loadingAnimation();


    }

    public void signIn () {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
                progressDialog.setMessage(getString(R.string.logging));
                progressDialog.show();
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                //Toast.makeText(LogoActivity.this, R.string.not_log,Toast.LENGTH_LONG).show();
                updateUILogNull(null);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LogoActivity.this, getString(R.string.logged_as) +" " + user.getDisplayName(), Toast.LENGTH_LONG).show();
                            updateUILogIn(user);
                            progressDialog.dismiss();
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LogoActivity.this, getString(R.string.failure_log) ,Toast.LENGTH_LONG).show();
                            updateUILogNull(null);
                            progressDialog.dismiss();
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {

        userSharedPreferences(user.getDisplayName(), user.getEmail(), false);
        final String userName = user.getDisplayName();
        Toast.makeText(LogoActivity.this, getString(R.string.welcome)  + " " + userName, Toast.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent logoIntent = new Intent(LogoActivity.this, MainActivity.class);
                startActivity(logoIntent);
                finish();
            }
        }, Utilities.START_DELAY);

    }

    private void updateUILogIn(FirebaseUser user) {

        Intent logoIntent = new Intent(LogoActivity.this, MainActivity.class);
        startActivity(logoIntent);
        finish();
        Toast.makeText(LogoActivity.this, getString(R.string.welcome)  + " "  + user.getDisplayName(), Toast.LENGTH_LONG).show();

        checkUserExist(user.getDisplayName(), user.getEmail());

        userPhone = user.getPhoneNumber();


    }

    private void checkUserExist (final String userName, final String userMail){
        mFirestore.collection("users").document(userMail).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            userSharedPreferences(userName, userMail, false);
                            //jugador.syncUserData(true, userMail);
                        } else {
                            createDbUser(userName, userMail);
                            userSharedPreferences(userName, userMail, true);
                            //jugador.syncUserData(true, userMail);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void updateUILogNull(FirebaseUser user) {

        userSharedPreferencesNotLogged();
        //createDbGuestUser("anonymous", "anonymous");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent logoIntent = new Intent(LogoActivity.this, MainActivity.class);
                startActivity(logoIntent);
                finish();
            }
        }, Utilities.START_DELAY/2);

    }

    private void createDbUser(String userName, String userMail){


        // Create a new user
        Map<String, Object> user = new HashMap<>();
        user.put("name", userName);
        user.put("email", userMail);
        user.put("phone", userPhone);
        user.put("date", new Date().getTime());
        user.put("level", 1500);
        user.put("livesUses", 0);
        user.put("totalLivesUses", 0);
        user.put("maxBubble",1);
        user.put("maxScore", 0);
        user.put("maxBubbleClassic",1);
        user.put("maxScoreClassic", 0);
        user.put("maxCombo", 0);
        user.put("over100", 0);
        user.put("over200", 0);
        user.put("countMoves", 0);
        user.put("credits", utilities.START_CREDITS);
        user.put("newCredits", 0);
        user.put("maxBubbleCount", 1);
        user.put("maxScoreCount", 0);
        user.put("maxBubbleCredits", 0);
        user.put("spendCredits", 0);
        user.put("newGames", 0);

        // Add a new document with a generated ID
        mFirestore.collection("users").document(userMail).set(user);
        /**
         .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
        @Override
        public void onSuccess(DocumentReference documentReference) {
        //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
        }
        })
         .addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
        //Log.w(TAG, "Error adding document", e);
        }
        });
         **/

    }


    private void userSharedPreferences (String userName, String userMail, boolean firstScore){

        SharedPreferences userSP = getSharedPreferences("userSP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSP.edit();
        editor.putBoolean("logged in", true);
        editor.putString("email", userMail);
        editor.putString("name", userName);
        editor.putBoolean("firstScore", true);
        editor.commit();

    }

    private void userSharedPreferencesNotLogged (){

        SharedPreferences userSP = getSharedPreferences("userSP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSP.edit();
        editor.putBoolean("logged in", false);
        editor.putString("email", "guest");
        editor.putString("name", "guest");
        editor.putBoolean("firstScore", true);
        editor.commit();

    }


        /**
        public void registerUser(View view){

            String email = txtEmail.getText().toString().trim();
            String password = txtPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)){
                Toast.makeText(this,"You must to put an email", Toast.LENGTH_LONG).show();
                return;
            } else if (TextUtils.isEmpty(password)){
                Toast.makeText(this,"You must to put a password", Toast.LENGTH_LONG).show();
                return;
            } else {

                progressDialog.setMessage("Registering...");
                progressDialog.show();
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete( Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent logoIntent = new Intent(LogoActivity.this, MainActivity.class);
                                    startActivity(logoIntent);
                                    finish();
                                    progressDialog.dismiss();
                                } else {
                                    Toast.makeText(LogoActivity.this, "Auhentication failed.", Toast.LENGTH_LONG).show();
                                }
                                progressDialog.dismiss();
                            }
                        });
            }

        }

        public void singIn(View view){

            String email = txtEmail.getText().toString().trim();
            String password = txtPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)){
                Toast.makeText(this,"You must to put an email", Toast.LENGTH_LONG).show();
                return;
            } else if (TextUtils.isEmpty(password)){
                Toast.makeText(this,"You must to put a password", Toast.LENGTH_LONG).show();
                return;
            } else {
                progressDialog.setMessage("Singing in");
                progressDialog.show();
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete( Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent logoIntent = new Intent(LogoActivity.this, MainActivity.class);
                                    startActivity(logoIntent);
                                    finish();
                                    progressDialog.dismiss();
                                } else {

                                    Toast.makeText(LogoActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                                // ...
                            }
                        });
            }
        }
    **/

}

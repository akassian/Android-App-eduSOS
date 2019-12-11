package com.example.edusos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LogInSignUpActivity extends AppCompatActivity {
    // For Sign in
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 007;
    GoogleSignInAccount googleAccount;

    private DatabaseReference dbExperts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_sign_up);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        dbExperts = FirebaseDatabase.getInstance().getReference("Experts");
    }
    public void onBackClick(View button) {
        Intent myIntent = new Intent(this, MainActivity.class);
        this.startActivity(myIntent);
    }
    public void onExpertSignUpClick(View view) {
        Intent myIntent = new Intent(this, RegisterExpertActivity.class);
        this.startActivity(myIntent);

    }
    public void onStudentSignUpClick(View view) {
        Intent myIntent = new Intent(this, RegisterStudentActivity.class);
        this.startActivity(myIntent);

    }

    // For Sign in
    public void onLoginClick(View button) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateData(account, true);
        signIn();
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        //startActivityForResult(signInIntent,1);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.d("SIGNIN_!: ", account.getDisplayName() + ",   " + account.getEmail());

            ((EduSOSApplication) LogInSignUpActivity.this.getApplication()).setAccount(account); // save account in a global variable


            //String personName = account.getDisplayName();

            //String email = account.getEmail();

            //txt_welcome.setText("Welcome "+personName+"\n"+ email);

            updateData(account, true);   // Signed in successfully, show authenticated UI.

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("SIGNIN_", "signInResult:failed code=" + e.getStatusCode());
            Log.d("SIGNIN_", "signInResult:failed code=" + e.getStatusCode());

            updateData(null, false);
        }
    }

    private void updateData(GoogleSignInAccount account, Boolean onlineStatus) {
        final Boolean onlineStatus1 = onlineStatus;
        String email;
        String googleAcc;
        if (account != null) {

            Log.d("SIGNIN_UI", account.getDisplayName() + ",   " + account.getEmail());

            if (account.getEmail()!= null) {
                email = account.getEmail().toString();
                googleAcc = email.split("@")[0];
                Query query = dbExperts.orderByChild("googleAccount").equalTo(googleAcc);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot ds: dataSnapshot.getChildren()) {

                                Expert expert = ds.getValue(Expert.class);
                                String key = ds.getKey();
                                DatabaseReference updateExpert = FirebaseDatabase.getInstance()
                                        .getReference("Experts")
                                        .child(key);

                                if (updateExpert != null) {
                                    updateExpert.child("online").setValue(onlineStatus1);

                                    Log.d("ONLINE_", String.valueOf(onlineStatus1));
                                    ((EduSOSApplication) LogInSignUpActivity.this.getApplication()).setLoggedInAsExpert(true);
                                    Log.d("LOGIN_", String.valueOf(((EduSOSApplication) LogInSignUpActivity.this.getApplication()).getLoggedInAsExpert()));

                                    //Toast.makeText(LogInSignUpActivity.this, "Online Status Updated!", Toast.LENGTH_SHORT).show();
                                    break;
                                }

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(LogInSignUpActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }


        } else {
            Log.d("SIGNIN_UI", "Sign in error!");
        }

    }


    public void onLogoutClick(View button) {
        signOut();

    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        signOutUpdate();
                        Toast.makeText(LogInSignUpActivity.this, "You have logged out!", Toast.LENGTH_SHORT).show();

                    }
                });
    }
    private void signOutUpdate() {
        ((EduSOSApplication) LogInSignUpActivity.this.getApplication()).setAccount(null);
        ((EduSOSApplication) LogInSignUpActivity.this.getApplication()).setLoggedInAsExpert(false);

    }

}

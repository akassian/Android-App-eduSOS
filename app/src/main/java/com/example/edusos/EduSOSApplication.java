package com.example.edusos;
import android.app.Application;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class EduSOSApplication extends Application {
    private GoogleSignInAccount account;

    public GoogleSignInAccount getAccount() {
        return account;
    }

    public void setAccount(GoogleSignInAccount account) {
        this.account = account;
    }
}

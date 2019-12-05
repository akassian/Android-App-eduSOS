package com.example.edusos;
import android.app.Application;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class EduSOSApplication extends Application {
    private GoogleSignInAccount account;
    private Boolean loggedInAsExpert = false;

    public Boolean getLoggedInAsExpert() {
        return loggedInAsExpert;
    }

    public void setLoggedInAsExpert(Boolean loggedInAsExpert) {
        this.loggedInAsExpert = loggedInAsExpert;
    }

    public GoogleSignInAccount getAccount() {
        return account;
    }

    public void setAccount(GoogleSignInAccount account) {
        this.account = account;
    }
}

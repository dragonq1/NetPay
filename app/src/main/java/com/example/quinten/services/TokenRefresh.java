package com.example.quinten.services;


import android.content.SharedPreferences;
import android.util.Log;

import com.example.quinten.methods.refreshToken;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class TokenRefresh extends FirebaseInstanceIdService{

    public static final String USER_INFO = "UserInfo";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.d("Firebase", "This is your Firebase token: " + refreshedToken);



         SharedPreferences settings = getSharedPreferences(USER_INFO, 0);
         final String strGebruikersnaam = settings.getString("gebruikersnaam", "");

        refreshToken refreshToken = new refreshToken();
        refreshToken.refreshidToken(refreshedToken, strGebruikersnaam, getApplicationContext());

    }

}


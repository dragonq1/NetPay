package com.example.quinten.services;


import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.quinten.requests.FirebaseTokenRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class TokenRefresh extends FirebaseInstanceIdService{

    public static final String USER_INFO = "UserInfo";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.d("Firebase", "This is your Firebase token: " + refreshedToken);

        SharedPreferences settings = getSharedPreferences(USER_INFO, 0);
        String strGebruikersnaam = settings.getString("gebruikersnaam", "");

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        };

        FirebaseTokenRequest FTR = new FirebaseTokenRequest(strGebruikersnaam, refreshedToken, responseListener);
        RequestQueue queue = Volley.newRequestQueue(TokenRefresh.this);
        queue.add(FTR);

    }

}


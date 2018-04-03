package com.example.quinten.methods;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.quinten.requests.FirebaseTokenRequest;

import org.json.JSONObject;


public class refreshToken {



    public void refreshidToken(String Token, String strGebruikersnaam, final Context context) {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonReponse = new JSONObject(response);
                    boolean success = jsonReponse.getBoolean("success");

                    if (success) {
                        Toast.makeText(context.getApplicationContext(), "Test", Toast.LENGTH_LONG).show();
                        Log.d("Error", "Update token was succesfull");
                    } else {
                        Toast.makeText(context.getApplicationContext(), "error:", Toast.LENGTH_LONG).show();
                        Log.d("Error", jsonReponse.getString("error"));
                    }


                } catch (Exception e) {
                    Toast.makeText(context.getApplicationContext(), "error catch", Toast.LENGTH_LONG).show();
                    Log.d("Error", e.toString());
                }
            }
        };

        FirebaseTokenRequest FTRRequest = new FirebaseTokenRequest(strGebruikersnaam, Token, responseListener);
        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
        queue.add(FTRRequest);
    }
}

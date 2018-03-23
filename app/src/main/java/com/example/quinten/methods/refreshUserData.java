package com.example.quinten.methods;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.quinten.requests.RefreshRequest;

import org.json.JSONObject;

public class refreshUserData {

    private boolean status;

    public boolean refreshData(String strGebruikersnaam, Context context) {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonReponse = new JSONObject(response);
                    boolean success = jsonReponse.getBoolean("success");

                    if(success) {
                        status =  true;
                    }else{
                        status = false;
                    }

                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        };

        RefreshRequest refreshRequest = new RefreshRequest(strGebruikersnaam, responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(refreshRequest);

        return status;
    }


}

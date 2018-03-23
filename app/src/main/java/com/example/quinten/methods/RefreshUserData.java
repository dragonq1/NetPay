package com.example.quinten.methods;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.quinten.requests.RefreshRequest;

import org.json.JSONObject;

public class RefreshUserData {
    private static final String USER_INFO = "UserInfo";

    public void refreshData(String strGebruikersnaam, final Context context) {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonReponse = new JSONObject(response);
                    boolean success = jsonReponse.getBoolean("success");

                    if(success) {
                        SharedPreferences settings = context.getApplicationContext().getSharedPreferences(USER_INFO, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("saldo", jsonReponse.getString("Saldo"));
                        editor.apply();
                        Toast.makeText(context.getApplicationContext(), "Gegevens opnieuw ingeladen!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context.getApplicationContext(), "Oeps! Er ging iets fout!", Toast.LENGTH_SHORT).show();
                    }

                }catch(Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context.getApplicationContext(), "Oeps! Er ging iets fout!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        RefreshRequest refreshRequest = new RefreshRequest(strGebruikersnaam, responseListener);
        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
        queue.add(refreshRequest);
    }


}

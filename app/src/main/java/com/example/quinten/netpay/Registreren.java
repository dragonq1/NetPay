package com.example.quinten.netpay;

import android.app.DownloadManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Registreren extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registreren);
        setTitle("Registreren");

        //Vars
        final EditText strVoornaam = (EditText) findViewById(R.id.txtVoornaam);
        final EditText strAchternaam = (EditText) findViewById(R.id.txtAchternaam);
        final EditText strGebruikersnaam = (EditText) findViewById(R.id.txtGebruikersnaam);
        final EditText strWachtwoord = (EditText) findViewById(R.id.txtWachtwoord);
        final Button btnRegister = (Button) findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String gebruikersnaam = strGebruikersnaam.getText().toString();
                final String voornaam = strVoornaam.getText().toString();
                final String achternaam = strAchternaam.getText().toString();
                final String wachtwoord = strWachtwoord.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean succes = jsonResponse.getBoolean("succes");

                            if(succes) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "Registratie succesvol!", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getApplicationContext(), "Fout bij registreren", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                RegisterRequest registerRequest = new RegisterRequest(gebruikersnaam, voornaam, achternaam, wachtwoord, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Registreren.this);
                queue.add(registerRequest);
            }
        });



    }
}

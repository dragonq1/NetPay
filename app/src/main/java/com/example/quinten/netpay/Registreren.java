package com.example.quinten.netpay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
        final EditText txtVoornaam = (EditText) findViewById(R.id.txtVoornaam);
        final EditText txtAchternaam = (EditText) findViewById(R.id.txtAchternaam);
        final EditText txtGebruikersnaam = (EditText) findViewById(R.id.txtGebruikersnaam);
        final EditText txtWachtwoord = (EditText) findViewById(R.id.txtWachtwoord);
        final EditText txtWachtwoordH = (EditText) findViewById(R.id.txtWachtwoordHerhalen);
        final Button btnRegister = (Button) findViewById(R.id.btnRegister);
        final ProgressBar prgRegistreren = (ProgressBar) findViewById(R.id.prbRegistreren);

        //ProgressSpiner onzichtbaar maken
        prgRegistreren.setVisibility(View.INVISIBLE);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Vars
                final String gebruikersnaam = txtGebruikersnaam.getText().toString();
                final String voornaam = txtVoornaam.getText().toString();
                final String achternaam = txtAchternaam.getText().toString();
                final String wachtwoord = txtWachtwoord.getText().toString();
                final String wachtwoordH = txtWachtwoordH.getText().toString();

                if (!(gebruikersnaam.equals("") || voornaam.equals("") || achternaam.equals("") || wachtwoord.equals("")
                        || wachtwoordH.equals(""))) {
                    if((wachtwoord.equals(wachtwoordH))) {
                        //ProgressSpiner zichtbaar maken
                        prgRegistreren.setVisibility(View.VISIBLE);
                        //Registratie aanvraag
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");

                                    if (success) {
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(getApplicationContext(), "Registratie succesvol!", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Fout bij registreren", Toast.LENGTH_LONG).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "ERROR" + " " + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        };

                        RegisterRequest registerRequest = new RegisterRequest(gebruikersnaam, voornaam, achternaam, wachtwoord, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(Registreren.this);
                        queue.add(registerRequest);
                    }else{
                        Toast.makeText(getApplicationContext(), "Wachtwoorden komen niet overeen!", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Vul alle velden in!", Toast.LENGTH_LONG).show();
                }
            }
        });
        //ProgressSpiner onzichtbaar maken
        prgRegistreren.setVisibility(View.INVISIBLE);



    }
}

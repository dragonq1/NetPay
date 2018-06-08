package com.example.quinten.netpay;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.dd.processbutton.iml.ActionProcessButton;
import com.example.quinten.requests.RegisterRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class Registreren extends AppCompatActivity {

    //Snack notificatie
    public void sendSnackbar(String strBericht, View v) {
        Snackbar snackbar = Snackbar.make(v, strBericht, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registreren);
        setTitle("Registreren");

        //Vars
        final EditText txtVoornaam = findViewById(R.id.txtVoornaam);
        final EditText txtAchternaam = findViewById(R.id.txtAchternaam);
        final EditText txtGebruikersnaam = findViewById(R.id.txtGebruikersnaam);
        final EditText txtWachtwoord = findViewById(R.id.txtWachtwoordBetaling);
        final EditText txtWachtwoordH = findViewById(R.id.txtWachtwoordHerhalen);
        final ActionProcessButton btnRegister = findViewById(R.id.btnRegister);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Vars
                final String gebruikersnaam = txtGebruikersnaam.getText().toString();
                final String voornaam = txtVoornaam.getText().toString();
                final String achternaam = txtAchternaam.getText().toString();
                final String wachtwoord = txtWachtwoord.getText().toString();
                final String wachtwoordH = txtWachtwoordH.getText().toString();

                btnRegister.setProgress(25);

                //Toetsenbord sluiten
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                if (!(gebruikersnaam.equals("") || voornaam.equals("") || achternaam.equals("") || wachtwoord.equals("")
                        || wachtwoordH.equals(""))) {
                    if((wachtwoord.equals(wachtwoordH))) {
                        btnRegister.setProgress(50);
                        //Registratie aanvraag
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");

                                    if (success) {
                                        btnRegister.setProgress(100);
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);

                                    }else if(jsonResponse.getString("error").equals("error-05")) {
                                        sendSnackbar("Gebruikersnaam is al in gebruik!", findViewById(android.R.id.content));
                                        btnRegister.setProgress(0);
                                    }
                                    else {
                                        sendSnackbar("Er ging iets fout! Blijft dit gebeuren, kan je contact opnemen!", findViewById(android.R.id.content));
                                        btnRegister.setProgress(0);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    sendSnackbar("Er ging iets fout! Blijft dit gebeuren, kan je contact opnemen!", findViewById(android.R.id.content));
                                    btnRegister.setProgress(0);
                                }
                            }
                        };

                        RegisterRequest registerRequest = new RegisterRequest(gebruikersnaam, voornaam, achternaam, wachtwoord, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(Registreren.this);
                        queue.add(registerRequest);
                    }else{
                        sendSnackbar("Wachtwoorden komen niet overeen!", findViewById(android.R.id.content));
                        btnRegister.setProgress(0);
                    }
                }else{
                    sendSnackbar("Vul alle velden in!", findViewById(android.R.id.content));
                    btnRegister.setProgress(0);
                }
            }
        });

        btnRegister.setProgress(0);


    }
}

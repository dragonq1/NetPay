package com.example.quinten.netpay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.dd.processbutton.iml.ActionProcessButton;
import com.example.quinten.methods.InternetStatus;
import com.example.quinten.requests.LoginRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    public static final String USER_INFO = "UserInfo";
    private CoordinatorLayout coordinatorLayout;


    //Edit texts resetten
    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    //Back button uitschakelen
    @Override
    public void onBackPressed() {
        SharedPreferences settings = getSharedPreferences(USER_INFO, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.apply();
        moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Vars
        final ActionProcessButton btnLogin = findViewById(R.id.btnLogin);
        final Button btnReg = findViewById(R.id.btnRegister);
        final EditText txtGebruiksnaam = findViewById(R.id.txtNaam);
        final EditText txtWachtwoord = findViewById(R.id.txtWachtwoordBetaling);









        //Button listeners
        //Login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Internet connectie nakijken
                InternetStatus IS = new InternetStatus();
                boolean is = IS.getInternetStatus(getApplicationContext());
                btnLogin.setProgress(25);

                //Toetsenbord sluiten
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                if(is) {
                    //Nakijken of alle velden zijn ingevuld
                    if(txtGebruiksnaam.getText().toString().equals("") || txtWachtwoord.getText().toString().equals("")) {
                        sendSnackbar("Vul alle velden in!", findViewById(android.R.id.content));
                        btnLogin.setProgress(0);
                    }else{
                        btnLogin.setProgress(50);

                        //Vars
                        final String strGebruikersnaam = txtGebruiksnaam.getText().toString();
                        final String strWachtwoord = txtWachtwoord.getText().toString();




                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonReponse = new JSONObject(response);
                                    boolean success = jsonReponse.getBoolean("success");

                                    if (success) {
                                        btnLogin.setProgress(100);
                                        //Terugekregen gebruikersinfo ophalen
                                        String strID = jsonReponse.getString("ID");
                                        String strGebruikersnaamResp = jsonReponse.getString("Gebruikersnaam");
                                        String strVoornaamResp = jsonReponse.getString("Voornaam");
                                        String strAchternaamResp = jsonReponse.getString("Achternaam");
                                        String strSaldoResp = jsonReponse.getString("Saldo");



                                        //Menu openen en gebruikersinfo cachen
                                        SharedPreferences settings = getSharedPreferences(USER_INFO, 0);
                                        SharedPreferences.Editor editor = settings.edit();
                                        editor.putString("ID", (strID));
                                        editor.putString("gebruikersnaam", strGebruikersnaamResp);
                                        editor.putString("voornaam", strVoornaamResp);
                                        editor.putString("achternaam", strAchternaamResp);
                                        editor.putString("saldo", strSaldoResp);
                                        editor.apply();



                                        Intent intent = new Intent(getApplicationContext(), Menu.class);
                                        startActivity(intent);


                                    } else if(jsonReponse.getString("error").equals("error-01")) {
                                        sendSnackbar("Wachtwoord of gebruikersnaam ongeldig!", findViewById(android.R.id.content));
                                        btnLogin.setProgress(0);

                                    }else if(jsonReponse.getString("error").equals("error-02")) {
                                        sendSnackbar("Wachtwoord of gebruikersnaam incorrect!", findViewById(android.R.id.content));
                                        btnLogin.setProgress(0);

                                    }else{
                                        Log.e("ERROR", "onResponse: " + jsonReponse.getString("error"));
                                        sendSnackbar("Er ging iets fout! Blijft dit gebeuren, meld dit dan!", findViewById(android.R.id.content));
                                        btnLogin.setProgress(0);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    sendSnackbar("Er ging iets fout! Blijft dit gebeuren, meld dit dan!", findViewById(android.R.id.content));
                                    btnLogin.setProgress(0);
                                }

                            }
                        };

                        LoginRequest loginRequest = new LoginRequest(strGebruikersnaam, strWachtwoord, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                        queue.add(loginRequest);


                    }

                }else{
                    sendSnackbar("Controleer de internet verbinding!", findViewById(android.R.id.content));
                    btnLogin.setProgress(0);
                }
            }
        });



        //Registeren
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Registreren.class);
                startActivity(intent);
            }

        });

    }

    //Snack notificatie
    public void sendSnackbar(String strBericht, View v) {
        Snackbar snackbar = Snackbar.make(v, strBericht, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}

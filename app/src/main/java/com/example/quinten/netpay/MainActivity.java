package com.example.quinten.netpay;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.dd.processbutton.iml.ActionProcessButton;
import com.example.quinten.methods.InternetStatus;
import com.example.quinten.requests.LoginRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    public static final String USER_INFO = "UserInfo";


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
        final ProgressBar prgLogin = findViewById(R.id.prbLogin);
        final EditText txtGebruiksnaam = findViewById(R.id.txtNaam);
        final EditText txtWachtwoord = findViewById(R.id.txtWachtwoordBetaling);




        //ProgressSpiner onzichtbaar maken
        prgLogin.setVisibility(View.INVISIBLE);

        //Button listeners
        //Login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Internet connectie nakijken
                InternetStatus IS = new InternetStatus();
                boolean is = IS.getInternetStatus(getApplicationContext());
                btnLogin.setProgress(25);



                if(is) {
                    //Nakijken of alle velden zijn ingevuld
                    if(txtGebruiksnaam.getText().toString().equals("") || txtWachtwoord.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Vul alle velden in!", Toast.LENGTH_LONG).show();
                    }else{
                        //ProgressSpiner zichtbaar maken
                        prgLogin.setVisibility(View.VISIBLE);
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

                                        //ProgressSpiner onzichtbaar maken
                                        prgLogin.setVisibility(View.INVISIBLE);

                                    } else if(jsonReponse.getString("error").equals("error-01")) {
                                        Toast.makeText(getApplicationContext(), "Gebruikersnaam of wachtwoord incorrect!", Toast.LENGTH_LONG).show();
                                        //ProgressSpiner onzichtbaar maken
                                        prgLogin.setVisibility(View.INVISIBLE);

                                    }else if(jsonReponse.getString("error").equals("error-02")) {
                                        Toast.makeText(getApplicationContext(), "Gebruikersnaam of wachtwoord incorrect!", Toast.LENGTH_LONG).show();
                                        //ProgressSpiner onzichtbaar maken
                                        prgLogin.setVisibility(View.INVISIBLE);

                                    }else{
                                        Toast.makeText(getApplicationContext(), "ERROR 1: " + jsonReponse.getString("error"), Toast.LENGTH_LONG).show();
                                        //ProgressSpiner onzichtbaar maken
                                        prgLogin.setVisibility(View.INVISIBLE);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "ERROR 1: " + " " +  e.getMessage(), Toast.LENGTH_SHORT).show();
                                    //ProgressSpiner onzichtbaar maken
                                    prgLogin.setVisibility(View.INVISIBLE);
                                }

                            }
                        };

                        LoginRequest loginRequest = new LoginRequest(strGebruikersnaam, strWachtwoord, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                        queue.add(loginRequest);


                    }

                }else{
                    Toast.makeText(getApplicationContext(), "Geen internet verbinding!", Toast.LENGTH_LONG).show();
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
}

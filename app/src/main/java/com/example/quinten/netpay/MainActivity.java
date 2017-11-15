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

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    public static final String USER_INFO = "UserInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Vars
        final Button btnLogin = (Button) findViewById(R.id.btnLogin);
        final Button btnReg = (Button) findViewById(R.id.btnRegister);
        final ProgressBar prgLogin = (ProgressBar) findViewById(R.id.prbLogin);
        final EditText txtGebruiksnaam = (EditText)findViewById(R.id.txtNaam);
        final EditText txtWachtwoord = (EditText)findViewById(R.id.txtWachtwoord);


        //ProgressSpiner onzichtbaar maken
        prgLogin.setVisibility(View.INVISIBLE);

        //Button listeners
        //Login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Nakijken of alle velden zijn ingevuld
                if(txtGebruiksnaam.getText().toString().equals("") || txtWachtwoord.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Vul alle velden in!", Toast.LENGTH_LONG).show();
                }else{
                    //ProgressSpiner zichtbaar maken
                    prgLogin.setVisibility(View.VISIBLE);

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
                                    //Terugekregen gebruikersinfo ophalen
                                    String strID = jsonReponse.getString("ID");
                                    String strGebruikersnaamResp = jsonReponse.getString("Gebruikersnaam");
                                    String strVoornaamResp = jsonReponse.getString("Voornaam");
                                    String strAchternaamResp = jsonReponse.getString("Achternaam");
                                    String strWachtwoordResp = jsonReponse.getString("Wachtwoord");
                                    String strSaldoResp = jsonReponse.getString("Saldo");



                                    //Menu openen en gebruikersinfo cachen
                                    SharedPreferences settings = getSharedPreferences(USER_INFO, 0);
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putString("ID", (strID));
                                    editor.putString("gebruikersnaam", strGebruikersnaamResp);
                                    editor.putString("voornaam", strVoornaamResp);
                                    editor.putString("achternaam", strAchternaamResp);
                                    editor.putString("wachtwoord", strWachtwoordResp);
                                    editor.putString("saldo", strSaldoResp);
                                    editor.apply();

                                    Intent intent = new Intent(getApplicationContext(), Menu.class);
                                    startActivity(intent);


                                    //ProgressSpiner onzichtbaar maken
                                    prgLogin.setVisibility(View.INVISIBLE);

                                } else {
                                    Toast.makeText(getApplicationContext(), "Gebruikersnaam of wachtwoord niet correct!", Toast.LENGTH_LONG).show();
                                    //ProgressSpiner onzichtbaar maken
                                    prgLogin.setVisibility(View.INVISIBLE);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "ERROR!" + " " +  e.getMessage().toString(), Toast.LENGTH_LONG).show();
                                //ProgressSpiner onzichtbaar maken
                                prgLogin.setVisibility(View.INVISIBLE);
                            }

                        }
                    };

                    LoginRequest loginRequest = new LoginRequest(strGebruikersnaam, strWachtwoord, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    queue.add(loginRequest);


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

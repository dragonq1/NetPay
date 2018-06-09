package com.example.quinten.netpay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.quinten.requests.WachtwoordVRequest;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.quinten.netpay.MainActivity.USER_INFO;

public class WachtwoordVeranderen extends AppCompatActivity {

    //Snack notificatie
    public void sendSnackbar(String strBericht, View v) {
        Snackbar snackbar = Snackbar.make(v, strBericht, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wachtwoord_veranderen);
        setTitle("Wachtwoord veranderen");

        //Vars
        final EditText txtOudWachtwoord = findViewById(R.id.txtOudWachtwoord);
        final EditText txtNieuwWachtwoord = findViewById(R.id.txtNieuwWachtwoord);
        final EditText txtNieuwWachtwoord2 = findViewById(R.id.txtNieuwWachtwoord2);
        final Button btnWachtwoordV = findViewById(R.id.btnWachtwoordV);

        //Gegevens ophalen
        SharedPreferences settings = getSharedPreferences(USER_INFO, 0);
        final String strGebruikersnaamResp = settings.getString("gebruikersnaam", "");


        btnWachtwoordV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toetsenbord sluiten
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                //Vars
                String strOudWachtwoord = txtOudWachtwoord.getText().toString();
                String strNieuwWachtwoord = txtNieuwWachtwoord.getText().toString();
                String strNieuwWachtwoord2 = txtNieuwWachtwoord2.getText().toString();

                if(txtOudWachtwoord.getText().toString().equals("") || txtNieuwWachtwoord.getText().toString().equals("")
                    || txtNieuwWachtwoord2.toString().equals("")) {
                    sendSnackbar("Vul alle velden in!", findViewById(android.R.id.content));
                }
                else if (!(strNieuwWachtwoord.equals(strNieuwWachtwoord2))) {
                    sendSnackbar("Wachtwoorden komen niet overeen!", findViewById(android.R.id.content));

                }else{


                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonReponse = new JSONObject(response);
                                boolean success = jsonReponse.getBoolean("success");

                                if (success) {
                                    Toast.makeText(getApplicationContext(), "Wachtwoord is aangepast!", Toast.LENGTH_SHORT).show();
                                    Intent i = getBaseContext().getPackageManager()
                                            .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);


                                }else if(jsonReponse.getString("error").equals("error-06")) {
                                    sendSnackbar("Oud en nieuw wachtwoord zijn hetzelfde!", findViewById(android.R.id.content));

                                }else if(jsonReponse.getString("error").equals("error-07")) {
                                    sendSnackbar("Wachtwoord incorrect!", findViewById(android.R.id.content));
                                }
                                else {
                                    sendSnackbar("Oeps! Er is iets fout gelopen!", findViewById(android.R.id.content));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                sendSnackbar("Oeps! Er is iets fout gelopen!", findViewById(android.R.id.content));
                            }

                        }
                    };

                    WachtwoordVRequest wachtwoordVRequest = new WachtwoordVRequest(strOudWachtwoord, strNieuwWachtwoord2, strGebruikersnaamResp, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(WachtwoordVeranderen.this);
                    queue.add(wachtwoordVRequest);


                }






            }
        });


    }
}

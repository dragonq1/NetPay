package com.example.quinten.netpay;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.quinten.requests.BetalingRequest;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.quinten.netpay.MainActivity.USER_INFO;

public class BetalingManueel extends AppCompatActivity {

    @Override
    public void onBackPressed() {
            super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_betaling_manueel);

        //Vars
        final SharedPreferences settings = getSharedPreferences(USER_INFO, 0);
        final EditText txtGebruikersnaam = (EditText) findViewById(R.id.txtBedragBetaling);
        final EditText txtBedrag = (EditText) findViewById(R.id.txtBedragBetaling);

        final Double dblSaldo = Double.parseDouble(settings.getString("saldo", ""));
        final String strID = settings.getString("ID", "");

        final Button btnBevestigen = (Button) findViewById(R.id.btnBevestigen);
        final Button btnAnnuleren = (Button) findViewById(R.id.btnAnnuleren);

        final String gebruikersnaam = settings.getString("gebruikersnaam", "");

        final String strWachtwoord = settings.getString("wachtwoord", "");



        //Betaling Bevestigen
        btnBevestigen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builderBevestigen = new AlertDialog.Builder((BetalingManueel.this));
                final EditText txtWachtwoord = new EditText(BetalingManueel.this);
                txtWachtwoord.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);

                builderBevestigen.setTitle("Betaling bevestigen").setMessage("Vul je wachtwoord in om de betaling te bevestigen").setPositiveButton("Bevestigen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, final int i) {
                        //Betaling bevestigen
                        if(txtWachtwoord.getText().toString().equals(strWachtwoord)) {
                            if(!(txtGebruikersnaam.getText().toString().equals(gebruikersnaam))) {

                                String strBedrag = txtBedrag.getText().toString().trim();
                                final Double dblBedrag = Double.parseDouble(strBedrag);

                                if(!(dblSaldo < dblBedrag)) {

                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonResponse = new JSONObject(response);
                                                String success = jsonResponse.getString("success");

                                                switch(success) {
                                                    case "success":
                                                        Toast.makeText(getApplicationContext(), "Transactie geslaagd!", Toast.LENGTH_LONG).show();
                                                        txtBedrag.setText("");
                                                        txtGebruikersnaam.setText("");

                                                        //Saldo aanpassen
                                                        SharedPreferences.Editor editor = settings.edit();
                                                        String strNieuwSaldo =  String.valueOf((dblSaldo - dblBedrag));
                                                        editor.putString("saldo",  strNieuwSaldo);
                                                        editor.apply();

                                                        //Terug naar betaling activity gaan
                                                        Intent intent = new Intent(getApplicationContext(), Betalen.class);
                                                        startActivity(intent);
                                                        break;
                                                    case "Statement 2":
                                                        Toast.makeText(getApplicationContext(), "Statement 2", Toast.LENGTH_LONG).show();
                                                        break;
                                                    case "Statement 3":
                                                        Toast.makeText(getApplicationContext(), "Statement 3", Toast.LENGTH_LONG).show();
                                                        break;
                                                    case "false":
                                                        Toast.makeText(getApplicationContext(), "false", Toast.LENGTH_LONG).show();
                                                        break;
                                                    default:
                                                        Toast.makeText(getApplicationContext(), success, Toast.LENGTH_LONG).show();
                                                        break;
                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                Toast.makeText(getApplicationContext(), "ERROR" + " " + e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    };

                                    String gebruikersnaamBetaler = settings.getString("gebruikersnaam", "");

                                    BetalingRequest betalingRequest = new BetalingRequest(txtGebruikersnaam.getText().toString(), gebruikersnaamBetaler, dblBedrag.toString(), responseListener);
                                    RequestQueue queue = Volley.newRequestQueue(BetalingManueel.this);
                                    queue.add(betalingRequest);

                                }else{
                                    //Saldo ontoerijkend
                                    Toast.makeText(getApplicationContext(), "Saldo ontoerijkend!", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                //Gebruikersnaam is hetzelfde
                                Toast.makeText(getApplicationContext(), "Je kan geen geen betaling aan jezelf doen!", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "Wachtwoord incorrect!", Toast.LENGTH_LONG).show();
                        }

                    }
                })
                .setNegativeButton("Annuleren", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Niets
                    }
                }).setView(txtWachtwoord);
                builderBevestigen.create();
                builderBevestigen.show();
            }
        });


        //Betaling annuleren
        btnAnnuleren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builderAnnuleren = new AlertDialog.Builder(BetalingManueel.this);

                builderAnnuleren.setTitle("Betaling annuleren").setMessage("Ben je zeker dat je de betaling wilt annuleren?").setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), Betalen.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Nee", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Niets
                    }
                });
                builderAnnuleren.create();
                builderAnnuleren.show();

            }
        });

    }
}

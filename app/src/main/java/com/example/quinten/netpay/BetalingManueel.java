package com.example.quinten.netpay;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
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

    //Snack notificatie
    public void sendSnackbar(String strBericht, View v) {
        Snackbar snackbar = Snackbar.make(v, strBericht, Snackbar.LENGTH_LONG);
        snackbar.show();
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder((BetalingManueel.this));
        builder.setTitle("Betaling annuleren?").setNegativeButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getApplicationContext(), Betalen.class);
                startActivity(intent);
            }
        }).setPositiveButton("Nee", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Niets
            }
        });
        builder.create();
        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_betaling_manueel);

        //Vars
        final SharedPreferences settings = getSharedPreferences(USER_INFO, 0);
        final EditText txtGebruikersnaam = (EditText) findViewById(R.id.txtGebruikersnaamMan);
        final EditText txtBedrag = (EditText) findViewById(R.id.txtBedragBetaling);

        final Double dblSaldo = Double.parseDouble(settings.getString("saldo", ""));
        final String strID = settings.getString("ID", "");

        final Button btnBevestigen = (Button) findViewById(R.id.btnBevestigen);
        final Button btnAnnuleren = (Button) findViewById(R.id.btnAnnuleren);

        final String gebruikersnaam = settings.getString("gebruikersnaam", "");



        //Betaling Bevestigen
        btnBevestigen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!(txtGebruikersnaam.getText().toString().equals(""))) {
                    if(!(txtBedrag.getText().toString().equals(""))) {

                        AlertDialog.Builder builderBevestigen = new AlertDialog.Builder((BetalingManueel.this));
                        final EditText txtWachtwoord = new EditText(BetalingManueel.this);
                        txtWachtwoord.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                        builderBevestigen.setTitle("Betaling bevestigen").setMessage("Vul je wachtwoord in om de betaling te bevestigen").setPositiveButton("Bevestigen", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, final int i) {
                                //Betaling bevestigen
                                if (!(txtGebruikersnaam.getText().toString().equals(gebruikersnaam))) {

                                    String strBedrag = txtBedrag.getText().toString().trim();
                                    String strWachtwoord = txtWachtwoord.getText().toString();
                                    final Double dblBedrag = Double.parseDouble(strBedrag);

                                    if (!(dblSaldo < dblBedrag)) {

                                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonResponse = new JSONObject(response);
                                                    String success = jsonResponse.getString("success");

                                                    switch (success) {
                                                        case "success":
                                                            sendSnackbar("Transactie geslaagd!", findViewById(android.R.id.content));
                                                            txtBedrag.setText("");
                                                            txtGebruikersnaam.setText("");

                                                            Log.d("Not", jsonResponse.getString("erron"));

                                                            //Saldo aanpassen
                                                            SharedPreferences.Editor editor = settings.edit();
                                                            String strNieuwSaldo = String.valueOf((dblSaldo - dblBedrag));
                                                            editor.putString("saldo", strNieuwSaldo);
                                                            editor.apply();

                                                            //Terug naar betaling activity gaan
                                                            Intent intent = new Intent(getApplicationContext(), Menu.class);
                                                            intent.putExtra("ActionButton", "false");
                                                            startActivity(intent);
                                                            break;
                                                        case "error-01":
                                                            sendSnackbar("Kan ontvanger niet vinden!", findViewById(android.R.id.content));
                                                            break;
                                                        case "Statement 3":
                                                            sendSnackbar("Er ging iets fout", findViewById(android.R.id.content));
                                                            Log.e("ERROR", "onResponse: " + "Statement 3");
                                                            break;
                                                        case "false":
                                                            sendSnackbar("Er ging uets fout", findViewById(android.R.id.content));
                                                            Log.e("ERROR", "onResponse: " + "false");
                                                            break;
                                                        case "wachtwoord":
                                                            sendSnackbar("Incorrect wachtwoord!", findViewById(android.R.id.content));
                                                            break;
                                                        default:
                                                            sendSnackbar("Er ging iets fout", findViewById(android.R.id.content));
                                                            break;
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    Toast.makeText(getApplicationContext(), "ERROR 4" + " " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        };

                                        String gebruikersnaamBetaler = settings.getString("gebruikersnaam", "");

                                        BetalingRequest betalingRequest = new BetalingRequest(txtGebruikersnaam.getText().toString(), gebruikersnaamBetaler, dblBedrag.toString(), strWachtwoord, responseListener);
                                        RequestQueue queue = Volley.newRequestQueue(BetalingManueel.this);
                                        queue.add(betalingRequest);

                                    } else {
                                        //Saldo ontoerijkend
                                        sendSnackbar("Saldo ontoerijkend!", findViewById(android.R.id.content));
                                    }
                                } else {
                                    //Gebruikersnaam is hetzelfde
                                    sendSnackbar("Je kan geen geen betaling aan jezelf doen!", findViewById(android.R.id.content));
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
                    }else{
                        sendSnackbar("Vul een geldig bedrag in!", findViewById(android.R.id.content));
                    }
                }else{
                    sendSnackbar("Vul een geldig bedrag in!", findViewById(android.R.id.content));
                }
            }
        });



        //Betaling annuleren
        btnAnnuleren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builderAnnuleren = new AlertDialog.Builder(BetalingManueel.this);

                builderAnnuleren.setTitle("Betaling annuleren?").setPositiveButton("Ja", new DialogInterface.OnClickListener() {
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

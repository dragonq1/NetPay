package com.example.quinten.netpay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.example.quinten.netpay.MainActivity.USER_INFO;

public class Account extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        setTitle("Account");

        //Vars
        final TextView txtVoornaam = (TextView) findViewById(R.id.txtVoornaam);
        final TextView txtAchternaam = (TextView) findViewById(R.id.txtWachtwoordBetaling);
        final TextView txtGebuikersnaam = (TextView) findViewById(R.id.txtGerbuikersnaam);
        final TextView txtSaldo = (TextView) findViewById(R.id.txtSaldo);
        final Button btnTransacties = (Button) findViewById(R.id.btnTransacties);
        final Button btnWachtwoordV = (Button) findViewById(R.id.btnWachtwoordV);

        //Gegevens ophalen
        SharedPreferences settings = getSharedPreferences(USER_INFO, 0);
        String strVoornaam = settings.getString("voornaam", "");
        String strAchternaam = settings.getString("achternaam", "");
        String strGebruikersnaam = settings.getString("gebruikersnaam", "");
        String strWachtwoord = settings.getString("wachtwoord","");
        String strSaldo = settings.getString("saldo","").replace(".",",");
        strSaldo = "€" + strSaldo;


        //Textviews invullen
        txtAchternaam.setText(strAchternaam);
        txtVoornaam.setText(strVoornaam);
        txtGebuikersnaam.setText(strGebruikersnaam);
        txtSaldo.setText(strSaldo);

        //Wachtwoord veranderen
        btnWachtwoordV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WachtwoordVeranderen.class);
                startActivity(intent);
            }
        });

        //Transacties
        btnTransacties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Transacties.class);
                startActivity(intent);
            }
        });



    }
}

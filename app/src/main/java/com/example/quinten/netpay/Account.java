package com.example.quinten.netpay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Account extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        setTitle("Account");

        //Vars
        final TextView txtVoornaam = (TextView) findViewById(R.id.txtVoornaam);
        final TextView txtAchternaam = (TextView) findViewById(R.id.txtWachtwoord);
        final TextView txtGebuikersnaam = (TextView) findViewById(R.id.txtGerbuikersnaam);
        final TextView txtSaldo = (TextView) findViewById(R.id.txtSaldo);
        final Button btnTransacties = (Button) findViewById(R.id.btnTransacties);
        final Button btnWachtwoordV = (Button) findViewById(R.id.btnWachtwoordV);

        //Doorgegegeven vars
        Intent intent = getIntent();
        final String strVoornaam = intent.getStringExtra("voornaam");
        final String strAchternaam = intent.getStringExtra("achternaam");
        final String strGebruikersnaam = intent.getStringExtra("gebruikersnaam");
        final String strWachtwoord = intent.getStringExtra("wachtwoord");
        String strSaldo = intent.getStringExtra("saldo");
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
                intent.putExtra("wachtwoord", strWachtwoord);
                intent.putExtra("gebruikersnaam", strGebruikersnaam);
                startActivity(intent);
            }
        });




    }
}

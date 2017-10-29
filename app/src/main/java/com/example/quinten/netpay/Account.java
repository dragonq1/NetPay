package com.example.quinten.netpay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Account extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        //Vars
        final TextView txtVoornaam = (TextView) findViewById(R.id.txtVoornaam);
        final TextView txtAchternaam = (TextView) findViewById(R.id.txtAchternaam);

        //Doorgegegeven vars
        Intent intent = getIntent();
        final String strVoornaam = intent.getStringExtra("voornaam");
        final String strAchternaam = intent.getStringExtra("achternaam");

        //nodige Textviews uitschakelen
        txtAchternaam.setEnabled(false);
        txtVoornaam.setEnabled(false);

        //Textviews invullen
        txtAchternaam.setText(strAchternaam);
        txtVoornaam.setText(strVoornaam);


    }
}

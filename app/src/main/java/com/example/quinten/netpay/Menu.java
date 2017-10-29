package com.example.quinten.netpay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class Menu extends Activity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        //Vars
        final TextView txtWelkom =  findViewById(R.id.txtWelkom);
        final TextView txtWelkomActie = findViewById(R.id.txtWelkomActie);
        final Button btnBetalen = (Button) findViewById(R.id.btnBetalen);
        final Button btnOntvangen = (Button) findViewById(R.id.btnOntvangen);
        final Button btnAccount = (Button) findViewById(R.id.btnAccount);

        //Doorgegegeven vars
        Intent intent = getIntent();
        final String strVoornaam = intent.getStringExtra("voornaam");
        final String strAchternaam = intent.getStringExtra("achternaam");
        final String strGebruikersnaam = intent.getStringExtra("gebruikersnaam");
        final String strSaldo = intent.getStringExtra("saldo");

        //Welkom bericht personaliseren

        String strWelkomBericht = strVoornaam.substring(0,1).toUpperCase() + strVoornaam.substring(1) + "!";
        strWelkomBericht = strWelkomBericht.replace(" ", "");
        strWelkomBericht = "Welkom " + strWelkomBericht;
        txtWelkom.setText(strWelkomBericht);

        //Button listeners

        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Account.class);
                intent.putExtra("gebruikersnaam", strGebruikersnaam);
                intent.putExtra("voornaam", strVoornaam);
                intent.putExtra("achternaam", strAchternaam);
                intent.putExtra("saldo", strSaldo);
                startActivity(intent);
            }
        });





    }
}

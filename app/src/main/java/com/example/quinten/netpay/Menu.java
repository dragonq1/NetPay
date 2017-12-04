package com.example.quinten.netpay;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import static com.example.quinten.netpay.MainActivity.USER_INFO;

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

        //Gegevens ophalen
        SharedPreferences settings = getSharedPreferences(USER_INFO, 0);
        String strVoornaam = settings.getString("voornaam", "");
        String strAchternaam = settings.getString("achternaam", "");
        //String strID = settings.getString("ID", "");

        //Welkom bericht personaliseren
        String strWelkomBericht = strVoornaam.substring(0,1).toUpperCase() + strVoornaam.substring(1) + "!";
        strWelkomBericht = strWelkomBericht.replace(" ", "");
        strWelkomBericht = "Hallo " + strWelkomBericht;
        txtWelkom.setText(strWelkomBericht);

        //Button listeners
        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Account.class);
                startActivity(intent);
            }
        });

        btnBetalen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Betalen.class);
                startActivity(intent);
            }
        });





    }
}

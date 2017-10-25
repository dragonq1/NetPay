package com.example.quinten.netpay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

        //Welkom bericht personaliseren
        Intent intent = getIntent();
        String strVoornaam = intent.getStringExtra("voornaam");
        txtWelkom.setText("Welkom " + strVoornaam + "!");



    }
}

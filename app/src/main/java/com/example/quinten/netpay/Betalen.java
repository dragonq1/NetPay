package com.example.quinten.netpay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Betalen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_betalen);
        setTitle("Betaling");

        //Vars
        final Button btnQRCode = (Button) findViewById(R.id.btnQRCode);
        final Button btnManueel = (Button) findViewById(R.id.btnManueel);

        btnManueel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BetalingManueel.class);
                startActivity(intent);
            }
        });
    }
}

package com.example.quinten.netpay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Vars
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        Button btnReg = (Button) findViewById(R.id.btnRegister);
        final EditText txtGebruiksnaam = (EditText)findViewById(R.id.txtNaam);
        final EditText txtWachtwoord = (EditText)findViewById(R.id.txtWachtwoord);

        //Button listeners

        //Login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtGebruiksnaam.getText().toString().equals("") || txtWachtwoord.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Vul alle velden in!", Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = new Intent(getApplicationContext(), Menu.class);
                    startActivity(intent);
                }
            }

        });

        //Registeren
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Registreren.class);
                startActivity(intent);
            }

        });

    }
}

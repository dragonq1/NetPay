package com.example.quinten.netpay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quinten.methods.InternetStatus;
import com.example.quinten.methods.RefreshUserData;
import com.example.quinten.methods.refreshToken;
import com.google.firebase.iid.FirebaseInstanceId;

import static com.example.quinten.netpay.MainActivity.USER_INFO;

public class Menu extends Activity {

    //Uitloggen back button override
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder((Menu.this));
        builder.setTitle("Ben je zeker dat je wilt uitloggen?").setNegativeButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Info resetten
                SharedPreferences settings = getSharedPreferences(USER_INFO, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.clear();
                editor.apply();
                finish();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);



        //Vars
        final TextView txtWelkom =  findViewById(R.id.txtWelkom);
        //final TextView txtWelkomActie = findViewById(R.id.txtWelkomActie);
        final Button btnBetalen = findViewById(R.id.btnBetalen);
        final Button btnOntvangen = findViewById(R.id.btnOntvangen);
        final Button btnAccount = findViewById(R.id.btnAccount);
        final Button btnUitloggen = findViewById(R.id.btnUitloggen);

        //Gegevens ophalen
        SharedPreferences settings = getSharedPreferences(USER_INFO, 0);
        String strVoornaam = settings.getString("voornaam", "");
        final String strGebruikersnaam = settings.getString("gebruikersnaam", "");
        //String strAchternaam = settings.getString("achternaam", "");
        //String strID = settings.getString("ID", "");

        //Token refreshen
        String idToken = FirebaseInstanceId.getInstance().getToken();
        refreshToken refreshToken = new refreshToken();
        refreshToken.refreshidToken(idToken, strGebruikersnaam, getApplicationContext());

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
                intent.putExtra("ActionButton", "false");
                startActivity(intent);
            }
        });

        btnOntvangen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(getApplicationContext(), OntvangenInfo.class);
                startActivity(intent);
            }
        });

        btnUitloggen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder((Menu.this));
                builder.setTitle("Ben je zeker dat je wilt uitloggen?").setNegativeButton("Ja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Info resetten
                        SharedPreferences settings = getSharedPreferences(USER_INFO, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.clear();
                        editor.apply();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
        });

        findViewById(R.id.FABBetalen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Betalen.class);
                intent.putExtra("ActionButton", "true");
                startActivity(intent);
            }
        });



        findViewById(R.id.FABRefresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InternetStatus IS = new InternetStatus();
                boolean is = IS.getInternetStatus(getApplicationContext());
                if(is) {
                    RefreshUserData refreshUserData = new RefreshUserData();
                    refreshUserData.refreshData(strGebruikersnaam, getApplicationContext());
                }else{
                    Toast.makeText(getApplicationContext(), "Geen internet verbinding!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}

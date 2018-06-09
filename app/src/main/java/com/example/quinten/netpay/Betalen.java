package com.example.quinten.netpay;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.quinten.requests.BetalingQRRequest;
import com.example.quinten.requests.QRCODEInfoRequest;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.quinten.netpay.MainActivity.USER_INFO;

public class Betalen extends AppCompatActivity {

    //Snack notificatie
    public void sendSnackbar(String strBericht, View v) {
        Snackbar snackbar = Snackbar.make(v, strBericht, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Menu.class);
        startActivity(intent);
    }

    //Vars
    private String[] _QRCodeInfo = new String[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_betalen);
        setTitle("Betaling");

        if(getIntent().getStringExtra("ActionButton") != null && getIntent().getStringExtra("ActionButton").equals("true")) {
            getIntent().putExtra("ActionButton", "false");
            openCamera();
        }

        //Vars
        final Button btnQRCode = (Button) findViewById(R.id.btnQRCode);
        final Button btnManueel = (Button) findViewById(R.id.btnManueel);



        //Manuel betalen
        btnManueel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BetalingManueel.class);
                startActivity(intent);
            }
        });


        //Betaling via scannen qr code

        //Camera openen
        btnQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });
    }

    public void openCamera() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan de QR-code");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    //Kijken voor resultaat QR-code
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(!(result == null)) {
            if(result.getContents() == null) {
                //Geen resultaat -> niets
            }else{
                if(result.getContents().startsWith("2247")) {
                    try {
                        getQRCodeInfo(result.getContents());
                    }catch(Exception e) {
                        Log.e("ERROR", "onActivityResult: " + e.getMessage());
                        sendSnackbar("Oeps, er ging iets fout!", findViewById(android.R.id.content));
                    }
                }else{
                    sendSnackbar("QR-code ongeldig!", findViewById(android.R.id.content));
                }

            }
        }else{
            sendSnackbar("Geen QR-code gevonden!", findViewById(android.R.id.content));
        }
    }

    //QR-code nakijken en info ophalen uit database
    public void getQRCodeInfo(String QRCode) {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonReponse = new JSONObject(response);
                    boolean success = jsonReponse.getBoolean("success");

                    if (success) {
                        //Terugekregen betaling info ophalen

                            String strBedrag = jsonReponse.getString("Bedrag");
                            String strGebruikersnaam = jsonReponse.getString("Gebruikersnaam");
                            String strNaam = jsonReponse.getString("Naam");
                            String stridCode = jsonReponse.getString("idCode");



                            _QRCodeInfo[0] = strBedrag;
                            _QRCodeInfo[1] = strGebruikersnaam;
                            _QRCodeInfo[2] = strNaam;
                            _QRCodeInfo[3] = stridCode;

                            AlertDialog(_QRCodeInfo);


                    }else if(jsonReponse.getString("error").equals("empty")) {
                        sendSnackbar("Geen betaling gevonden!", findViewById(android.R.id.content));
                    }

                    else {
                        if (jsonReponse.getString("error").isEmpty()) {
                            Log.e("ERROR", "onResponse: " + jsonReponse.getString("success"));
                            sendSnackbar("Oeps!, er ging iets fout!", findViewById(android.R.id.content));
                        }else{
                            Log.e("ERROR", "onResponse: " + jsonReponse.getString("success"));
                            sendSnackbar("Oeps!, er ging iets fout!", findViewById(android.R.id.content));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("ERROR", "onResponse: " + e.getMessage());
                    sendSnackbar("Oeps!, er ging iets fout!", findViewById(android.R.id.content));

                }

            }
        };

        QRCODEInfoRequest QRCodeInfoRequest = new QRCODEInfoRequest(QRCode, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Betalen.this);
        queue.add(QRCodeInfoRequest);

    }

    //Betaling methode
    public void betaling(String gbnOntvanger, final String bedrag, String qrCode, String wachtwoord) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String success = jsonResponse.getString("success");

                    switch(success) {
                        case "success":
                            Toast.makeText(getApplicationContext(), "Betaling succesvol!", Toast.LENGTH_LONG).show();


                            //Saldo aanpassen
                            SharedPreferences settings = getSharedPreferences(USER_INFO, 0);
                            SharedPreferences.Editor editor = settings.edit();
                            double dblBedrag = Double.parseDouble(bedrag);
                            double dblSaldo = Double.parseDouble(settings.getString("saldo", ""));

                            String strNieuwSaldo =  String.valueOf((dblSaldo - dblBedrag));
                            editor.putString("saldo",  strNieuwSaldo);
                            editor.apply();


                            //Terug naar betaling activity gaan
                            Intent intent = new Intent(getApplicationContext(), Betalen.class);
                            startActivity(intent);

                            break;
                        case "Statement 2":
                            Log.e("ERROR", "onResponse: statement 3");
                            sendSnackbar("Oeps!, er ging iets fout!", findViewById(android.R.id.content));
                            break;
                        case "Statement 3":
                            Log.e("ERROR", "onResponse: statement 3");
                            sendSnackbar("Oeps!, er ging iets fout!", findViewById(android.R.id.content));
                            break;
                        case "false":
                            Log.e("ERROR", "onResponse: false");
                            sendSnackbar("Oeps!, er ging iets fout!", findViewById(android.R.id.content));
                            break;
                        case "wachtwoord":
                            sendSnackbar("Wachtwoord incorrect!", findViewById(android.R.id.content));
                            AlertDialog(_QRCodeInfo);
                            break;
                        default:
                            Log.e("ERROR", "onResponse: " + success);
                            sendSnackbar("Oeps!, er ging iets fout!", findViewById(android.R.id.content));
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "ERROR 4" + " " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        };

        SharedPreferences settings = getSharedPreferences(USER_INFO, 0);
        String gebruikersnaamBetaler = settings.getString("gebruikersnaam", "");

        BetalingQRRequest betalingQRRequest = new BetalingQRRequest(gbnOntvanger, gebruikersnaamBetaler, bedrag, qrCode, wachtwoord, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Betalen.this);
        queue.add(betalingQRRequest);
    }

    //Alert dialog creeëren en tonen
    public void AlertDialog(final String[] QRCodeInfo) {

        try{
            final String strBedrag = QRCodeInfo[0];
            final String strBedragTV = strBedrag.replace(".", ",") + " euro";
            final String strGebruikersnaam = QRCodeInfo[1];
            String strNaam = QRCodeInfo[2].replace("  ", " ");
            final String stridCode = QRCodeInfo[3];

            //Saldo en naam checken + vars
            final SharedPreferences settings = getSharedPreferences(USER_INFO, 0);
            double dblSaldo = Double.parseDouble(settings.getString("saldo", ""));
            double dblBedrag = Double.parseDouble(strBedrag);
            String strgbnBetaler = settings.getString("gebruikersnaam", "");

            if(dblSaldo < dblBedrag) {
                sendSnackbar("Saldo ontoerijkend!", findViewById(android.R.id.content));
            }
            else if(strgbnBetaler.equals(strGebruikersnaam)) {
                sendSnackbar("Je kan geen betaling aan jezelf doen!", findViewById(android.R.id.content));
            }else{

                View v = getLayoutInflater().inflate(R.layout.dialogbox_betaling, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(Betalen.this);

                TextView tvBedrag = v.findViewById(R.id.txtBedragBetalingQR);
                TextView tvOntvanger = v.findViewById(R.id.txtBegunstigdeBetalingQR);
                final EditText txtWachtwoord = v.findViewById(R.id.txtWachtwoordBetalingQR);

                builder.setTitle("Nieuwe betaling");
                tvBedrag.setText(strBedragTV);
                tvOntvanger.setText(strNaam);



                builder.setPositiveButton("Bevestigen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Wachtwoord en veld nakijken
                        if(txtWachtwoord.getText().toString().equals("")) {
                            sendSnackbar("Bevestig de betaling met je wachtwoord!", findViewById(android.R.id.content));
                            AlertDialog(QRCodeInfo);
                        }else{
                            String strWachtwoord = txtWachtwoord.getText().toString();
                            betaling(strGebruikersnaam, strBedrag, stridCode, strWachtwoord);
                        }

                    }
                }).setNegativeButton("Annuleren", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Niets
                    }
                });


                builder.setView(v);

                AlertDialog dialog = builder.create();
                dialog.show();
            }

        }catch(Exception e) {
            e.printStackTrace();
            Log.e("ERROR", "AlertDialog: " + e.getMessage());
            sendSnackbar("Oeps!, er ging iets fout!", findViewById(android.R.id.content));
        }




    }

}

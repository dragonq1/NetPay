package com.example.quinten.netpay;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.quinten.requests.QRCODEInfoRequest;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class Betalen extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Menu.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_betalen);
        setTitle("Betaling");

        //Vars
        final Button btnQRCode = (Button) findViewById(R.id.btnQRCode);
        final Button btnManueel = (Button) findViewById(R.id.btnManueel);
        final Activity activity = this;


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
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan de QR-code");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.setOrientationLocked(true);
                integrator.initiateScan();

            }
        });
    }

    //Kijken voor resultaat QR-code
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(!(result == null)) {
            if(result.getContents() == null) {
                //Geen resultaat -> niets
            }else{
                try {
                     getQRCodeInfo(result.getContents());
                }catch(Exception e) {
                    Toast.makeText(getApplicationContext(), "ERROR 2!" + " " +  e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }else{
            Toast.makeText(getApplicationContext(), "Geen QR-code gevonden!", Toast.LENGTH_LONG).show();
        }
    }

    //QR-code nakijken en info ophalen uit database
    public void getQRCodeInfo(String QRCode) {

        //Vars

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonReponse = new JSONObject(response);
                    boolean success = jsonReponse.getBoolean("success");

                    if (success) {
                        Toast.makeText(getApplicationContext(), "Test 1", Toast.LENGTH_LONG).show();
                        //Terugekregen betaling info ophalen
                        String strBedrag = jsonReponse.getString("Bedrag");

                        String strGebruikersnaam = jsonReponse.getString("Gebruikersnaam");
                        String strNaam = jsonReponse.getString("Naam");
                        String stridCode = jsonReponse.getString("idCode");

                        Toast.makeText(getApplicationContext(), "Test 2", Toast.LENGTH_LONG).show();


                        String[] QRCodeInfo = new String[4];
                        QRCodeInfo[0] = strBedrag;
                        QRCodeInfo[1] = strGebruikersnaam;
                        QRCodeInfo[2] = strNaam;
                        QRCodeInfo[3] = stridCode;

                        AlertDialog(QRCodeInfo);

                    } else {
                        if (jsonReponse.getString("success").isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Er ging iets fout!", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Er ging iets fout!" + " " +jsonReponse.getString("success"), Toast.LENGTH_LONG).show();
                        }
                    }

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "ERROR 9!" + " " +  e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        };

        QRCODEInfoRequest QRCodeInfoRequest = new QRCODEInfoRequest(QRCode, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Betalen.this);
        queue.add(QRCodeInfoRequest);

    }

    //Alert dialog creeëren en tonen
    public void AlertDialog(String[] QRCodeInfo) {

        try{
            String strBedrag = QRCodeInfo[0].replace(".", ",") + " euro";
            String strGebruikersnaam = QRCodeInfo[1];
            String strNaam = QRCodeInfo[2].replace("  ", " ");
            String stridCode = QRCodeInfo[3];

            View v = getLayoutInflater().inflate(R.layout.dialogbox_betaling, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(Betalen.this);

            TextView tvBedrag =  v.findViewById(R.id.txtBedragBetalingQR);
            TextView tvOntvanger =  v.findViewById(R.id.txtBegunstigdeBetalingQR);

            builder.setTitle("Nieuwe betaling");
            tvBedrag.setText(strBedrag);
            tvOntvanger.setText(strNaam);

            builder.setPositiveButton("Bevestigen", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).setNegativeButton("Annuleren", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });



            builder.setView(v);

            AlertDialog dialog = builder.create();
            dialog.show();

        }catch(Exception e) {
            Toast.makeText(getApplicationContext(), "ERROR 3!" + " " +  e.getMessage(), Toast.LENGTH_LONG).show();
        }




    }
}

package com.example.quinten.netpay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.quinten.requests.QRCodeRequest;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import static com.example.quinten.netpay.MainActivity.USER_INFO;

public class OntvangenInfo extends AppCompatActivity {


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ontvangen_info);
        setTitle("Nieuwe betaling");


        //Vars
        final EditText txtBedrag = findViewById(R.id.txtBedragQR);
        final Button btnGenerate = findViewById(R.id.btnGenerate);
        final ProgressBar prgQRCode = findViewById(R.id.prgQRCode);
        final ImageView LVQRCode = findViewById(R.id.lvQRCode);
        prgQRCode.setVisibility(View.INVISIBLE);

        //Gegevens gebruiker ophalen
        final SharedPreferences settings = getSharedPreferences(USER_INFO, 0);
        final String strGebruikersnaam = settings.getString("gebruikersnaam", "");

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtBedrag.getText().toString().equals("")) {
                    sendSnackbar("Gelieve een bedrag in te vullen!", findViewById(android.R.id.content));
                    LVQRCode.setImageBitmap(null);
                }else{
                    prgQRCode.setVisibility(View.VISIBLE);
                    //Code genereren
                    Random random = new Random();
                    int randomCode = random.nextInt(5000) + 100;
                    final String qrcode = ("2247-" + strGebruikersnaam + "-" + txtBedrag.getText().toString() + "-" +randomCode);

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonReponse = new JSONObject(response);
                                boolean success = jsonReponse.getBoolean("success");

                                if (success) {

                                    //QR code genereren
                                try {
                                    MultiFormatWriter multiF = new MultiFormatWriter();
                                    BitMatrix bitMatrix = multiF.encode(qrcode, BarcodeFormat.QR_CODE, 500, 500);
                                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                                    LVQRCode.setImageBitmap(bitmap);

                                }catch(WriterException e) {
                                    Log.e("ERROR", "onResponse: " + e.getMessage());
                                    sendSnackbar("Er ging iets fout!", findViewById(android.R.id.content));
                                    LVQRCode.setImageBitmap(null);
                                }



                                    sendSnackbar("QR-Code gegenereerd!", findViewById(android.R.id.content));
                                    //ProgressSpiner onzichtbaar maken
                                    prgQRCode.setVisibility(View.INVISIBLE);

                                } else {
                                    Log.e("ERROR", "onResponse: " + jsonReponse.getString("success"));
                                    sendSnackbar("Er ging iets fout!", findViewById(android.R.id.content));
                                    LVQRCode.setImageBitmap(null);
                                    //ProgressSpiner onzichtbaar maken
                                    prgQRCode.setVisibility(View.INVISIBLE);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                sendSnackbar("Er ging iets fout!", findViewById(android.R.id.content));
                                LVQRCode.setImageBitmap(null);
                                //ProgressSpiner onzichtbaar maken
                                prgQRCode.setVisibility(View.INVISIBLE);
                            }

                        }
                    };

                    QRCodeRequest qrCodeRequest = new QRCodeRequest(strGebruikersnaam, txtBedrag.getText().toString(), qrcode, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(OntvangenInfo.this);
                    queue.add(qrCodeRequest);
                }
            }
        });

        }
    }



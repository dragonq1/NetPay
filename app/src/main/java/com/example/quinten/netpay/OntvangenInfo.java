package com.example.quinten.netpay;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ontvangen_info);


        //Vars
        final EditText txtBedrag = (EditText) findViewById(R.id.txtBedragQR);
        final Button btnGenerate = (Button) findViewById(R.id.btnGenerate);
        final ProgressBar prgQRCode = (ProgressBar) findViewById(R.id.prgQRCode);
        final ImageView LVQRCode = (ImageView) findViewById(R.id.lvQRCode);
        prgQRCode.setVisibility(View.INVISIBLE);

        //Gegevens gebruiker ophalen
        final SharedPreferences settings = getSharedPreferences(USER_INFO, 0);
        final String strGebruikersnaam = settings.getString("gebruikersnaam", "");

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtBedrag.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Gelieve een bedrag in te vullen!", Toast.LENGTH_LONG).show();
                }else{
                    prgQRCode.setVisibility(View.VISIBLE);
                    //Code genereren
                    Random random = new Random();
                    int randomCode = random.nextInt(500) + 100;
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
                                    Toast.makeText(getApplicationContext(), "FOUT" + e.getMessage(), Toast.LENGTH_LONG).show();
                                }



                                    Toast.makeText(getApplicationContext(), "Het werkt!!!", Toast.LENGTH_LONG).show();
                                    //ProgressSpiner onzichtbaar maken
                                    prgQRCode.setVisibility(View.INVISIBLE);

                                } else {
                                    Toast.makeText(getApplicationContext(), "FOUT " + jsonReponse.getString("success"), Toast.LENGTH_LONG).show();
                                    //ProgressSpiner onzichtbaar maken
                                    prgQRCode.setVisibility(View.INVISIBLE);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "ERROR!" + " " +  e.getMessage(), Toast.LENGTH_LONG).show();
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



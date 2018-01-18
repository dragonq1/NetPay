package com.example.quinten.netpay;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.quinten.requests.TransactiesRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.StringTokenizer;

import static com.example.quinten.netpay.MainActivity.USER_INFO;

public class Transacties extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transacties);
        setTitle("Transacties");

        //Vars
        final Button btnTraAct = (Button) findViewById(R.id.btnTransacties);
        final ListView lvLijst = (ListView) findViewById(R.id.lvLijst);

        final ArrayList<String> strLijstDatum = new ArrayList<>();
        final ArrayList<String> strLijstBetaler = new ArrayList<>();
        final ArrayList<SpannableString> strLijstBedrag = new ArrayList<>();

        SharedPreferences settings = getSharedPreferences(USER_INFO, 0);
        final String strVoornaam = settings.getString("voornaam", "");
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.prgTrans);

        //Progresbar ontzichtbaar maken
        progressBar.setVisibility(View.INVISIBLE);



        btnTraAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Listview leegmaken
                strLijstBedrag.clear();
                strLijstBetaler.clear();
                strLijstDatum.clear();

                //Transacties ophalen
                //Progresbar zichtbaar maken
                progressBar.setVisibility(View.VISIBLE);
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                int intTeller = 0;
                                while ((jsonResponse.length() -1) > intTeller){
                                    ///gegevens ophalen
                                    String strResp = jsonResponse.getString(String.valueOf(intTeller));
                                    StringTokenizer st = new StringTokenizer(strResp, "-");
                                    String strBedrag = st.nextToken() + "EUR";
                                    String strOntvanger = st.nextToken().trim();
                                    String strBetaler = st.nextToken().trim();
                                    String strDatumDag = st.nextToken();
                                    int intDatumMaand = Integer.parseInt(st.nextToken());

                                    //Datum omzetten
                                    String strDatumMaand = new DateFormatSymbols().getMonths()[intDatumMaand-1];
                                    String strDatum = (strDatumDag + " " + strDatumMaand);

                                    //Geld ontvangen
                                    if(strVoornaam.equals(strOntvanger)) {

                                        //Tekst kleur geven
                                        strBedrag = "-" + strBedrag;
                                        SpannableString ssBedrag =  new SpannableString(strBedrag);
                                        ssBedrag.setSpan(new ForegroundColorSpan(Color.parseColor("#b21515")), 0, strBedrag.length(), 0);
                                        strLijstBedrag.add(ssBedrag);

                                        strLijstBetaler.add(strBetaler);
                                        strLijstDatum.add(strDatum);

                                    //Betaling
                                    }else{
                                        //Tekst kleur geven
                                        strBedrag = "+" + strBedrag;
                                        SpannableString ssBedrag =  new SpannableString(strBedrag);
                                        ssBedrag.setSpan(new ForegroundColorSpan(Color.parseColor("#20a02f")), 0, strBedrag.length(), 0);
                                        strLijstBedrag.add(ssBedrag);

                                        strLijstBetaler.add(strOntvanger);
                                        strLijstDatum.add(strDatum);
                                    }

                                    //Arrays omzetten naar string arrays
                                    SpannableString[] strLijstBedragen = new SpannableString[strLijstBedrag.size()];
                                    strLijstBedragen = strLijstBedrag.toArray(strLijstBedragen);

                                    String[] strLijstOntvanger = new String[strLijstBetaler.size()];
                                    strLijstOntvanger = strLijstBetaler.toArray(strLijstOntvanger);

                                    String[] strLijstDatums = new String[strLijstDatum.size()];
                                    strLijstDatums = strLijstDatum.toArray(strLijstDatums);

                                    ListViewAdapter Adapter = new ListViewAdapter(Transacties.this, strLijstOntvanger, strLijstBedragen, strLijstDatums);
                                    lvLijst.setAdapter(Adapter);
                                    intTeller++;
                                }
                                //Progresbar ontzichtbaar maken
                                progressBar.setVisibility(View.INVISIBLE);
                            } else {
                                Toast.makeText(getApplicationContext(), "Fout bij ophalen van transacties!", Toast.LENGTH_LONG).show();
                                //Progresbar ontzichtbaar maken
                                progressBar.setVisibility(View.INVISIBLE);
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "ERROR!" + " " + e.getMessage(), Toast.LENGTH_LONG).show();
                            //Progresbar ontzichtbaar maken
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                    }
                };

                SharedPreferences settings = getSharedPreferences(USER_INFO, 0);
                String strID = settings.getString("ID","");

                TransactiesRequest transactiesRequest = new TransactiesRequest(strID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Transacties.this);
                queue.add(transactiesRequest);
            }
        });
        //Progresbar ontzichtbaar maken
        progressBar.setVisibility(View.INVISIBLE);


    }
}


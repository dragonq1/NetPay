package com.example.quinten.netpay;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

public class Menu extends Activity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        //Vars
        Button btnOntvangen = (Button) findViewById(R.id.btnOntvangen);

    }
}

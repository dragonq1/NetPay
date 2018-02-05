package com.example.quinten.requests;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class BetalingQRRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL = "http://37.34.58.100/~dragv/BetalingQR.php";
    private Map<String, String> params;

    public BetalingQRRequest(String gebruikersnaamOntvanger, String gebruikersnaamBetaler, String bedrag, String qrcode, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("gebruikersnaamOntvanger", gebruikersnaamOntvanger);
        params.put("gebruikersnaamBetaler", gebruikersnaamBetaler);
        params.put("bedrag", bedrag);
        params.put("qrcode", qrcode);
    }

    @Override
    public Map<String, String> getParams() { return params;
    }


}

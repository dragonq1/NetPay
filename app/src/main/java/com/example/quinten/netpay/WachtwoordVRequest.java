package com.example.quinten.netpay;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class WachtwoordVRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL = "http://37.34.58.100/~dragv/wachtwoordv.php";
    private Map<String, String> params;

    public WachtwoordVRequest(String wachtwoord, String gebruikersnaam, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("wachtwoord", wachtwoord);
        params.put("gebruikersnaam", gebruikersnaam);
    }
    @Override
    public Map<String, String> getParams() { return params;
    }

}

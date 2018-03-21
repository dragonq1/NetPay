package com.example.quinten.requests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class WachtwoordVRequest extends StringRequest {

    //Test
    //Test2

    private static final String REGISTER_REQUEST_URL = "http://185.114.157.174/~dragv/wachtwoordv.php";
    private Map<String, String> params;

    public WachtwoordVRequest(String oudwachtwoord, String nieuwwachtwoord, String gebruikersnaam, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("oudwachtwoord", oudwachtwoord);
        params.put("nieuwwachtwoord", nieuwwachtwoord);
        params.put("gebruikersnaam", gebruikersnaam);
    }
    @Override
    public Map<String, String> getParams() { return params;
    }

}

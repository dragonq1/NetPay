package com.example.quinten.requests;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL = "http://185.114.157.174/~dragv/register.php";
    private Map<String, String> params;

    public RegisterRequest(String gebruikersnaam, String voornaam, String achternaam, String wachtwoord, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("gebruikersnaam", gebruikersnaam);
        params.put("voornaam", voornaam);
        params.put("achternaam", achternaam);
        params.put("wachtwoord", wachtwoord);
    }

    @Override
    public Map<String, String> getParams() { return params;
    }

}

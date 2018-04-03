package com.example.quinten.requests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class FirebaseTokenRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL = "http://185.114.157.174/~dragv/TokenRefresh.php";
    private Map<String, String> params;

    public FirebaseTokenRequest(String gebruikersnaam, String token, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("gebruikersnaam", gebruikersnaam);
        params.put("token", token);
    }


    @Override
    public Map<String, String> getParams() { return params;
    }

}

package com.example.quinten.requests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RefreshRequest extends StringRequest {

    private static final String LOGIN_REQUEST_URL = "http://185.114.157.174/~dragv/Refresh.php";
    private Map<String, String> params;

    public RefreshRequest(String gebruikersnaam, Response.Listener<String> listener) {
        super(Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("gebruikersnaam", gebruikersnaam);
    }

    @Override
    public Map<String, String> getParams() { return params;
    }

}

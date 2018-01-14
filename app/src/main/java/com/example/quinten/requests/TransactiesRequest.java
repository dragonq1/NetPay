package com.example.quinten.requests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class TransactiesRequest extends StringRequest {

    private static final String TRANSACTIES_REQUEST_URL_REQUEST_URL = "http://37.34.58.100/~dragv/transacties.php";
    private Map<String, String> params;

    public TransactiesRequest(String intID, Response.Listener<String> listener) {
        super(Method.POST, TRANSACTIES_REQUEST_URL_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("ID", intID);
    }
    @Override
    public Map<String, String> getParams() { return params;
    }

}

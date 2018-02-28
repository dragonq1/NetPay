package com.example.quinten.requests;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class QRCODEInfoRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL = "http://185.114.157.174/~dragv/QRCodeRequest.php";
    private Map<String, String> params;

    public QRCODEInfoRequest(String idCode, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("idCode", idCode);
    }
    @Override
    public Map<String, String> getParams() { return params;
    }
}

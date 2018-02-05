package com.example.quinten.netpay;

import java.net.InetAddress;

public class InternetVerbinding {

    public boolean getInternetStatus() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }

    }
}

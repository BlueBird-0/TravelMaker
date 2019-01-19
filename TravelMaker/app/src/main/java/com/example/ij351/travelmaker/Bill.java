package com.example.ij351.travelmaker;


import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class Bill {
    public String documentId;
    public String uri;
    public Timestamp time;

    public Bill(String documentId, String uri, Timestamp time)
    {
        this.documentId= documentId;
        this.uri = uri;
        this.time = time;
    }

    public Map<String, Object> getHashMap() {
        Map<String, Object> data = new HashMap<>();
        data.put("time", time);
        data.put("uri", uri);
        return data;
    }
}

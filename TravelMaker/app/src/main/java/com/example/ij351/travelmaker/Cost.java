package com.example.ij351.travelmaker;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class Cost {
    public double cost;
    public Timestamp timestamp;
    public String content;

    public Cost(double cost, Timestamp timestamp, String content)
    {
        this.cost = cost;
        this.timestamp = timestamp;
        this.content = content;
    }


    public Map<String, Object> getHashMap() {
        Map<String, Object> data = new HashMap<>();
        data.put("content", content);
        data.put("time", timestamp);
        data.put("cost", cost);
        return data;
    }
}

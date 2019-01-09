package com.example.ij351.travelmaker;

import com.google.firebase.Timestamp;

import org.w3c.dom.Comment;

import java.util.HashMap;
import java.util.Map;

public class Content {
    public String uid; //문서 uid
    public String comment; //내용
    public Timestamp time;   //작성시간
    public String writer;  //작성자

    public Content(String uid, String comment, Timestamp time, String writer)
    {
        this.uid = uid;
        this.comment = comment;
        this.time = time;
        this.writer = writer;
    }

    public Content(String comment)
    {
        this.comment = comment;
        this.time = Timestamp.now();
        this.writer = User.getFirebaseUser().getUid();
    }

    public Map<String, Object> getHashMap() {
        Map<String, Object> data = new HashMap<>();
        data.put("comment", comment);
        data.put("time", time);
        data.put("writer", writer);
        return data;
    }
}

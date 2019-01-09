package com.example.ij351.travelmaker;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

//여행 방 관리 클래스
//FireStore  이용
public class TravelRoom {
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static String roomId;
    private static String TAG = "LoginActivity";

    TravelRoom()
    {
        db = FirebaseFirestore.getInstance();
    }


    // 새로운 여행 방 생성
    public static void createNewRoom() {
        Map<String, Object> data = new HashMap<>();
        data.put("part", "me");

        db.collection("Travels")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "방만들기 성공");


                        //Participants 생성
                        User me = new User();
                        me.uid = User.getFirebaseUser().getUid();
                        me.name = User.getFirebaseUser().getDisplayName();
                        me.email = User.getFirebaseUser().getEmail();

                        documentReference.collection("Participants")
                                .document(me.uid)
                                .set(me.getHashMap())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "참가자 메뉴 생성");
                                    }
                                });

                        //CheckLists 생성
                        Map<String, Object> checkData = new HashMap<>();
                        checkData.put("정보", "me");
                        documentReference.collection("CheckLists")
                                .document("UserName")
                                .set(checkData)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "참가자 메뉴 생성");
                                    }
                                });


                        //Costs 생성
                        Map<String, Object> costData = new HashMap<>();
                        costData.put("정보", "me");
                        documentReference.collection("Costs")
                                .document("UserName")
                                .set(costData)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "참가자 메뉴 생성");
                                    }
                                });
                    }
                });
    }

    // 글 작성
    public static void writeContent(String title, String comment) {
        Content data = new Content(comment);

        roomId = "xBvXhUxaCAoTRIMnuWcG";

        db.collection("Travels").document(roomId).collection("CheckLists").document(title).collection("contents")
                .add(data.getHashMap())
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "글쓰기 성공");
                    }
                });
    }
}


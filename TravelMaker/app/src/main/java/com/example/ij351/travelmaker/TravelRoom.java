package com.example.ij351.travelmaker;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

//여행 방 관리 클래스
//FireStore  이용
public class TravelRoom {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String RoomId;
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
                        Map<String, Object> data = new HashMap<>();
                        data.put("정보", "me");
                        documentReference.collection("Participants")
                                .document("UserName")
                                .set(data)
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
                                .set(data)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "참가자 메뉴 생성");
                                    }
                                });
                    }
                });
    }

}

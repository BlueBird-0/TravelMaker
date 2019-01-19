package com.example.ij351.travelmaker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    public static int numPeopleInRoom = 1;
    private static String TAG = "LoginActivity";

    TravelRoom()
    {
        db = FirebaseFirestore.getInstance();
    }


    // 새로운 여행 방 생성
    public static void createNewRoom(OnCompleteListener<DocumentReference> listener) {
        Map<String, Object> data = new HashMap<>();
        data.put("make", Timestamp.now());

        db.collection("Travels")
                .add(data)
                .addOnCompleteListener(listener)
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
                                .set(me.getHashMap());
                    }
                });
    }

    // 방 입장
    public static void entryRoom(final String id, OnCompleteListener<DocumentSnapshot> listener)
    {
        db.collection("Travels").document(id)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.getData() != null) {
                            roomId = id;
                            Log.d(TAG, "방 입장 : " + roomId);


                            //Participants 생성
                            User me = new User();
                            me.uid = User.getFirebaseUser().getUid();
                            me.name = User.getFirebaseUser().getDisplayName();
                            me.email = User.getFirebaseUser().getEmail();
                            db.collection("Travels").document(id).collection("Participants")
                                    .document(me.uid)
                                    .set(me.getHashMap());
                        }
                        else{
                        }
                    }
                })
                .addOnCompleteListener(listener);
    }
    // 방 확인
    public static boolean hasRoom()
    {
        if(TravelRoom.roomId == null || TravelRoom.roomId.trim().length() == 0 || TravelRoom.roomId.equals(""))
        {
            return false;
        }
        return true;
    }

    public static void createCheckList(String title)
    {
        Map<String, Object> data = new HashMap<>();
        data.put("make", Timestamp.now());
        data.put("writer", User.getFirebaseUser().getDisplayName());
        db.collection("Travels").document(roomId).collection("CheckLists").document(title)
                .set(data);
    }
    public static void deleteCheckList(String title, String documentId)
    {
        db.collection("Travels").document(roomId).collection("CheckLists").document(title)
                .collection("contents").document(documentId).delete();
    }



    // 글 작성
    public static void writeContent(String title, String comment) {
        Content data = new Content(comment, title);
        db.collection("Travels").document(roomId).collection("CheckLists").document(title).collection("contents")
                .add(data.getHashMap());
    }
    public static void deleteContent(String title)
    {
        db.collection("Travels").document(roomId).collection("CheckLists").document(title).delete();
    }

    //글작성
    public static void writeCost(String content, Double cost) {
        Cost data = new Cost(cost, Timestamp.now(), content);
        db.collection("Travels").document(roomId).collection("Costs")
                .add(data.getHashMap());
    }
    public static void deleteCost(String documentId)
    {
        db.collection("Travels").document(roomId).collection("Costs").document(documentId).delete();
    }
}


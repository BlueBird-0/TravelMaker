package com.example.ij351.travelmaker;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
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
    public static String roomId = "uhZTAheSeMEi0A7M5nry";
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

                        //CheckLists 생성
                        Map<String, Object> checkData = new HashMap<>();
                        checkData.put("정보", "me");
                        documentReference.collection("CheckLists")
                                .document("UserName")
                                .set(checkData);
                    }
                });
    }

    // 방 입장
    public static void entryRoom(String id)
    {
        roomId = id;
        Log.d(TAG,"방 입장 : "+roomId);
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


package com.example.ij351.travelmaker;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class User {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static FirebaseUser user = mAuth.getCurrentUser();
    private static String TAG = "UserManager";

    //User 정보 클래스
    public String uid;
    public String name;
    public String phoneNumber;
    public String email;
    public boolean hasVISA;
    public boolean hasPassport;
    public User() {
        this.hasVISA = false;
        this.hasPassport = false;
    }
    public User(String uid, String name, boolean hasPassport, boolean hasVISA, String email)
    {
        this.uid = uid;
        this.name = name;
        this.hasPassport = hasPassport;
        this.hasVISA = hasVISA;
        this.email = email;
    }

    public User(String uid, String name, String phoneNumber, String email) {
        this.uid = uid;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.hasVISA = false;
        this.hasPassport = false;
    }

    public Map<String, Object> getHashMap() {
        Map<String, Object> data = new HashMap<>();
        data.put("uid", uid);
        data.put("name", name);
        data.put("phoneNumber", phoneNumber);
        data.put("email", email);
        data.put("hasVISA", hasVISA);
        data.put("hasPassport", hasPassport);
        return data;
    }


    //로그아웃
    public static void logoutUser() {
        mAuth.signOut();
        user = null;
    }

    //로그인
    public static void loginUser(String email, String password, OnCompleteListener<AuthResult> listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        user = mAuth.getCurrentUser();
                    }
                })
            .addOnCompleteListener(listener);
    }

    //로그인 상태 체크
    public static boolean checkLogined() {
        if (user != null) {
            Log.d(TAG, "로그인 상태 체크 : O 로그인");
            return true;
        } else {
            Log.d(TAG, "로그인 상태 체크 : X 로그아웃");
            return false;
        }
    }

    //회원가입
    public static void createUser(final String email, final String name, String password, OnCompleteListener<AuthResult> listener) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        user = mAuth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name).build();
                        user.updateProfile(profileUpdates);
                        //Authentication 회원가입 성공하면 DATABASE 생성
                        User MyInfo = new User(user.getUid(), name, "휴대폰번호", email);
                        db.collection("Users")
                                .document(user.getUid())
                                .set(MyInfo.getHashMap());
                    }
                })
                .addOnCompleteListener(listener);
    }

    public static FirebaseUser getFirebaseUser()
    {
        return user;
    }
}

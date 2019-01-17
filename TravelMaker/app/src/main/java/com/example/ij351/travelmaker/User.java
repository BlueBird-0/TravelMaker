package com.example.ij351.travelmaker;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
    public static void loginUser() {
        user = FirebaseAuth.getInstance().getCurrentUser();
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
    public static void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName("이름").build();
                            user.updateProfile(profileUpdates);

                            //Authentication 회원가입 성공하면 DATABASE 생성
                            User MyInfo = new User(user.getUid(), "이름", "휴대폰번호", "이메일");
                            db.collection("Users")
                                    .document(user.getUid())
                                    .set(MyInfo.getHashMap());
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            //Toast.makeText(LoginActivity.this, "Authentication failed.",
                            //      Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static FirebaseUser getFirebaseUser()
    {
        return user;
    }
}

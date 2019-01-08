package com.example.ij351.travelmaker;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class User {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static FirebaseUser user = mAuth.getCurrentUser();
    private static String TAG = "UserManager";


    //로그아웃
    public static void logoutUser(){
        user = null;
    }
    //로그인
    public static void loginUser(){
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    //로그인 상태 체크
    public static boolean checkLogined()
    {
        if(user != null) {
            Log.d(TAG, "로그인 상태 체크 : O 로그인");
            return true;
        }else{
            Log.d(TAG, "로그인 상태 체크 : X 로그아웃");
            return false;
        }
    }


    //회원가입
    public static String createUser(String email, String password)
    {
        checkLogined();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            checkLogined();
                        }else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            //Toast.makeText(LoginActivity.this, "Authentication failed.",
                            //      Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        return null;
    }

}

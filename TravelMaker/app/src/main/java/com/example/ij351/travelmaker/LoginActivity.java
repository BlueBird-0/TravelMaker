package com.example.ij351.travelmaker;

import android.os.Bundle;
import android.os.UserManager;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static FirebaseUser user = mAuth.getCurrentUser();
    private String TAG = "LoginActivityLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);    //액티비티에 사용되는 xml 불러오기

        //xml에서 버튼 찾아오기
        Button btn_login = (Button)findViewById(R.id.btn_login);
        Button btn_create = (Button)findViewById(R.id.btn_createUser);
        //버튼 기능 구현 (리스너 이용)
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //기능 구현
            }
        });
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //기능 구현
                EditText edit_id = (EditText)findViewById(R.id.editText_id);    //xml에서 EditText 가져오기
                EditText edit_pw = (EditText)findViewById(R.id.editText_id);

                String id = edit_id.getText().toString();   //문자열로 변환
                String pw = edit_pw.getText().toString();

                Log.d(TAG, "입력된 아이디 : "+id);    //테스트용 코드 로그 출력_ 안드로이드 스튜디오 하단 "Logcat" 에 콘솔처럼 출력       입력이 잘 됐는지 확인 가능

                User.createUser(id, pw);


            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI();
    }


}
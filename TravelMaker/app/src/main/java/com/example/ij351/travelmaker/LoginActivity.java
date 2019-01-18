package com.example.ij351.travelmaker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.UserManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    long backPressedTime = 0;

    private String TAG = "LoginActivityLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);    //액티비티에 사용되는 xml 불러오기

        //xml에서 버튼 찾아오기
        final Button btn_login = (Button)findViewById(R.id.btn_login);
        final Button btn_create = (Button)findViewById(R.id.btn_createUser);
        //버튼 기능 구현 (리스너 이용)
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //기능 구현
                EditText edit_id = (EditText)findViewById(R.id.editText_id);    //xml에서 EditText 가져오기
                EditText edit_pw = (EditText)findViewById(R.id.editText_pw);
                if(edit_id.getText().toString().trim().length() == 0) {
                    showSnackbar(findViewById(R.id.layout_login), "Emptied E-mail");
                    edit_id.requestFocus();
                    return;
                }
                if(edit_pw.getText().toString().trim().length() == 0) {
                    showSnackbar(findViewById(R.id.layout_login), "Emptied PW");
                    edit_pw.requestFocus();
                    return;
                }
                if(checkEmail(edit_id.getText().toString()) != true)
                {
                    showSnackbar(findViewById(R.id.layout_login), "Not matched E-mail format");
                    edit_id.requestFocus();
                    return;
                }


                final ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar_main);
                progressBar.setVisibility(View.VISIBLE);
                btn_login.setEnabled(false);
                btn_create.setEnabled(false);

                //로그인
                User.loginUser(edit_id.getText().toString(), edit_pw.getText().toString(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //잠금 해제
                        progressBar.setVisibility(View.INVISIBLE);
                        btn_login.setEnabled(true);
                        btn_create.setEnabled(true);

                        if(task.isSuccessful()){
                            finish();
                        }
                        else {
                            Log.d(TAG, task.getException().toString());
                            showSnackbar(findViewById(R.id.layout_login), task.getException().getMessage());
                        }
                    }
                });
            }
        });


        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //회원가입 액티비티 실행
                Intent intent = new Intent(getBaseContext(), CreateActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(User.checkLogined() == true)
        {
            Intent intent = new Intent(this, RoomActivity.class);
            startActivity(intent);
            //finish();
        }
    }

    @Override
    public void onBackPressed() {
        //뒤로 두번에 앱 종료
        final long FINISH_INTERVAL_TIME = 2000;
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            finishAffinity();
            System.runFinalization();
            System.exit(0);
        } else {
            backPressedTime = tempTime;
            Toast.makeText(this, "Good Travel", Toast.LENGTH_SHORT).show();
        }
    }

    public static void showSnackbar(View view, String message)
    {
        //스낵바
        TSnackbar snackbar = TSnackbar
                .make(view, message, TSnackbar.LENGTH_LONG);
        //snackbar.setActionTextColor(Color.WHITE);
        View snackbarView = snackbar.getView();
        //snackbarView.setBackgroundColor(Color.parseColor("#CC00CC"));
        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    //이메일 형식 체크
    public static boolean checkEmail(String email){
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        boolean isNormal = m.matches();
        return isNormal;

    }
}
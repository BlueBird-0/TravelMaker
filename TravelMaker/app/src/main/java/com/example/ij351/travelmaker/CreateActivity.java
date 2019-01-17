package com.example.ij351.travelmaker;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.admin.v1beta1.Progress;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateActivity extends AppCompatActivity {
    private String TAG = "LoginActivityLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);    //액티비티에 사용되는 xml 불러오기



        //xml에서 버튼 찾아오기
        final Button btn_create = (Button)findViewById(R.id.btn_createUser);
        //버튼 기능 구현 (리스너 이용)

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //기능 구현
                EditText edit_id = (EditText)findViewById(R.id.editText_id);    //xml에서 EditText 가져오기
                EditText edit_name = (EditText)findViewById(R.id.editText_name);    //xml에서 EditText 가져오기
                EditText edit_pw = (EditText)findViewById(R.id.editText_pw);
                EditText edit_confirm = (EditText)findViewById(R.id.editText_confirm);    //xml에서 EditText 가져오기

                if(edit_id.getText().toString().trim().length() == 0) {
                    showSnackbar(findViewById(R.id.layout_login), "Emptied E-mail");
                    edit_id.requestFocus();
                    return;
                }
                if(edit_name.getText().toString().trim().length() == 0) {
                    showSnackbar(findViewById(R.id.layout_login), "Emptied Name");
                    edit_name.requestFocus();
                    return;
                }
                if(edit_pw.getText().toString().trim().length() == 0) {
                    showSnackbar(findViewById(R.id.layout_login), "Emptied PW");
                    edit_pw.requestFocus();
                    return;
                }
                if(edit_confirm.getText().toString().trim().length() == 0) {
                    showSnackbar(findViewById(R.id.layout_login), "Emptied Confirm");
                    edit_confirm.requestFocus();
                    return;
                }
                if(checkEmail(edit_id.getText().toString()) != true)
                {
                    showSnackbar(findViewById(R.id.layout_login), "Not matched E-mail format");
                    edit_id.requestFocus();
                    return;
                }
                if(!edit_pw.getText().toString().equals(edit_confirm.getText().toString()))
                {
                    showSnackbar(findViewById(R.id.layout_login), "Not matched Password and Confirm");
                    edit_pw.requestFocus();
                    return;
                }

                String id = edit_id.getText().toString();   //문자열로 변환
                String name = edit_name.getText().toString();
                String pw = edit_pw.getText().toString();


                final ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar_main);
                progressBar.setVisibility(View.VISIBLE);
                btn_create.setEnabled(false);
                //회원가입
                User.createUser(id, name, pw, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.INVISIBLE);
                        btn_create.setEnabled(true);
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            showSnackbar(findViewById(R.id.layout_login), "ID created successful♥");
                            finish();
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            showSnackbar(findViewById(R.id.layout_login), task.getException().getMessage());
                        }
                    }
                });
            }
        });


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
package com.example.ij351.travelmaker;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewContentActivity extends AppCompatActivity {
    private String TAG = "NewContentActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newcontent);    //액티비티에 사용되는 xml 불러오기

        final EditText title = (EditText)findViewById(R.id.editText_title);
        title.requestFocus();

        Button create = (Button)findViewById(R.id.button_newContent);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.getText().toString().trim().length() == 0)
                {
                    title.requestFocus();
                    Toast.makeText(getApplicationContext(), "Title is null", Toast.LENGTH_SHORT).show();
                }
                //타이틀 만들기
                TravelRoom.createCheckList(title.getText().toString());
                finish();
            }
        });

        final InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(title, InputMethodManager.SHOW_FORCED);
    }
}
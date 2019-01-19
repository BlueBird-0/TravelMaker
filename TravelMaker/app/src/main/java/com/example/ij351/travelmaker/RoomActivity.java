package com.example.ij351.travelmaker;

import android.content.Intent;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class RoomActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        //        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);    //액티비티에 사용되는 xml 불러오기

        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar_main);

        final Button create_room = (Button)findViewById(R.id.button_createRoom);
        final Button entry_room = (Button)findViewById(R.id.button_entryRoom);
        final EditText entryCode = (EditText)findViewById(R.id.editText_entryRoom);
        final Button logout = (Button)findViewById(R.id.button_logout);

        create_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //버튼 막기
                create_room.setEnabled(false);
                entry_room.setEnabled(false);
                entryCode.setEnabled(false);
                logout.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);

                TravelRoom.createNewRoom(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        TravelRoom.entryRoom(task.getResult().getId(), new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if(task.isSuccessful())
                                {
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                }
                                //에러
                                else {
                                    //버튼 풀기
                                    create_room.setEnabled(true);
                                    entry_room.setEnabled(true);
                                    entryCode.setEnabled(true);
                                    logout.setEnabled(true);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    showSnackbar(findViewById(R.id.layout_room), task.getException().getMessage());
                                }

                            }
                        });
                    }
                });
            }
        });

        entry_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(entryCode.getText().toString().trim().length() == 0) {
                    entryCode.requestFocus();
                    showSnackbar(findViewById(R.id.layout_room), "Emptied RoomCode");
                    return;
                }

                //버튼 막기
                create_room.setEnabled(false);
                entry_room.setEnabled(false);
                entryCode.setEnabled(false);
                logout.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);

                TravelRoom.entryRoom(entryCode.getText().toString(), new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            if(doc.exists())
                            {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }else
                            {
                                //버튼 풀기
                                create_room.setEnabled(true);
                                entry_room.setEnabled(true);
                                entryCode.setEnabled(true);
                                logout.setEnabled(true);
                                progressBar.setVisibility(View.INVISIBLE);
                                showSnackbar(findViewById(R.id.layout_room), "Room does not exist!");
                            }
                        }
                    }
                });
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.logoutUser();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                getApplicationContext().startActivity(intent);
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
}

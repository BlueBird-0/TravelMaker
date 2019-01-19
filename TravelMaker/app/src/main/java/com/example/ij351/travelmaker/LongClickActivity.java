package com.example.ij351.travelmaker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class LongClickActivity extends AppCompatActivity {
    public static int STATE_DEL_CHECKLIST = 0;
    public static int STATE_DEL_CONTENT = 1;
    public static int STATE_DEL_COST = 2;
    public static int STATE_DEL_BILL = 3;
    private String TAG = "LoginActivityLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_longclick);    //액티비티에 사용되는 xml 불러오기

        //번들 데이터 가져오기
        final Bundle extras = getIntent().getExtras();
        /*
        switch (extras.getInt("state"))
        {
            case 0: //STATE_DEL_CHECKLIST
                Log.d(TAG, extras.getString("title"));
                Log.d(TAG, extras.getString("documentId"));
                break;
        }*/

        Button delete = (Button)findViewById(R.id.button_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(extras.getInt("state") == STATE_DEL_CHECKLIST)
                {
                    TravelRoom.deleteCheckList(extras.getString("title"), extras.getString("documentId"));
                    finish();
                }
                if(extras.getInt("state") == STATE_DEL_CONTENT)
                {
                    TravelRoom.deleteContent(extras.getString("title"));
                    finish();
                }
                if(extras.getInt("state") == STATE_DEL_COST)
                {
                    TravelRoom.deleteCost(extras.getString("documentId"));
                    finish();
                }
                if(extras.getInt("state") == STATE_DEL_BILL)
                {
                    TravelRoom.db.collection("Travels").document(TravelRoom.roomId).collection("Bills").document(
                            extras.getString("documentId")).delete();
                    //firestore 삭제
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();
                    StorageReference desertRef = storageRef.child("images/"+TravelRoom.roomId+extras.getString("storageFileName"));
                    desertRef.delete();
                    finish();
                }
            }
        });
        Button cancel = (Button)findViewById(R.id.button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

package com.example.ij351.travelmaker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

import javax.annotation.Nullable;

public class ParticipantActivity extends AppCompatActivity {
    private String TAG = "ParticipantActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant);    //액티비티에 사용되는 xml 불러오기

        final Switch passport = (Switch)findViewById(R.id.switch_passport);
        final Switch visa = (Switch)findViewById(R.id.switch_visa);
        passport.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true)
                {
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("hasPassport", true);
                    TravelRoom.db.collection("Travels").document(TravelRoom.roomId).collection("Participants").document(User.getFirebaseUser().getUid())
                            .update(data);
                }else
                {
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("hasPassport", false);
                    TravelRoom.db.collection("Travels").document(TravelRoom.roomId).collection("Participants").document(User.getFirebaseUser().getUid())
                            .update(data);
                }
            }
        });
        visa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true)
                {
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("hasVISA", true);
                    TravelRoom.db.collection("Travels").document(TravelRoom.roomId).collection("Participants").document(User.getFirebaseUser().getUid())
                            .update(data);
                }else
                {
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("hasVISA", false);
                    TravelRoom.db.collection("Travels").document(TravelRoom.roomId).collection("Participants").document(User.getFirebaseUser().getUid())
                            .update(data);
                }
            }
        });

        TravelRoom.db.collection("Travels").document(TravelRoom.roomId).collection("Participants").document(User.getFirebaseUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>(){
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        passport.setChecked(doc.getBoolean("hasPassport"));
                        visa.setChecked(doc.getBoolean("hasVISA"));
                        //progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }
}

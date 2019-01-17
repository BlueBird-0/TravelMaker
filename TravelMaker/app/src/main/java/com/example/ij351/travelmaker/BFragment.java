package com.example.ij351.travelmaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BFragment extends Fragment {
    RecyclerPartAdapter adapter;
    RecyclerCheckAdapter adapter_check;
    private String TAG = "BFragment";

    public static BFragment newInstance(int page, String title) {
        BFragment bFragment = new BFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        bFragment.setArguments(args);
        return bFragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_b, container, false);

        Button btn = (Button)view.findViewById(R.id.button3);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TravelRoom.createNewRoom(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            TravelRoom.entryRoom(task.getResult().getId());
                            //getActivity().finish();
                        }
                    }
                });
            }
        });
        Button logout = (Button)view.findViewById(R.id.button4);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.logoutUser();
            }
        });

        Button newContent= (Button)view.findViewById(R.id.button_newContent);
        newContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //로그인 액티비티 실행
                Intent intent = new Intent(getContext(), NewContentActivity.class);
                startActivity(intent);
            }
        });



        //방에 있는 사람들 데이터 가져오기
        //리스트 출력
        final ArrayList<User> participants = new ArrayList<>();
        // set up the RecyclerView
        final RecyclerView recyclerView = view.findViewById(R.id.recycler_participant);
        final ProgressBar progressBar = (ProgressBar)view.findViewById(R.id.progressBar_main);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter = new RecyclerPartAdapter(getContext(), participants);
        //adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);


        Log.d(TAG, "Room : "+TravelRoom.roomId);
        TravelRoom.db.collection("Travels").document(TravelRoom.roomId).collection("Participants")
                .addSnapshotListener(new EventListener<QuerySnapshot>(){
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot value, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed2.", e);
                            return;
                        }

                        participants.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.getString("name") != null || doc.getBoolean("hasPassport") != null || doc.getBoolean("hasVISA") != null
                                    || doc.getString("uid") != null || doc.getString("email") != null) {
                                String uid = doc.getString("uid");
                                String name = doc.getString("name");
                                String email = doc.getString("uid");
                                Boolean hasPassport = doc.getBoolean("hasPassport");
                                Boolean hasVISA = doc.getBoolean("hasVISA");

                                User newUser = new User(uid, name, hasPassport, hasVISA, email);
                                participants.add(newUser);
                            }
                        }
                        adapter.notifyDataSetChanged();     //Adapter 새로고침
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });





        //방의 CheckLists 가져오기
        //리스트 출력
        final ArrayList<String> titles = new ArrayList<>();
        // set up the RecyclerView
        final RecyclerView recyclerView_checkList = view.findViewById(R.id.recycler_checklists);
        final ProgressBar progressBar2 = (ProgressBar)view.findViewById(R.id.progressBar_checklist);
        recyclerView_checkList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter_check = new RecyclerCheckAdapter(getContext(), titles);
        //adapter.setClickListener(this);
        recyclerView_checkList.setAdapter(adapter_check);
        TravelRoom.db.collection("Travels").document(TravelRoom.roomId).collection("CheckLists")
                .addSnapshotListener(new EventListener<QuerySnapshot>(){
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot value, @javax.annotation.Nullable FirebaseFirestoreException e) {

                        Log.w(TAG, "changed Text");
                        if (e != null) {
                            Log.w(TAG, "Listen failed2.", e);
                            return;
                        }

                        //titles.clear();
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            switch(dc.getType()){
                                case ADDED:
                                    Log.d(TAG, "Document Change : ADDED");
                                    titles.add(dc.getDocument().getId());
                                    break;
                                case MODIFIED:
                                    Log.d(TAG, "Document Change : MODIFIED");
                                    Log.d(TAG, "Document Change : "+dc.getDocument().getData());
                                    break;
                                case REMOVED:
                                    Log.d(TAG, "Document Change : REMOVED");
                                    titles.remove(dc.getDocument().getId());
                                    break;
                            }
                        }
                        adapter_check.notifyDataSetChanged();     //Adapter 새로고침
                        progressBar2.setVisibility(View.INVISIBLE);
                    }
                });
        /*
        TravelRoom.db.collection("Travels").document(TravelRoom.roomId)
                .collection("CheckLists").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                titles.add(documentSnapshot.getId());
                            }
                            adapter_check.notifyDataSetChanged();     //Adapter 새로고침
                            progressBar2.setVisibility(View.INVISIBLE);
                        }
                    }
                });*/

        return view;
    }
}
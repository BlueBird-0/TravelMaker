package com.example.ij351.travelmaker;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BFragment extends Fragment {
    RecyclerPartAdapter adapter;
    RecyclerCheckAdapter adapter_check;

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
        View view = inflater.inflate(R.layout.fragment_b, container, false);

        Button btn = (Button)view.findViewById(R.id.button3);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TravelRoom.createNewRoom();
            }
        });


        Button logout = (Button)view.findViewById(R.id.button4);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.logoutUser();
            }
        });

        Button write = (Button)view.findViewById(R.id.button5);
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TravelRoom.writeContent("UserName", "첫번째 글");
            }
        });



        //방에 있는 사람들 데이터 가져오기
        //리스트 출력
        final ArrayList<String> Participants = new ArrayList<>();
        // set up the RecyclerView
        final RecyclerView recyclerView = view.findViewById(R.id.recycler_participant);
        final ProgressBar progressBar = (ProgressBar)view.findViewById(R.id.progressBar_main);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter = new RecyclerPartAdapter(getContext(), Participants);
        //adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        //TravelRoom.db.collection("Travels").document(TravelRoom.roomId)
        TravelRoom.db.collection("Travels").document(TravelRoom.roomId)
                .collection("Participants").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Participants.add(documentSnapshot.getString("name"));
                            }
                            adapter.notifyDataSetChanged();     //Adapter 새로고침
                            progressBar.setVisibility(View.INVISIBLE);
                        }
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
                });

        return view;
    }
}
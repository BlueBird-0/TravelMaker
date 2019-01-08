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

import java.util.ArrayList;

public class BFragment extends Fragment {
    private String title;
    private int page;
    RecyclerPartAdapter adapter;



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
        Log.d("test001", String.valueOf(page));

        Button btn = (Button)view.findViewById(R.id.button3);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test001", String.valueOf(page)+": 버튼 눌림");
                TravelRoom.createNewRoom();
            }
        });


        Button logout = (Button)view.findViewById(R.id.button4);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test001", String.valueOf(page)+": 버튼 눌림 =>로그아웃");
                User.logoutUser();
            }
        });

        //리스트 출력
        ArrayList<String> animalNames = new ArrayList<>();
        animalNames.add("전인학");
        animalNames.add("Cow");
        animalNames.add("Camel");
        animalNames.add("Sheep");
        animalNames.add("Goat");

        // set up the RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recycler_participant);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter = new RecyclerPartAdapter(getContext(), animalNames);
        //adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);


        //데이터 가져오기 테스트
        TravelRoom.getParticipants("WYICGi4IhKWXU9vJF4H2");

        return view;
    }
}
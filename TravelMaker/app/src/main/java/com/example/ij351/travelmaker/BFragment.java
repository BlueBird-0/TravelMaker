package com.example.ij351.travelmaker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class BFragment extends Fragment {
    private String title;
    private int page;

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
        return view;
    }
}
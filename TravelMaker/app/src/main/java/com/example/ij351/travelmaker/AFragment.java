package com.example.ij351.travelmaker;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.cachapa.expandablelayout.ExpandableLayout;

public class AFragment extends Fragment {

    public static AFragment newInstance(int page, String title) {
        AFragment aFragment = new AFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        aFragment.setArguments(args);
        return aFragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a, container, false);

        final ExpandableLayout expandableLayout1 = (ExpandableLayout)view.findViewById(R.id.expandable_layout1);
        final ExpandableLayout expandableLayout2 = (ExpandableLayout)view.findViewById(R.id.expandable_layout2);
        final ExpandableLayout expandableLayout3 = (ExpandableLayout)view.findViewById(R.id.expandable_layout3);

        ConstraintLayout expand1 = (ConstraintLayout)view.findViewById(R.id.layout_expand1);
        expand1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandableLayout1.toggle();
            }
        });

        ConstraintLayout expand2 = (ConstraintLayout)view.findViewById(R.id.layout_expand2);
        expand2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandableLayout2.toggle();
                showDatePickerDialog(getView());
            }
        });

        ConstraintLayout expand3 = (ConstraintLayout)view.findViewById(R.id.layout_expand3);
        expand3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandableLayout3.toggle();
            }
        });

        return view;
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }
}

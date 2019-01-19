package com.example.ij351.travelmaker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment2 extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    Calendar c;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        //final Calendar c = Calendar.getInstance();
        int year = getArguments().getInt("year");
        int month = getArguments().getInt("month");
        int day = getArguments().getInt("day");

        DatePickerDialog dpd = new DatePickerDialog(getActivity(), this, year, month, day);
        c = Calendar.getInstance();
        c.set(year, month, day);

        dpd.setMessage("Ending Date");
        return dpd;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        view.setMinDate(c.getTimeInMillis());
        Log.d("test001", "눌림");
    }
}

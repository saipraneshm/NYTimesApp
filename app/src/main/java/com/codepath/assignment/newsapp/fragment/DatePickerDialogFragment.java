package com.codepath.assignment.newsapp.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;


import com.codepath.assignment.newsapp.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by saip92 on 8/20/2017.
 */

public class DatePickerDialogFragment extends DialogFragment {


    private static final String ARG_DATE = "date";
    public  static final String EXTRA_DATE = "EXTRA_TIME";
    private DatePicker mDatePicker;

    public static DatePickerDialogFragment newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);

        DatePickerDialogFragment dialogFragment = new DatePickerDialogFragment();
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        Date date = (Date) getArguments().getSerializable(ARG_DATE);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date_picker, null);
        mDatePicker = (DatePicker) v.findViewById(R.id.date_picker);
        mDatePicker.init(year, month, day, null);


        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    int year1 = mDatePicker.getYear();
                    int month1 = mDatePicker.getMonth();
                    int day1 = mDatePicker.getDayOfMonth();
                    Date date1 = new GregorianCalendar(year1, month1, day1).getTime();
                    if(date1.getTime() < System.currentTimeMillis()){
                        date1 = new Date();
                    }
                    sendResult(Activity.RESULT_OK, date1);
                }).create();
    }

    private void sendResult(int resultCode, Date date){
        if(getTargetFragment() == null) return;

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE,date);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }


}

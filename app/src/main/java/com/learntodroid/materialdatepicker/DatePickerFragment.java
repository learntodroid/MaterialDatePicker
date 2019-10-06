package com.learntodroid.materialdatepicker;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

public class DatePickerFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    private TextView dateTextView;
    private DatePickerDialog dpd;
    private Button weekendDateButton;
    private Button weekdayDateButton;

    final int MAX_SELECTABLE_DATE_IN_FUTURE = 365; // 365 days into the future max

    public DatePickerFragment() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.datepicker_layout, container, false);

        dateTextView = view.findViewById(R.id.date_textview);
        weekendDateButton = view.findViewById(R.id.date_button_weekend);
        weekdayDateButton = view.findViewById(R.id.date_button_weekday);

        view.findViewById(R.id.original_button).setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            new android.app.DatePickerDialog(
                    requireActivity(),
                    (view1, year, month, dayOfMonth) -> Log.d("Orignal", "Got clicked"),
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        weekendDateButton.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            if (dpd == null) {
                dpd = DatePickerDialog.newInstance(
                        DatePickerFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
            } else {
                dpd.initialize(
                        DatePickerFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
            }

            // restrict to weekends only
            ArrayList<Calendar> weekends = new ArrayList<Calendar>();
            Calendar day = Calendar.getInstance();
            for (int i = 0; i < MAX_SELECTABLE_DATE_IN_FUTURE; i++) {
                if (day.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || day.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    Calendar d = (Calendar) day.clone();
                    weekends.add(d);
                }
                day.add(Calendar.DATE, 1);
            }
            Calendar[] weekendDays = weekends.toArray(new Calendar[weekends.size()]);
            dpd.setSelectableDays(weekendDays);

            dpd.setOnCancelListener(dialog -> {
                Log.d("DatePickerDialog", "Dialog was cancelled");
                dpd = null;
            });
            dpd.show(requireFragmentManager(), "Datepickerdialog");
        });

        weekdayDateButton.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            if (dpd == null) {
                dpd = DatePickerDialog.newInstance(
                        DatePickerFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
            } else {
                dpd.initialize(
                        DatePickerFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
            }

            // restrict to weekdays only
            ArrayList<Calendar> weekdays = new ArrayList<Calendar>();
            Calendar day = Calendar.getInstance();
            for (int i = 0; i < MAX_SELECTABLE_DATE_IN_FUTURE; i++) {
                if (day.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && day.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    Calendar d = (Calendar) day.clone();
                    weekdays.add(d);
                }
                day.add(Calendar.DATE, 1);
            }
            Calendar[] weekdayDays = weekdays.toArray(new Calendar[weekdays.size()]);
            dpd.setSelectableDays(weekdayDays);

            dpd.setOnCancelListener(dialog -> {
                Log.d("DatePickerDialog", "Dialog was cancelled");
                dpd = null;
            });
            dpd.show(requireFragmentManager(), "Datepickerdialog");
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dpd = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) requireFragmentManager().findFragmentByTag("Datepickerdialog");
        if(dpd != null) dpd.setOnDateSetListener(this);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = "You picked the following date: "+dayOfMonth+"/"+(++monthOfYear)+"/"+year;
        dateTextView.setText(date);
        dpd = null;
    }
}
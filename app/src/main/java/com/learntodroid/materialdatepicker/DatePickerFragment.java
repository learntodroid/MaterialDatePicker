package com.learntodroid.materialdatepicker;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private TextView dateTextView;
    private CheckBox modeDarkDate;
    private CheckBox modeCustomAccentDate;
    private CheckBox vibrateDate;
    private CheckBox dismissDate;
    private CheckBox titleDate;
    private CheckBox showYearFirst;
    private CheckBox showVersion2;
    private CheckBox switchOrientation;
    private CheckBox limitSelectableDays;
    private CheckBox highlightDays;
    private DatePickerDialog dpd;

    public DatePickerFragment() {
        // Required empty public constructor
    }

    private void limitToWeekends(int days_into_future) {
        ArrayList<Calendar> weekends = new ArrayList<Calendar>();
        Calendar day = Calendar.getInstance();
        for (int i = 0; i < days_into_future; i++) {
            if (day.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || day.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                Calendar d = (Calendar) day.clone();
                weekends.add(d);
            }
            day.add(Calendar.DATE, 1);
        }
        Calendar[] weekendDays = weekends.toArray(new Calendar[weekends.size()]);
        dpd.setSelectableDays(weekendDays);
    }

    private void limitToWeekdays(int days_into_future) {
        ArrayList<Calendar> weekdays = new ArrayList<Calendar>();
        Calendar day = Calendar.getInstance();
        for (int i = 0; i < days_into_future; i++) {
            if (day.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && day.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                Calendar d = (Calendar) day.clone();
                weekdays.add(d);
            }
            day.add(Calendar.DATE, 1);
        }
        Calendar[] weekdayDays = weekdays.toArray(new Calendar[weekdays.size()]);
        dpd.setSelectableDays(weekdayDays);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.datepicker_layout, container, false);

        // Find our View instances
        dateTextView = view.findViewById(R.id.date_textview);
        Button dateButton = view.findViewById(R.id.date_button);
        modeDarkDate = view.findViewById(R.id.mode_dark_date);
        modeCustomAccentDate = view.findViewById(R.id.mode_custom_accent_date);
        vibrateDate = view.findViewById(R.id.vibrate_date);
        dismissDate = view.findViewById(R.id.dismiss_date);
        titleDate = view.findViewById(R.id.title_date);
        showYearFirst = view.findViewById(R.id.show_year_first);
        showVersion2 = view.findViewById(R.id.show_version_2);
        switchOrientation = view.findViewById(R.id.switch_orientation);
        limitSelectableDays = view.findViewById(R.id.limit_dates);
        highlightDays = view.findViewById(R.id.highlight_dates);

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

        // Show a datepicker when the dateButton is clicked
        dateButton.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            /*
            It is recommended to always create a new instance whenever you need to show a Dialog.
            The sample app is reusing them because it is useful when looking for regressions
            during testing
             */
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
            dpd.setThemeDark(modeDarkDate.isChecked());
            dpd.vibrate(vibrateDate.isChecked());
            dpd.dismissOnPause(dismissDate.isChecked());
            dpd.showYearPickerFirst(showYearFirst.isChecked());
            dpd.setVersion(showVersion2.isChecked() ? DatePickerDialog.Version.VERSION_2 : DatePickerDialog.Version.VERSION_1);
            if (modeCustomAccentDate.isChecked()) {
                dpd.setAccentColor(Color.parseColor("#9C27B0"));
            }
            if (titleDate.isChecked()) {
                dpd.setTitle("DatePicker Title");
            }
            if (highlightDays.isChecked()) {
                Calendar date1 = Calendar.getInstance();
                Calendar date2 = Calendar.getInstance();
                date2.add(Calendar.WEEK_OF_MONTH, -1);
                Calendar date3 = Calendar.getInstance();
                date3.add(Calendar.WEEK_OF_MONTH, 1);
                Calendar[] days = {date1, date2, date3};
                dpd.setHighlightedDays(days);
            }
            if (limitSelectableDays.isChecked()) {
//                limitToWeekends(365*3);
                limitToWeekdays(365*3);
            }

            if (switchOrientation.isChecked()) {
                if (dpd.getVersion() == DatePickerDialog.Version.VERSION_1) {
                    dpd.setScrollOrientation(DatePickerDialog.ScrollOrientation.HORIZONTAL);
                } else {
                    dpd.setScrollOrientation(DatePickerDialog.ScrollOrientation.VERTICAL);
                }
            }
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
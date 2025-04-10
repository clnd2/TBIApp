package com.example.appinterface1;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.Date;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Summary;
import java.io.StringReader;
import java.util.List;

public class Cal_CreateEventActivity extends AppCompatActivity {

    static String TAG = "System.out";

    static Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cal_create_event);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void doneButton(View view) {

    }

    public void cancelButton(View view) {
        System.out.println("cancelButton running");
        Intent i = null;

        i = new Intent(this, RemindersActivity.class);

        startActivity(i);
    }

    public void selectDateButton(View view){
        showDatePicker();
    }

    public void selectTimeButton(View view) {
        showTimePicker();
    }

    public void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {

                    calendar.add(Calendar.YEAR, selectedYear);
                    calendar.add(Calendar.MONTH, selectedMonth);
                    calendar.add(Calendar.DAY_OF_MONTH, selectedDay);

                    //Log.d(TAG, "start date: " + date);


                    TextView textDateSelected = findViewById(R.id.textDateSelected);
                    textDateSelected.setText("Selected Date: " + (selectedMonth + 1) + "/" + selectedDay + "/" + selectedYear);

                    // Save the selected date
                    //calendar.setTime(date);
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    public void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {

                        calendar.add(Calendar.HOUR_OF_DAY, selectedHour);
                        calendar.add(Calendar.MINUTE, selectedMinute);

                        TextView textTimeSelected = findViewById(R.id.textTimeStartSelected);
                        textTimeSelected.setText("selected time: " + selectedHour + ":" + selectedMinute);

                    }
                }, hour, minute, false); // false for 24-hour format

        timePickerDialog.setTitle("Start Time");

        timePickerDialog.show();
    }
}
package com.example.appinterface1;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.Date;

public class Cal_CreateEventActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    static String TAG = "System.out";

    static Calendar date = Calendar.getInstance();
    static Calendar startTime = Calendar.getInstance();
    static Calendar duration = Calendar.getInstance();


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

        Spinner repeatSpinner = findViewById(R.id.dropdownDuration);
        setupRepeatSpinner(repeatSpinner); // Call the separate function
        repeatSpinner.setOnItemSelectedListener(this);
        // set the inputs to 0
        date.set(0,0,0,0,0,0);
        startTime.set(0,0,0,0,0,0);
        duration.set(0,0,0,0,0,0);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
        // duration spinner itemSelected Listener
        String item = parent.getSelectedItem().toString();
        // calculate time in ms based on item
        Log.d(TAG, "spinner item: " + item);

        int pos = parent.getSelectedItemPosition();
        duration.set(0,0,0,0,0,0);

        switch (pos) {
            case 0:
                // nothing, leave as 0
                break;
            case 1:
                duration.set(Calendar.MINUTE,15);
                break;
            case 2:
                duration.set(Calendar.MINUTE,30);
                break;
            case 3:
                duration.set(Calendar.HOUR_OF_DAY,1);
                break;
            case 4:
                duration.set(Calendar.HOUR_OF_DAY,2);
                break;
            case 5:
                duration.set(Calendar.HOUR_OF_DAY,3);
                break;
            case 6:
                duration.set(Calendar.HOUR_OF_DAY,4);
                break;
        }

    }
    public void onNothingSelected(AdapterView<?> arg0){
        // just here to make the spinner happy
    }

    private void setupRepeatSpinner(Spinner spinner) {
        String[] durations = {"0 Minutes","15 Minutes", "30 Minutes", "1 Hour", "2 Hours", "3 Hours", "4 Hours"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, durations);
        spinner.setAdapter(adapter);
    }

    public void doneButton(View view) {
        //Log.d(TAG, "Date: " + date.getTime());
        //Log.d(TAG, "Time: " + startTime.getTime());
        //Log.d(TAG, "Duration: " + duration.getTime());

        Calendar EventStart = Calendar.getInstance();
        Calendar EventEnd = Calendar.getInstance();
        EventStart.set(date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DAY_OF_MONTH),startTime.get(Calendar.HOUR_OF_DAY),startTime.get(Calendar.MINUTE),0);
        EventEnd.set(date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DAY_OF_MONTH),startTime.get(Calendar.HOUR_OF_DAY),startTime.get(Calendar.MINUTE),0);
        EventEnd.add(Calendar.MINUTE, duration.get(Calendar.MINUTE));
        EventEnd.add(Calendar.HOUR_OF_DAY, duration.get(Calendar.HOUR_OF_DAY));

        Date start = EventStart.getTime();
        Date end = EventEnd.getTime();
        Log.d(TAG, "start: " + start);
        Log.d(TAG, "end: " + end);

        // get title and description
        //TextView textDateSelected = findViewById(R.id.textDateSelected);
        TextView uiTitle = findViewById(R.id.uiTitle);
        String title = uiTitle.getText().toString();

        // add to calendar
        Context context = getApplicationContext();
        EventHandling.addEvent(context, title, start, end, false);
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this,"adding event, please wait several minutes", duration);
        toast.show();

        Intent i = new Intent(this, CalendarActivity.class);
        startActivity(i);
    }

    public void cancelButton(View view) {
        System.out.println("cancelButton running");
        Intent i = null;

        i = new Intent(this, CalendarActivity.class);

        startActivity(i);
    }

    public void selectDateButton(View view){
        showDatePicker();
    }

    public void selectTimeButton(View view) {
        showTimePicker();
    }

    public void spinnerHandler(){

    }

    public void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {

                    date.set(Calendar.YEAR, selectedYear);
                    date.set(Calendar.MONTH, selectedMonth);
                    date.set(Calendar.DAY_OF_MONTH, selectedDay);

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

                        startTime.set(Calendar.HOUR_OF_DAY, selectedHour);
                        startTime.set(Calendar.MINUTE, selectedMinute);

                        TextView textTimeSelected = findViewById(R.id.textTimeStartSelected);
                        textTimeSelected.setText("selected time: " + selectedHour + ":" + selectedMinute);

                    }
                }, hour, minute, false); // false for 24-hour format

        timePickerDialog.setTitle("Start Time");

        timePickerDialog.show();
    }
}
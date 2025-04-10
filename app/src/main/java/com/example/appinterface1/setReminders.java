package com.example.appinterface1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.DatePickerDialog;

//added
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.SharedPreferences;
import android.app.TimePickerDialog;
import android.widget.TimePicker;

import java.util.Calendar;

public class setReminders extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_set_reminders);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Spinner repeatSpinner = findViewById(R.id.dropdownDuration);
        setupRepeatSpinner(repeatSpinner); // Call the separate function
    }

    public void done(View v) {

        Intent i = null;

        i = new Intent(this, ShowRemindersActivity.class);

        //save the text of each file and make it a show reminder
        String icsContent = createICSFile();
        saveICSFile(icsContent);

        startActivity(i);

    }

    public void cancel(View v) {

        Intent i = null;

        i = new Intent(this, RemindersActivity.class);

        startActivity(i);

    }

    private String createICSFile() {
        //get the text of the title
        EditText title = findViewById(R.id.uiTitle);
        String tit = title.getText().toString();

        //get the text of the description
        EditText description = findViewById(R.id.uiDescription);
        String desc = description.getText().toString();

        //get the text of the location
        EditText location = findViewById(R.id.uiLocation);
        String loc = location.getText().toString();

        //get the text of the date
        String dat = getSavedDate();

        //get the text of the time
        String timStart = getStartTime();

        //get duration
        Spinner spinner = findViewById(R.id.dropdownDuration);
        String duration = spinner.getSelectedItem().toString();

        int startingTime = Integer.parseInt(timStart);

        int duration2 = 0;

        switch (duration) {
            case "15 Minutes":
                duration2 = startingTime + 15;
                break;
            case "30 Minutes":
                duration2 = startingTime + 30;
                break;
            case "1 Hour":
                duration2 = startingTime + 100;
                break;
            case "2 Hours":
                duration2 = startingTime + 200;
                break;
            case "3 Hours":
                duration2 = startingTime + 300;
                break;
            case "4 Hours":
                duration2 = startingTime + 400;
                break;
        }

        int secondLastDigit = (duration2 / 10) % 10;

        if(secondLastDigit >= 6)
        {
            duration2 = duration2 - 60;
            duration2 = duration2 + 100;
        }

        String endTime = "";

        if(duration2 >= 2400) {
            duration2 = duration2 - 2400;
            if (duration2 < 100) {
                endTime = String.valueOf(duration2);
                endTime = "00" + endTime;
            } else if (duration2 < 1000) {
                endTime = String.valueOf(duration2);
                endTime = "0" + endTime;
            }
        }
        else {
            if(duration2 < 100)
            {
                endTime = String.valueOf(duration2);
                endTime = "00" + endTime;
            }
            else if (duration2 < 1000)
            {
                endTime = String.valueOf(duration2);
                endTime = "0" + endTime;
            }
            else
            {
                endTime = String.valueOf(duration2);
            }
        }

        //Log.d("endTime", endTime);

        //create an ICAL file
        StringBuilder icsContent = new StringBuilder();
        icsContent.append("BEGIN:VCALENDAR\n");
        icsContent.append("Version:2.0\n");
        icsContent.append("BEGIN:VEVENT\n");
        icsContent.append("SUMMARY:" + tit + "\n");
        icsContent.append("DTSTART;TZID=America/Denver:" + dat + "T" + timStart + "00" + "\n");
        icsContent.append("DTEND;TZID=America/Denver:" + dat + "T" + endTime + "00" + "\n");
        icsContent.append("LOCATION:" + loc + "\n");
        icsContent.append("DESCRIPTION:" + desc + "\n");
        icsContent.append("STATUS:CONFIRMED\n");
        icsContent.append("END:VEVENT\n");
        icsContent.append("END:VCALENDAR\n");

        return icsContent.toString();
    }

    private void saveICSFile(String icsContent)
    {
        try {

            File file = new File(getFilesDir(), "event.ics");

            // Get the path to the external storage directory
            File directory = getFilesDir();

            Log.e("INFO", "Display Directory:" + directory.getAbsolutePath());

            if (file.exists()) {

                // Create a file for the ICS content
                File icsFile = new File(directory, "event.ics");
                FileOutputStream fileOutputStream = new FileOutputStream(icsFile);


                // Write the ICS content to the file
                fileOutputStream.write(icsContent.getBytes());
                fileOutputStream.close();
            }
            else
            {
                // Create a file for the ICS content
                File icsFile = new File(directory, "event.ics");
                FileOutputStream fileOutputStream = new FileOutputStream(icsFile);


                // Write the ICS content to the file
                fileOutputStream.write(icsContent.getBytes());
                fileOutputStream.close();
            }

            //Log.d("ICS", "ICS file saved at: " + icsFile.getAbsolutePath());

            }
        catch (IOException e)
            {
            Log.e("ICS", "Error saving ICS file: " + e.getMessage());
            }

    }

    public void selectDate(View v)
    {
        showDatePicker();
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {


                    if(selectedMonth < 10 && selectedDay < 10) {
                        String selectedDate = selectedYear + "0" + (selectedMonth + 1) + "0" + selectedDay;
                        saveDate(selectedDate);

                    }
                    else if (selectedMonth < 10 && selectedDay >= 10)
                    {
                        String selectedDate = selectedYear + "0" + (selectedMonth + 1) + "" + selectedDay;
                        saveDate(selectedDate);
                    }
                    else if (selectedMonth >= 10 && selectedDay < 10)
                    {
                        String selectedDate = selectedYear + "" + (selectedMonth + 1) + "0" + selectedDay;
                        saveDate(selectedDate);
                    }
                    else
                    {
                        String selectedDate = selectedYear + "" + (selectedMonth + 1) + "" + selectedDay;
                        saveDate(selectedDate);
                    }

                    TextView textDateSelected = findViewById(R.id.textDateSelected);
                    textDateSelected.setText("Selected Date: " + (selectedMonth + 1) + "/" + selectedDay + "/" + selectedYear);

                    // Save the selected date
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    private void saveDate(String date) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("selected_date", date);
        editor.apply();
    }

    private String getSavedDate() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("selected_date", "");
    }

    public void selectStartTime(View v)
    {
        showTimePicker();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {

                        String amPm;
                        int hour12;
                        int hour24;

                        // Convert to 12-hour format
                        if (selectedHour >= 12) {
                            amPm = "PM";
                            hour12 = selectedHour == 12 ? 12 : selectedHour - 12;
                            hour24 = selectedHour;
                        } else {
                            amPm = "AM";
                            hour12 = selectedHour == 0 ? 12 : selectedHour;
                            hour24 = selectedHour;
                        }

                            String selectedTime = String.format("%02d%02d", hour24, selectedMinute);


                            TextView textTimeSelected = findViewById(R.id.textTimeStartSelected);
                            if(selectedMinute < 10) {
                                textTimeSelected.setText("Selected Time: " + hour12 + ":" + "0" + selectedMinute + " " + amPm);
                            }
                            else{
                                textTimeSelected.setText("Selected Time: " + hour12 + ":" + selectedMinute + " " + amPm);
                            }
                            saveStartTime(selectedTime);

                    }
                }, hour, minute, false); // false for 24-hour format

        timePickerDialog.setTitle("Start Time");

        timePickerDialog.show();
    }

    private void saveStartTime(String time) {
        SharedPreferences sharedPreferences1 = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences1.edit();
        editor.putString("selected_time", time);
        editor.apply();
    }

    private String getStartTime() {
        SharedPreferences sharedPreferences1 = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences1.getString("selected_time", "");
    }

    private void setupRepeatSpinner(Spinner spinner) {
        String[] durations = {"15 Minutes", "30 Minutes", "1 Hour", "2 Hours", "3 Hours", "4 Hours"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, durations);
        spinner.setAdapter(adapter);
    }


}
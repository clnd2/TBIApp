package com.example.appinterface1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.app.DatePickerDialog;

//added
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
        EditText date = findViewById(R.id.uiDate);
        String dat = date.getText().toString();

        //get the text of the time
        EditText time = findViewById(R.id.uiTime);
        String tim = time.getText().toString();

        //create an ICAL file
        StringBuilder icsContent = new StringBuilder();
        icsContent.append("BEGIN:VCALENDAR\n");
        icsContent.append("Version:2.0\n");
        icsContent.append("BEGIN:VEVENT\n");
        icsContent.append("SUMMARY:" + tit + "\n");
        icsContent.append("DTSTART;TZID=America/Denver:" + dat + "T" + tim +"\n");
        icsContent.append("DTEND;TZID=America/Denver:\n");
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
            // Get the path to the external storage directory
            File directory = getFilesDir();

            // Create a file for the ICS content
            File icsFile = new File(directory, "event.ics");
            FileOutputStream fileOutputStream = new FileOutputStream(icsFile);

            // Write the ICS content to the file
            fileOutputStream.write(icsContent.getBytes());
            fileOutputStream.close();

            Log.d("ICS", "ICS file saved at: " + icsFile.getAbsolutePath());

            //Debug to make sure the content is being saved
            //TextView textView = findViewById(R.id.textTitle);
            //textView.setText(icsContent);

            }
        catch (IOException e)
            {
            Log.e("ICS", "Error saving ICS file: " + e.getMessage());
            }

        // Display content in TextView

    }

    public void selectDate (View v)
    {

        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

    }
}
package com.example.appinterface1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

//static final String TAG = "MainActivity";

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

        //Log.d("TAG", tim); // displays the correct word

        //create an ICAL file
        StringBuilder icsContent = new StringBuilder();
        icsContent.append("BEGIN:VCALENDAR\n");
        icsContent.append("Version:2.0\n");
        icsContent.append("BEGIN:VEVENT\n");
        icsContent.append("SUMMARY:" + tit + "\n");
        icsContent.append("DTSTART:\n");
        icsContent.append("DTEND:\n");
        icsContent.append("LOCATION:" + loc + "\n");
        icsContent.append("DESCRIPTION:" + desc + "\n");
        icsContent.append("STATUS:CONFIRMED\n");
        icsContent.append("END:VEVENT\n");
        icsContent.append("END:VCALENDAR\n");


        startActivity(i);

    }

    public void cancel(View v) {

        Intent i = null;

        i = new Intent(this, RemindersActivity.class);

        startActivity(i);

    }
}
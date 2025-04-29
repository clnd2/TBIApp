package com.example.appinterface1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ShowRemindersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_reminders2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        displayICS();

    }

   /* public void home(View v) {

        Intent i = null;

        i = new Intent(this, MainActivity.class);

        startActivity(i);

    }*/

    public void home(View v) {
        Intent i = null;
        i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
    private void displayICS ()
    {
        File directory = getFilesDir();
        File icsFile = new File(directory, "event.ics");

        StringBuilder stringBuilder = new StringBuilder();
        //correct way to display ICAL

        try
        {
            FileInputStream fis = new FileInputStream(icsFile);
            List<String> eventDetails = parseICS(fis);
            displayEvents(eventDetails);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }




        //above is correct display of ICAL

        //Testing




        TextView textView = findViewById(R.id.textView1);
/*
        if (icsFile.exists()) {
            StringBuilder content = new StringBuilder();

            try {
                FileInputStream fis = new FileInputStream(icsFile);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader reader = new BufferedReader(isr);

                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }

                reader.close();
                isr.close();
                fis.close();

                textView.setText(content.toString()); // Display in TextView
                Log.d("INFO", "File loaded and displayed");

            } catch (IOException e) {
                Log.e("INFO", "Error reading file", e);
                textView.setText("Error reading file: " + e.getMessage());
            }

        } else {
            textView.setText("File not found: " + icsFile.getAbsolutePath());
            Log.d("INFO", "File not found at: " + icsFile.getAbsolutePath());
        }

 */



        //TextView textView = findViewById(R.id.textView1);
        //textView.setText(eventDetails);
    }

    public static List<String> parseICS(FileInputStream inputStream) {
        List<String> eventDetails = new ArrayList<>();
        StringBuilder eventBlock = new StringBuilder();
        boolean insideEvent = false;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.equals("BEGIN:VEVENT")) {
                    insideEvent = true;
                    eventBlock.setLength(0); // Clear previous event data
                }
                else if (line.equals("END:VEVENT")) {
                    insideEvent = false;
                    eventDetails.add(eventBlock.toString()); // Store the event
                }
                else if (insideEvent) {
                    if (line.startsWith("SUMMARY:")) {
                        eventBlock.append("Event: ").append(line.substring(8)).append("\n");
                    }
                    else if (line.startsWith("DESCRIPTION:")) {
                        eventBlock.append("Description: ").append(line.substring(12)).append("\n");
                    }
                    else if (line.startsWith("LOCATION:")) {
                        eventBlock.append("Location: ").append(line.substring(9)).append("\n");
                    }
                    else if (line.startsWith("DTSTART;TZID=America/Denver:")) {
                        String formattedDateStart = formatDateTime(line.substring(28));
                        eventBlock.append("Start: ").append(formattedDateStart).append("\n");
                    }
                    else if (line.startsWith("DTEND;TZID=America/Denver:")) {
                        String formattedDateEnd = formatDateTime(line.substring(26));
                        eventBlock.append("End: ").append(formattedDateEnd).append("\n");
                    }
                }
            }
        } catch (Exception e) {
            eventDetails.add("Error parsing ICS file: " + e.getMessage());
        }

        return eventDetails;
    }

    private void displayEvents(List<String> events) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String event : events) {
            stringBuilder.append(event).append("\n\n");
        }

        TextView textView = findViewById(R.id.textView1);
        textView.setText(stringBuilder.toString());
    }

    private static String formatDateTime(String icsDate) {
        // Ensure the string is long enough
        //if (icsDate.length() < 15) return "Invalid Date";

        Log.e("ICS", "Display icsDate: " + icsDate);

        // Extract date parts
        String year = icsDate.substring(0, 4);
        String month = icsDate.substring(4, 6);
        String day = icsDate.substring(6, 8); //8 = T
        String hour = icsDate.substring(9, 11);
        String minute = icsDate.substring(11, 13);

        // Convert month number to month name manually
        String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        int monthIndex = Integer.parseInt(month) - 1; // Convert "02" to index 1
        String monthName = (monthIndex >= 0 && monthIndex < 12) ? monthNames[monthIndex] : "Unknown";

        // Convert hour to 12-hour format manually
        int hourInt = Integer.parseInt(hour);
        String period = (hourInt >= 12) ? "PM" : "AM";
        int hour12 = (hourInt == 0) ? 12 : (hourInt > 12 ? hourInt - 12 : hourInt); // Convert 00:00 to 12:00 AM

        // Return formatted string
        return monthName + " " + day + ", " + year + " - " + hour12 + ":" + minute + " " + period;
    }

}
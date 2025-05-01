package com.example.otfapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.net.URL;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.net.HttpURLConnection;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Summary;
import java.io.StringReader;
import java.util.List;


public class CalendarActivity extends AppCompatActivity {

    private static final String iCalLink = "https://calendar.google.com/calendar/ical/6f04ab60cadd7634d63b71c23703691710283ea6ea77d6abb04a585c09818a32%40group.calendar.google.com/private-34c64368bd5e39f085179411d3fab82b/basic.ics";
    CalendarView calendar;
    TextView date_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calendar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        calendar = findViewById(R.id.calendarView);
        date_view = findViewById(R.id.dateView);

        // listener for when date selected is changed
        calendar.setOnDateChangeListener(
                (view, year, month, dayOfMonth) -> {
                    String date = (month+1) + "-" + dayOfMonth + "-" + year;
                    date_view.setText(date);

                    date = year + "-" + (month+1) + "-" + dayOfMonth;
                    Date selectedDate = null;
                    try {
                        selectedDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    List<String> eventDetails = EventHandling.getEventsForDate(this, selectedDate);
                    displayEvents(eventDetails);
                }
        );

    }

    private void displayEvents(List<String> events) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String event : events) {
            stringBuilder.append(event).append("\n\n");
        }

        TextView textView = findViewById(R.id.eventText);
        textView.setText(stringBuilder.toString());
    }

    public void changeActivity(View v) {
        // when a button in main menu is pressed, will switch to corresponding activity
        // right now this is Back Button
        Intent i;
        String title = ((Button) v).getText().toString();
        switch (title) {
            case "Back":
                i = new Intent(this, MainActivity.class);
                break;
            case "Create Event":
                i = new Intent(this, Cal_CreateEventActivity.class);
            default:
                i = new Intent(this, Cal_CreateEventActivity.class);

        }
        startActivity(i);
    }

    // run the thing
    public void getiCal(View view) {
        System.out.println("getiCal running");
        // check for internet connection
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean connected =false;
        if (connectivityManager != null) {
            Network network = connectivityManager.getActiveNetwork();
            if (network!=null) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
                connected = (capabilities!=null) &&
                        (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                         capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                         capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));

            }
        }

        if (connected){
            fetchURLContent(iCalLink, this::handleiCal);
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this,"syncing with OTF, please wait several minutes", duration);
            toast.show();
        } else {
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this,"please connect to internet", duration);
            toast.show();
        }

    }

    // get iCal link
    public void fetchURLContent(String urlString, FetchCallback callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                StringBuilder result = new StringBuilder();
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line).append("\n");
                    }
                }
                handler.post(() -> callback.onResult(result.toString()));

            } catch (Exception e) {
                handler.post(() -> callback.onResult("Error: " + e.getMessage()));
            }
        });
    }
    public interface FetchCallback {
        void onResult(String result);
    }

    private void handleiCal(String iCalString) {
        System.out.println(iCalString);
        System.out.println("running handleiCal");

        // if we don't have read calendar permission, ask for it
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR},100);
        }

        // store iCal events in a net.fortuna calendar
        Calendar iCalCal = parseICal(iCalString);


        // get events out of net.fortuna calendar
        List<VEvent> events = iCalCal.getComponents(VEvent.VEVENT);

        Context context = getApplicationContext();
        // if we don't have read calendar permission, ask for it
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR},100);
        }

        // now create events in Native Calendar
        int i = 0;
        for (VEvent event : events) {
            i++;
            System.out.println(i);
            Summary summary = event.getSummary();
            String title = summary!= null ? summary.getValue() : "No Title";
            //String startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(event.getStartDate().getDate());
            Date start = event.getStartDate().getDate();
            Date end = event.getEndDate().getDate();
            boolean isAllDay = event.getStartDate().toString().contains("VALUE=DATE");
            EventHandling.addEvent(context, title, start, end, isAllDay);

        }
    }

    // get iCal events into Calendar Events
    public Calendar parseICal(String icalString) {
        System.out.println("parsing iCal");
        System.setProperty("net.fortuna.ical4j.timezone.cache.impl", "net.fortuna.ical4j.util.MapTimeZoneCache");
        Calendar icalendar = null;
        try{
            StringReader sin = new StringReader(icalString); // read iCal string into sin
            CalendarBuilder builder = new CalendarBuilder();
            icalendar = builder.build(sin); // build calendar with iCal string

        } catch (Exception e) {
            //e.printStackTrace();
            Log.d("System.out","parseICal Error :" + e);
        }
        return icalendar;
    }

    public void getCalID(){

        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String[] projection = new String[]{
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME
        };
        try {
            Cursor cursor = getContentResolver().query(uri, projection, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    long calendarId = cursor.getLong(0);
                    String calName = cursor.getString(1);
                    System.out.println("Calendar ID and name: ");
                    System.out.println(calendarId);
                    System.out.println(calName);
                }
                cursor.close();
            }


        } catch (Exception e) {
            Log.d("System.out","getCalID error " + e);
        }

    }

}


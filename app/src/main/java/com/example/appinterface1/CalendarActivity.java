package com.example.appinterface1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.net.URL;
import android.os.Handler;
import android.os.Looper;
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
import net.fortuna.ical4j.util.CompatibilityHints;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;



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
                }
        );

        getiCal();

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
            default:
                i = new Intent(this, CalendarActivity.class);

        }
        startActivity(i);
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

    // run the thing
    public void getiCal() {
        System.out.println("getiCal running");
        fetchURLContent(iCalLink, this::handleiCal);
    }

    private void handleiCal(String iCalString) {
        System.out.println(iCalString);
        System.out.println("testing parseIcal");
        parseICal(iCalString);
    }

    // get iCal events into Calendar Events
    public void parseICal(String icalString) {
        System.setProperty("net.fortuna.ical4j.timezone.cache.impl", "net.fortuna.ical4j.util.MapTimeZoneCache");
        try{
            //CompatibilityHints.setHintEnabled(CompatibilityHints.KEY_RELAXED_PARSING,true);
            StringReader sin = new StringReader(icalString); // read iCal string into sin
            System.out.println("string reader");
            System.out.println(sin);
            CalendarBuilder builder = new CalendarBuilder();
            Calendar calendar = builder.build(sin); // build calendar with iCal string

            List<VEvent> events = calendar.getComponents(VEvent.VEVENT);
            // test function by printing events
            for (VEvent event : events) {
                Summary summary = event.getSummary();
                String title = summary!= null ? summary.getValue() : "No Title";
                String startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(event.getStartDate().getDate());
                System.out.println((summary));
                System.out.println(title);
                System.out.println(startDate);
            }
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("parseICal Error:");
            System.out.println(e);
        }
    }
}


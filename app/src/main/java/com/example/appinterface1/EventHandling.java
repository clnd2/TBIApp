package com.example.appinterface1;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class EventHandling extends Activity {
    public static void addEvent(Context context, String title, Date startDate, Date endDate, boolean isAllDay) {
        // input: title, start, end
        // adds event to native calendar with ID=1

        long calID = 1;
        long startMillis = startDate.getTime();
        long endMillis = endDate.getTime();

        // check for duplicates
        if (EventAlreadyExists(context, title, startMillis, endMillis)) {
            System.out.println("event already exists");
            return;
        }

        System.out.println("Adding Event");
        Log.d("System.out", "Title: " + title);
        Log.d("System.out", "start date: " + startDate);
        Log.d("System.out", "end date: " + endDate);


        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.ALL_DAY, isAllDay);

        Uri uri = context.getContentResolver().insert(CalendarContract.Events.CONTENT_URI, values);

        if (uri != null) {
            long eventID = Long.parseLong(uri.getLastPathSegment());
            Log.d("System.out", "event ID: " + eventID);
        }
    }
    // Method to check if a calendar event already exists with the given parameters
    public static boolean EventAlreadyExists(Context context, String title, long startMillis, long endMillis) {
        // URI pointing to the Calendar Events table
        Uri uri = CalendarContract.Events.CONTENT_URI;

        // ID of the calendar to query; hardcoded here as 1 (could be dynamic in a real application)
        long calID = 1;

        // Flag to indicate whether the event exists
        boolean eventExists = false;

        // Specify the columns we want to retrieve; here we only need the event ID
        String[] projection = new String[]{CalendarContract.Events._ID};

        // Define the selection criteria (WHERE clause) for the query
        String selection = CalendarContract.Events.CALENDAR_ID + " = ? AND " +
                CalendarContract.Events.TITLE + " = ? AND " +
                CalendarContract.Events.DTSTART + " = ? AND " +
                CalendarContract.Events.DTEND + " = ?";

        // Provide the values to match against the selection criteria
        String[] selectionArgs = new String[] {
                String.valueOf(calID),
                title,
                String.valueOf(startMillis),
                String.valueOf(endMillis)
        };

        // Perform the query on the calendar content provider
        Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);

        // If the cursor is not null, check if any results were returned
        if (cursor != null) {
            // If one or more results are found, the event already exists
            eventExists = (cursor.getCount() > 0);
            cursor.close(); // Always close the cursor to avoid memory leaks
        }

        // Return true if the event exists, false otherwise
        return eventExists;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    public static List<String> getEventsForDate(Context context, Date selectedDate) {
        List<String> events = new ArrayList<>();

        // Define the start of the day (00:00:00.000)
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(selectedDate);
        startCal.set(Calendar.HOUR_OF_DAY, 0);
        startCal.set(Calendar.MINUTE, 0);
        startCal.set(Calendar.SECOND, 0);
        startCal.set(Calendar.MILLISECOND, 0);
        long startMillis = startCal.getTimeInMillis();

        // Define the end of the day (23:59:59.999)
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(selectedDate);
        endCal.set(Calendar.HOUR_OF_DAY, 23);
        endCal.set(Calendar.MINUTE, 59);
        endCal.set(Calendar.SECOND, 59);
        endCal.set(Calendar.MILLISECOND, 999);
        long endMillis = endCal.getTimeInMillis();

        // Calendar provider URI
        Uri uri = CalendarContract.Events.CONTENT_URI;

        // Specify columns to retrieve
        String[] projection = new String[]{
                CalendarContract.Events.TITLE,
                CalendarContract.Events.DTSTART,
                CalendarContract.Events.DTEND,
                CalendarContract.Events.DESCRIPTION
        };

        // Filter events where start or end time falls within selected date
        String selection = "(" + CalendarContract.Events.DTSTART + " >= ? AND " + CalendarContract.Events.DTSTART + " <= ?)" +
                " OR (" + CalendarContract.Events.DTEND + " >= ? AND " + CalendarContract.Events.DTEND + " <= ?)";
        String[] selectionArgs = new String[]{
                String.valueOf(startMillis),
                String.valueOf(endMillis),
                String.valueOf(startMillis),
                String.valueOf(endMillis)
        };

        // Execute query
        Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);

        if (cursor != null) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

            while (cursor.moveToNext()) {
                String title = cursor.getString(0);
                long dtStart = cursor.getLong(1);
                long dtEnd = cursor.getLong(2);
                String description = cursor.getString(3);

                String timeRange = timeFormat.format(new Date(dtStart)) + " - " + timeFormat.format(new Date(dtEnd));
                String eventInfo = "Title: " + title + "\nTime: " + timeRange + "\nDescription: " +
                        (description != null ? description : "No description");
                events.add(eventInfo);
            }
            cursor.close();
        }

        return events;
    }
}

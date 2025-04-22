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

import java.util.Date;
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
    public static boolean EventAlreadyExists(Context context, String title, long startMillis, long endMillis) {
        Uri uri = CalendarContract.Events.CONTENT_URI;
        long calID = 1;
        boolean eventExists = false;

        String[] projection = new String[]{CalendarContract.Events._ID};
        String selection = CalendarContract.Events.CALENDAR_ID + " = ? AND " +
                CalendarContract.Events.TITLE + " = ? AND " +
                CalendarContract.Events.DTSTART + " = ? AND " +
                CalendarContract.Events.DTEND + " = ?";
        String[] selectionArgs = new String[] {
                String.valueOf(calID),
                title,
                String.valueOf(startMillis),
                String.valueOf(endMillis)
        };

        Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
        if (cursor != null) {
            eventExists = (cursor.getCount() > 0);
            cursor.close();
        }
        return eventExists;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}

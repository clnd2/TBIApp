package com.example.appinterface1;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.Manifest;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //requestCalendarPermission();
    }

    public void changeActivity(View v) {
        // when a button in main menu is pressed, will switch to corresponding activity

        Intent i = null;
        String title = ((Button) v).getText().toString();
        switch (title) {
            case "Calendar":
               // Intent intent = new Intent(Intent.ACTION_VIEW);
               // intent.setData(Uri.parse("content://com.android.calendar/time/" + System.currentTimeMillis()));
                break;
            case "Reminders":
                i = new Intent(this, RemindersActivity.class);
                break;
            case "Games":
                i = new Intent(this, GamesActivity.class);
                break;
            case "Messages":
                i = new Intent(this, MessagesActivity.class);
                break;
            case "Profile":
                i = new Intent(this, ProfileActivity.class);

                break;
            default:
                i = new Intent(this, MessagesActivity.class);

        }
        startActivity(i);

    }



}
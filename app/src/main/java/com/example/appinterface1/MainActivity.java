package com.example.appinterface1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
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
    }

    public void changeActivity(View v) {
        // when a button in main menu is pressed, will switch to corresponding activity

        Intent i;
        String title = ((Button) v).getText().toString();
        switch (title) {
            case "Calendar":
                i = new Intent(this, CalendarActivity.class);
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
            case "Password Manager":
                i = new Intent(this, choiceScreen.class);
                break;
            default:
                i = new Intent(this, MainActivity.class);

        }
        startActivity(i);

    }
}
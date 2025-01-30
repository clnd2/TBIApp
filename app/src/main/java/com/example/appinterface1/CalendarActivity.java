package com.example.appinterface1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CalendarActivity extends AppCompatActivity {

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
                new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(
                            @NonNull CalendarView view,
                            int year,
                            int month,
                            int dayOfMonth)
                    {
                        String date = (month+1) + "-" + dayOfMonth + "-" + year;
                        date_view.setText(date);
                    }
                }
        );

    }

    public void changeActivity(View v) {
        // when a button in main menu is pressed, will switch to corresponding activity

        Intent i = null;
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
}
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

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void changeActivity(View p) {
        // when a button in main menu is pressed, will switch to corresponding activity

        Intent i = null;
        String title = ((Button) p).getText().toString();
        switch (title) {
            case "Caretaker Settings":
                i = new Intent(this, CaretakerActivity.class);
                break;
            case "home":
                i = new Intent(this, MainActivity.class);
                break;
            default:
                i = new Intent(this, ProfileActivity.class);

        }
        startActivity(i);

    }
}


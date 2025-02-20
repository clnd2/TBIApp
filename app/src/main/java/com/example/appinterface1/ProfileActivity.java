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

    public void changeActivity(View v) {
        // when a button in main menu is pressed, will switch to corresponding activity

        Intent i;
        int id = v.getId();

        // if-else instead of switch-case because of non-final nature of resource IDs
        if (id == R.id.caretakerButton) {
            i = new Intent(this, CaretakerActivity.class);
        } else if (id == R.id.homeButton) {
            i = new Intent(this, MainActivity.class);
        } else {
            i = new Intent(this, ProfileActivity.class);
        }

        // start activity
        startActivity(i);
    }
}


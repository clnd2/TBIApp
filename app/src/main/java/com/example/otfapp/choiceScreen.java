package com.example.otfapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class choiceScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choice_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void selectOptions(View SP) {
        Intent i;
        int id = SP.getId();

        if (id == R.id.homeButton) {
            i = new Intent(this, MainActivity.class);
        } else if (id == R.id.showButton) {
            i = new Intent(this, pswdShow.class);
        } else if (id == R.id.saveButton) {
            i = new Intent(this, pswdSave.class);
        } else if (id == R.id.editButton) {
            i = new Intent(this, pswdEdit.class);
        } else {
            i = new Intent(this, choiceScreen.class);
        }

        startActivity(i);
    }

    public void homeScreen(View H) {
        Intent homeScreen = new Intent(this, MainActivity.class);
        startActivity(homeScreen);
    }

}
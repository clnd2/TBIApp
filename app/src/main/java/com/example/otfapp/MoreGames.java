package com.example.otfapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MoreGames extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_more_games);
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
        if (id == R.id.Sudoku) {
            i = new Intent(this, SudokuGame.class);
        } else if (id == R.id.G2048) {
            i = new Intent(this, Game2048.class);
        } else if (id == R.id.WordSearch) {
            i = new Intent(this, WordSearch.class);
        } else if (id == R.id.Home) {
                i = new Intent(this, MainActivity.class);
        } else {
            i = new Intent(this, MainActivity.class);
        }

        // start activity
        startActivity(i);
    }

}

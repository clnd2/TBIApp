package com.example.appinterface1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ShowRemindersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_reminders2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        displayICS();

    }

    public void home(View v) {

        Intent i = null;

        i = new Intent(this, MainActivity.class);

        startActivity(i);

    }

    private void displayICS ()
    {
        File directory = getFilesDir();
        File icsFile = new File(directory, "event.ics");

        StringBuilder stringBuilder = new StringBuilder();
        try
        {
            FileInputStream fis = new FileInputStream(icsFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            reader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        TextView textView = findViewById(R.id.textView);
        textView.setText(stringBuilder.toString());
    }


}
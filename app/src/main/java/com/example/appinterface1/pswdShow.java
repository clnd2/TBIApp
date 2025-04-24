package com.example.appinterface1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.io.File; //Added import
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

import android.widget.TextView; //Added Import
import android.util.Log; //Added Import

public class pswdShow extends AppCompatActivity {

    //Global Variables
    String fileName = "Password List.txt";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pswd_show);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //showPSWD()
        TextView text = findViewById(R.id.fileShow);
        String data = readFromFile(fileName);

        if (data.isEmpty()) {
            data = "There are no passwords saved yet.";
        }

        else {
            Log.d("TAG", data);
        }

        text.setText(data);

        //Make the scroll for the passwords
        text.setMovementMethod(new ScrollingMovementMethod());
    }

    public void homeScreen(View H) {
        Intent homeScreen = new Intent(this, MainActivity.class);
        startActivity(homeScreen);
    }

    public String readFromFile(String fileName) {
        File path = getFilesDir();
        File readFrom = new File(path, fileName);
        byte[] content = new byte[(int) readFrom.length()];
        try {
            FileInputStream stream = new FileInputStream(readFrom);
            stream.read(content);
            return new String(content);
        } catch (Exception e) {
            Log.d("TAG", "There was an error reading file in the try block.");
            return new String(content);
        }
    }
}

//Change
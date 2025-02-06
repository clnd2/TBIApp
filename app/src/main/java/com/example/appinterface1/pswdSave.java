package com.example.appinterface1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.io.*; //Added import

public class pswdSave extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pswd_save);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    protected void savePSWD() {

        String FILE_NAME = "Password Storage.txt";

        System.out.print("Enter One Word Description: "); //Needs to be from a text box
        String detail = scanner.nextLine();

        System.out.print("Enter Username (Case Sensitive): "); //Needs to be from a text box
        String userName = scanner.nextLine();

        System.out.print("Enter Password (Case Sensitive): "); //Needs to be from a text box
        String userPass = scanner.nextLine();

        //R.id.textveiw8

        File file = new File(FILE_NAME);
        try {
            if (file.exists()) {
                try (FileWriter writer = new FileWriter(file, true)) {
                    String passWD = String.format("Detail: %s\nUsername: %s\nPassword: %s\n", detail, userName, userPass);
                    writer.write("\n" + passWD);
                }
            }
            else {
                try (FileWriter writer = new FileWriter(file)) {
                    String passWD = String.format("Detail: %s\nUsername: %s\nPassword: %s\n", detail, userName, userPass);
                    writer.write(passWD);
                }
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public void homeScreen(View H) {
        Intent homeScreen = new Intent(this, MainActivity.class);
        startActivity(homeScreen);
    }
}
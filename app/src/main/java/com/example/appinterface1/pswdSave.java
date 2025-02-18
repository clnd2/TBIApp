package com.example.appinterface1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.io.File; //Added Import
import java.io.IOException; //Added Import
import java.io.FileWriter; //Added Import

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

        File psdStore = new File("Password Storage.txt");

        try {
            if (!psdStore.exists()) {
                psdStore.createNewFile();
            }

        } catch (IOException e) {
            System.out.println("Error Happened Trying to Create File");
            e.printStackTrace();
        }

        System.out.println("File Created"); //To show that the file was created

        //Needs to be from a text box
        EditText desText = findViewById(R.id.desText);
        String userDec = desText.getText().toString();

        //Needs to be from a text box
        EditText userText = findViewById(R.id.userText);
        String userNam = userText.getText().toString();

        //Needs to be from a text box
        EditText passText = findViewById(R.id.passText);
        String userPass = passText.getText().toString();

        try {
            FileWriter myWriter = new FileWriter("Password Storage.txt");
            myWriter.write("Detail: %s\nUsername: %s\nPassword: %s\n", userDec, userNam, userPass);
            myWriter.close();
            System.out.println("Successfully Wrote to File");

        } catch (IOException e) {
            System.out.println("Error Happened Trying to Write File");
            e.printStackTrace();
        }
    }

    public void homeScreen(View H) {
        Intent homeScreen = new Intent(this, MainActivity.class);
        startActivity(homeScreen);
    }
}
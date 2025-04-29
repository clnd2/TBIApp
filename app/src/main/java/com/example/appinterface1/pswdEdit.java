package com.example.appinterface1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File; //Added Import
import java.io.FileOutputStream; //Added Import
import java.io.FileInputStream; //Added Import
import java.io.IOException; //Added Import
import java.util.Arrays;

public class pswdEdit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pswd_edit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    //Function that does the password changes
    public void changePassword (String changeDec, String changeNam, String changePass, String fileName) {
        String data = readFromFile(fileName);
        Log.d("TAG", data); //Delete for string testing

        //Split data into words to find what to change
        String regex = ":\\s+|\\n+";
        String[] words = data.split(regex);

        Log.d("TAG", Arrays.toString(words)); //Delete for string testing

        //Using description to find the username and password to change and changing in string
        int i = 0;
        int checkVal = 0;
        for (String word : words) {
            if (word.equals(changeDec)) {
                Log.d("TAG", "Correct Description was found to replace"); //Delete for string testing

                words[i + 2] = changeNam;
                words[i + 4] = changePass;

                //Check Val to know that the password was found and edited so does not need to be added
                checkVal = 1;
                break;
            }
            i++;
        }

        File path = getFilesDir();
        File fileP = new File(path, fileName);
        if (checkVal == 1) {
            //Delete the old file, does not work yet
            if (fileP.delete()) {
                Log.d("TAG", "File deleted now doing now making new one."); //Delete for string testing

                int j = 0;
                for (String ignored : words) {
                    //Create the data string
                    String first = ("Detail: " + words[j + 1]);
                    String second = ("\nUsername: " + words[j + 3]);
                    String third = ("\nPassword: " + words[j + 5] + "\n\n");
                    String content = (first + second + third);
                    Log.d("TAG", content); //Delete for string testing

                    //Add to the file
                    try {
                        //Create the file
                        FileOutputStream fos = new FileOutputStream(fileP, true);

                        //Write to the file
                        fos.write(content.getBytes());
                        fos.close();

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    j = j + 6;
                }
                //To show the edit is complete
                Toast.makeText(getApplicationContext(), changeDec + " password edited.", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("TAG", "Error deleting the file."); //Delete for string testing
            }
        }

        //That password does not exist so adding to the password file
        else {
            Log.d("TAG", "Password not there creating now."); //Delete for string testing

            //Create the data string
            String first = ("Detail: " + changeDec);
            String second = ("\nUsername: " + changeNam);
            String third = ("\nPassword: " + changePass + "\n\n");
            String content = (first + second + third);

            //Add to the file
            try {
                //Access the file
                FileOutputStream fos = new FileOutputStream(fileP, true);

                //Write to the file
                fos.write(content.getBytes());
                fos.close();
                Toast.makeText(getApplicationContext(), "Wrote to file: " + fileName, Toast.LENGTH_SHORT).show();
                Log.d("TAG", "The file was written to.");

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        //Go back to the choice screen after edit password
        Intent choiceScreen = new Intent(this, com.example.appinterface1.choiceScreen.class);
        startActivity(choiceScreen);
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
            return "Nothing";
        }
    }

    public void editPSWD(View view) {
        //Log.d("TAG", "Edit password called");
        String fileName = "Password List.txt";

        EditText desText = findViewById(R.id.editDescription);
        String changeDec = desText.getText().toString();

        EditText userText = findViewById(R.id.editUserName);
        String changeNam = userText.getText().toString();

        EditText passText = findViewById(R.id.editPassword);
        String changePass = passText.getText().toString();

        changePassword(changeDec, changeNam, changePass, fileName);
    }

    public void homeScreen(View H) {
        Intent homeScreen = new Intent(this, MainActivity.class);
        startActivity(homeScreen);
    }
}
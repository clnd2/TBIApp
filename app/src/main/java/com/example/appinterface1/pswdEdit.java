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
import java.nio.file.Files;
import java.util.Objects;

import android.util.Log; //Added Import

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
        String oldNam = "";
        String oldPass = "";
        String data = readFromFile(fileName);

        //Split data into words to find what to change
        String[] words = data.split(" ");

        //Using description to find the username and password to change, Need to figure out how to get the username and pass from the word list
        for (String word : words) {
            Log.d("TAG", word); //Delete for string testing
            if (Objects.equals(word, changeDec)) {
                Log.d("TAG", "Correct Description was found to replace"); //Delete for string testing
                //oldNam = word[+2];
                //oldPass = word[+2];
            }
        }

        //String namChange = data.replace(oldNam, changeNam);
        //String contentChange = namChange.replace(oldPass, changePass);

        //Log.d("TAG", contentChange); //Delete for string testing

        //deleteFile(fileName);

        //CreateAndWriteFile(fileName, changeContent);
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
            throw new RuntimeException(e);
        }
    }

    /*public void CreateAndWriteFile(String fileName, String Content) {
        try {
            //Getting the file path
            File path = getFilesDir();

            //Create the file
            File fileP = new File(path, fileName);
            FileOutputStream fos = new FileOutputStream(fileP, true);

            //Write to the file
            fos.write(Content.getBytes());
            fos.close();
            Toast.makeText(getApplicationContext(), "Wrote to file: " + fileName, Toast.LENGTH_SHORT).show();
            Log.d("TAG", "The file was created and written to.");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/

    public void editPSWD(View view) {

        String fileName = "Password List.txt";

        EditText desText = findViewById(R.id.editDescription);
        //String changeDec = desText.getText().toString();

        EditText userText = findViewById(R.id.editUserName);
        //String changeNam = userText.getText().toString();

        EditText passText = findViewById(R.id.editPassword);
        //String changePass = passText.getText().toString();

        String changeDec = "testq"; //For testing
        String changeNam = "secondBDub"; //For testing
        String changePass = "thirdRob"; //For testing

        changePassword(changeDec, changeNam, changePass, fileName);
    }

    /*public void deleteFile(String fileName){ Only for is the delete file function does not work
        File path = getFilesDir();
        File fileP = new File(path, fileName);
        try{
            if (Files.deleteIfExists(fileP.toPath())) {
                System.out.println("File was deleted to create the new one.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/
    public void homeScreen(View H) {
        Intent homeScreen = new Intent(this, MainActivity.class);
        startActivity(homeScreen);
    }
}
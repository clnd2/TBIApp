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
import java.util.Objects; //Added Import
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
        String data = readFromFile(fileName);
        Log.d("TAG", data); //Delete for string testing

        //Split data into words to find what to change
        String regex = ":\\s+|\\n+";
        String[] words = data.split(regex);


        Log.d("TAG", Arrays.toString(words)); //Delete for string testing

        //Using description to find the username and password to change
        int i = 0;
        int checkVal = 0;
        for (String word : words) {
            if (word.equals(changeDec)) {
                Log.d("TAG", "Correct Description was found to replace"); //Delete for string testing
                String oldNam = words[i + 2];
                String oldPass = words[i + 4];
                Log.d("TAG", oldNam); //Delete for string testing
                Log.d("TAG", oldPass); //Delete for string testing

                //Changing the data in the file //Issue Here with changing file cannot do .replace at it changes whenever that word is seen
                //String namChange = data.replace(oldNam, changeNam);
                //String editContent = namChange.replace(oldPass, changePass);
                words[i + 2] = changeNam;
                words[i + 4] = changePass;
                //Log.d("TAG", editContent); //Delete for string testing
                Log.d("TAG", Arrays.toString(words)); //Delete for string testing
                String updatedPass = String.join(regex, words);

                //Delete the file that had the old data, Does not work
                //Getting the file path
                File path = getFilesDir();

                //Create the file
                File fileP = new File(path, fileName);
                if (fileP.delete()) {
                    Log.d("TAG", "File deleted now doing now making new one."); //Delete for string testing

                    //Create a new file that has the new information
                    CreateAndWriteFile(fileName, updatedPass);
                }
                else {
                    Log.d("TAG", "Error deleting the file."); //Delete for string testing
                }

                //Check Val to know that the password was found and edited so does not need to be added
                checkVal = 1;
                break;
            }
            i++;
        }

        //That password does not exist so adding to the password file
        if (checkVal == 0){
            //Do the addition of the password here
            Log.d("TAG", "Password not there creating now."); //Delete for string testing

            //Create the data string
            String first = ("Detail: " + changeDec);
            String second = ("\nUsername: " + changeNam);
            String third = ("\nPassword: " + changePass + "\n\n");
            String content = (first + second + third);

            //Add to the file
            try {
                //Getting the file path
                File path = getFilesDir();

                //Create the file
                File fileP = new File(path, fileName);
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
        Toast.makeText(getApplicationContext(), changeDec + " password edited.", Toast.LENGTH_SHORT).show();
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
            throw new RuntimeException(e);
        }
    }

    public void CreateAndWriteFile(String fileName, String Content) {
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

    //To delete file but may not need and will be deleted
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
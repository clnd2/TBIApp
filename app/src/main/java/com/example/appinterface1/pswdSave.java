package com.example.appinterface1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.io.File; //Added Import
import java.io.FileOutputStream;
//import java.io.IOException; //Added Import

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

        //createFile("Password Storage.txt");

        //Needs to be from a text box
        EditText desText = findViewById(R.id.desText);
        String userDec = desText.getText().toString();

        //Needs to be from a text box
        EditText userText = findViewById(R.id.userText);
        String userNam = userText.getText().toString();

        //Needs to be from a text box
        EditText passText = findViewById(R.id.passText);
        String userPass = passText.getText().toString();

        //Formulating the message
        String first = ("Detail: " + userDec);
        String second = ("\nUsername: " + userNam);
        String third = ("\nPassword: " + userPass);
        String content = (first + second + third);

        writeToFile("Password Storage.txt", content);
    }

    public void homeScreen(View H) {
        Intent homeScreen = new Intent(this, MainActivity.class);
        startActivity(homeScreen);
    }

    public void writeToFile(String filename, String content) {
        File path = getApplicationContext().getFilesDir();
        try {
            FileOutputStream writer = new FileOutputStream(new File(path, filename));
            writer.write(content.getBytes());
            writer.close();
            Toast.makeText(getApplicationContext(), "Wrote to file: " + filename, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

   /* public void createFile(String fileName) {
        try{
            File root = new File(Environment.getExternalStorageDirectory(), "My Folder"); //Creating the folder of My Folder

            if(!root.exists()){
                root.mkdir(); //To make the folder if it does not exist
            }

            File filepath = new File(root, fileName); //Not sure when the physical file is made directly


        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

}

/*String fileName = "Documents\\Password Storage.txt";
File psdStore = new File(fileName);

        try {
boolean fileCheck = psdStore.createNewFile();

                if (fileCheck) {
        System.out.println("File did not exist and was created");
                }

                        else {
                        System.out.println("File already exists, appending file.");
                }

                        } catch (IOException e) {
        System.out.println("Error Happened Trying to Create File");
            e.printStackTrace();
        }

                System.out.println("File Created"); //To show that the file was created*/
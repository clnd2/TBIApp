package com.example.appinterface1;
//import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import java.io.IOException; //Added Import
import android.util.Log; //Added Import

public class pswdSave extends AppCompatActivity {

    //Global Variables
    String fileName = "Password List.txt";

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

    //Testing if the file existing works and if the append works, if not take out the fileCheck and the if statement leaving everything in the else
    public void CreateAndWriteFile(String fileName, String Content) {
        //Checks to see if the file already exists
        File fileCheck = new File(getApplicationContext().getFilesDir(), fileName);

        //If it already exists then just append
        if (fileCheck.exists()) {
            Log.d("TAG", "The file already exists append.");

          //If it does not exist then create and write to
        } else {
            try {
                //Getting the file path
                File path = getFilesDir();

                //Create the file
                File fileP = new File(path, fileName);
                FileOutputStream fos = new FileOutputStream(fileP);

                //Write to the file
                fos.write(Content.getBytes());
                fos.close();
                Toast.makeText(getApplicationContext(), "Wrote to file: " + fileName, Toast.LENGTH_SHORT).show();
                Log.d("TAG", "The file was created and written to.");

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void savePSWD(View view) {
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

        //Function that creates and writes to the file
        CreateAndWriteFile(fileName, content);

        //Go back to the choice screen after saved password
        Intent choiceScreen = new Intent(this, com.example.appinterface1.choiceScreen.class);
        startActivity(choiceScreen);
    }

    public void homeScreen(View H) {
        Intent homeScreen = new Intent(this, MainActivity.class);
        startActivity(homeScreen);
    }

}
package com.example.appinterface1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.io.File; //Added import
import java.io.FileInputStream;
import android.widget.TextView; //Added Import
import android.util.Log; //Added Import

public class pswdShow extends AppCompatActivity {

    //Global Variables
    String fileName = "PasswordList.txt";

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
        text.setText(data);
    }

   /* protected void showPSWD() {

     String FILE_NAME = "Password Storage.txt";

     File file = new File(FILE_NAME);

     if(file.exists()) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
        catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
     else {
        System.out.println("File Does Not Exist");
    }
}*/

    /*File psdStorage = new File("Password Storage.txt");
    TextView visiblePSWD = findViewById(R.id.fileShow);

       try {
        Scanner myReader = new Scanner(psdStorage);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            //System.out.println(data);
            visiblePSWD.setText(data);
        }
        myReader.close();

    } catch (FileNotFoundException e) {
        System.out.println("An Error Occurred Trying to Read the File");
        e.printStackTrace();
    }*/

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
            throw new RuntimeException(e);
        }
    }
}
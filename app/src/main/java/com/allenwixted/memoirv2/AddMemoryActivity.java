package com.allenwixted.memoirv2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class AddMemoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memory);


        Button saveButton = (Button) findViewById(R.id.saveButton);
        if (saveButton != null) {
            saveButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){

                    Context context = getApplicationContext();
                    File titles = new File(context.getFilesDir(), getString(R.string.title));
                    File desc = new File(context.getFilesDir(), getString(R.string.desc));
                    File lon = new File(context.getFilesDir(), getString(R.string.lon));
                    File lat = new File(context.getFilesDir(), getString(R.string.lat));

                    EditText title = (EditText) findViewById(R.id.editText);
                    String titleText = title.getText().toString();

                    EditText description = (EditText) findViewById(R.id.editText2);
                    String descText = description.getText().toString();


                    writeToFile(getString(R.string.title), String.format("%s\n", titleText));
                    writeToFile(getString(R.string.desc),String.format("%s\n", descText));
                    writeToFile(getString(R.string.lon),titleText);
                    writeToFile(getString(R.string.lat),titleText);



                    CharSequence text = "Memory Saved!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                }
            });
        }
    }

    private void writeToFile(String filename, String data )
    {

        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

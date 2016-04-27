package com.allenwixted.memoirv2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import icepick.State;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreateOptionsMenu(Menu menu){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Adds functionality for the view memory button
        Button logButton = (Button) findViewById(R.id.mainLogButton);
        if (logButton != null) {
            logButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent i = new Intent(getApplicationContext(), AddMemoryActivity.class);
                    startActivity(i);
                }
            });
        }

        //Adds functionality for the map button
        Button viewButton = (Button) findViewById(R.id.mainViewButton);
        if (viewButton != null) {
            viewButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent i = new Intent(getApplicationContext(), MapActivity.class);
                    startActivity(i);
                }
            });
        }
    }
}

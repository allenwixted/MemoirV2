package com.allenwixted.memoirv2;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class helpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }

    //Adds a custom menu to the activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //Detect clicks in the action bar and decide what to do with them
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.action_help){
            Intent i = new Intent(getApplicationContext(), helpActivity.class);
            startActivity(i);
            return true;
        }

        else{

        }

        return super.onOptionsItemSelected(item);
    }
}

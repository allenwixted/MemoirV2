package com.allenwixted.memoirv2;

//API KEY: AIzaSyBihlHuDZ-WJL3LKjKMOmFlFWfxtl0UztE

//stu

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static com.allenwixted.memoirv2.R.string.lat;

public class MapActivity extends AppCompatActivity {

    ArrayList<String> titles = new ArrayList<>(1);
    ArrayList<String> descr = new ArrayList<>(1);
    ArrayList<String> lats = new ArrayList<>(1);
    ArrayList<String> longs = new ArrayList<>(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        readTitle();
        readDescription();
        readLatitude();
        readLongitude();

    }

    //Adds a custom menu to the activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //Detect clicks in the action bar and decide what to do with them
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_map) {
            Intent i = new Intent(getApplicationContext(), MapActivity.class);
            startActivity(i);
            return true;
        } else if (id == R.id.action_help) {
            Intent i = new Intent(getApplicationContext(), helpActivity.class);
            startActivity(i);
            return true;
        } else if (id == R.id.action_log) {
            Intent i = new Intent(getApplicationContext(), AddMemoryActivity.class);
            startActivity(i);
            return true;
        } else {

        }

        return super.onOptionsItemSelected(item);
    }

    public void readLatitude() {

        final Context context = getApplicationContext();
        File file = new File(String.format("%s/%s", context.getFilesDir(), getString(R.string.lat)));
        if (file.exists()) {
            FileInputStream fis = null;
            try {


                fis = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                while (fis.available() > 0) {
                    lats.add(reader.readLine());
                }
            } catch (Exception e) {
                Log.d("MemoryRead", e.toString());
            } finally {
                if (fis != null)
                    try {
                        fis.close();
                    } catch (IOException ignored) {
                    }
            }
        }
    }

    public void readLongitude() {

        final Context context = getApplicationContext();
        File file = new File(String.format("%s/%s", context.getFilesDir(), getString(R.string.lon)));
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                while (fis.available() > 0) {

                    longs.add(reader.readLine());
                }
            } catch (Exception e) {
                Log.d("MemoryRead", e.toString());
            } finally {
                if (fis != null)
                    try {
                        fis.close();
                    } catch (IOException ignored) {
                    }
            }
        }
    }

    public void readTitle() {

        final Context context = getApplicationContext();

        File file = new File(String.format("%s/%s", context.getFilesDir(), getString(R.string.title)));
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                while (fis.available() > 0) {

                    titles.add(reader.readLine());
                }
            } catch (Exception e) {
                Log.d("MemoryRead", e.toString());
            } finally {
                if (fis != null)
                    try {
                        fis.close();
                    } catch (IOException ignored) {
                    }
            }
        }

    }

    public void readDescription() {

        final Context context = getApplicationContext();

        File file = new File(String.format("%s/%s", context.getFilesDir(), getString(R.string.desc)));
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                while (fis.available() > 0) {

                    descr.add(reader.readLine());
                }
            } catch (Exception e) {
                Log.d("MemoryRead", e.toString());
            } finally {
                if (fis != null)
                    try {
                        fis.close();
                    } catch (IOException ignored) {
                    }
            }
        }

    }
}
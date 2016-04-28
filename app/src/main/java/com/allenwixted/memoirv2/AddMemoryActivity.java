package com.allenwixted.memoirv2;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class AddMemoryActivity extends AppCompatActivity {

    double latitude;
    double longitude;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memory);
        final Context context = getApplicationContext();

        //Create a file for each type of info. Titles, Descriptions, longitude and latitude of Saved Memories)
        File titles = new File(context.getFilesDir(), getString(R.string.title));
        File desc = new File(context.getFilesDir(), getString(R.string.desc));
        File lon = new File(context.getFilesDir(), getString(R.string.lon));
        File lat = new File(context.getFilesDir(), getString(R.string.lat));

        //create GPS listener
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Check permissions of GPS
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            longitude = location.getLongitude();
            latitude = location.getLatitude();

        } else //if GPS permission is disabled, notify user to enable it
        {

            CharSequence text = "GPS Permission Disabled, Please Enable!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

        //create GPS listener
        final LocationListener locationListener = new LocationListener() {
            //If location changes, update variables
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        //set GPS location update interval to 2s
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);

        Button saveButton = (Button) findViewById(R.id.saveButton);
        if (saveButton != null) {
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Create a file for each type of info. Titles, Descriptions, longitude and latitude of Saved Memories)

                    //Title of Memory
                    EditText title = (EditText) findViewById(R.id.editText);
                    EditText description = (EditText) findViewById(R.id.editText2);

                    if (title != null && description != null) {
                        String titleText = title.getText().toString();
                        String descText = description.getText().toString();
                        //Toast toast2 = Toast.makeText(context, text2, Toast.LENGTH_LONG);
                        //toast2.show();


                        //Write info to each file
                        //writeToFile(getString(R.string.title), String.format("%s\n", titleText));
                        //writeToFile(getString(R.string.desc), String.format("%s\n", descText));
                        //writeToFile(getString(R.string.lon), String.format("%f\n", longitude));
                        //writeToFile(getString(R.string.lat), String.format("%f\n", latitude));

                        writeFile(getString(R.string.title), String.format("%s\n", titleText));
                        writeFile(getString(R.string.desc), String.format("%s\n", descText));
                        writeFile(getString(R.string.lat), String.format("%s\n", latitude));
                        writeFile(getString(R.string.lon), String.format("%s\n", longitude));

                        //Test of lat and long writing
                        //writeToFile(getString(R.string.lon), String.format("%f\n", 73.89595049));
                        //writeToFile(getString(R.string.lat), String.format("%f\n", -34.89593812));

                        //Create toast to notify user that memory has been saved
                        CharSequence text = "Memory Saved!";
                        int duration = Toast.LENGTH_SHORT;

                        //Toast toast = Toast.makeText(context, text, duration);
                        //toast.show();

                        //CharSequence text2 = readFile(String.format("%s/%s", context.getFilesDir(), getString(R.string.title)));


                        CharSequence text2 = readFile(String.format("%s/%s", context.getFilesDir(), getString(R.string.title)));
                        System.out.println(text2);
                        Toast toast2 = Toast.makeText(context, text2, Toast.LENGTH_LONG);
                        toast2.show();


                    }
                }
            });
        }
    }

    //Write data to specified file
    private void writeToFile(String filename, String data)
    {
        FileOutputStream outputStream;
        try
        {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //Adds a custom menu to the activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //Detect clicks in the action bar and decide what to do with them
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_map)
        {
            Intent i = new Intent(getApplicationContext(), MapActivity.class);
            startActivity(i);
            return true;
        } else if (id == R.id.action_help)
        {
            Intent i = new Intent(getApplicationContext(), helpActivity.class);
            startActivity(i);
            return true;
        }
        else if(id == R.id.action_log)
        {
            Intent i = new Intent(getApplicationContext(), AddMemoryActivity.class);
            startActivity(i);
            return true;
        }
        else
        {

        }

        return super.onOptionsItemSelected(item);
    }


    public static String readFile(String filePath)
    {

        String result = "";
        File file = new File(filePath);
        if (file.exists())
        {
            FileInputStream fis = null;
            try
            {
                fis = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

                while (fis.available() > 0)
                {
                    result = result + reader.readLine() + "\n";
                }
            } catch (Exception e)
            {
                Log.d("MemoryRead", e.toString());
            } finally {
                if (fis != null)
                    try
                    {
                        fis.close();
                    } catch (IOException ignored)
                    {
                    }
            }
        }
        return result;
    }

    public void writeFile(String fileName, String text)
    {

        FileOutputStream fOut = null;
        try {
            fOut = openFileOutput(fileName, MODE_APPEND);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write(text);
            osw.flush();
            osw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

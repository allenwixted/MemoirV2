package com.allenwixted.memoirv2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class AddMemoryActivity extends AppCompatActivity
{

    double latitude;
    double longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memory);

        //create GPS listener
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Check permissions of GPS
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();

        } else //if GPS permission is disabled, notify user to enable it
        {
            Context context = getApplicationContext();
            CharSequence text = "GPS Permission Disabled, Please Enable!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

        //create GPS listener
        final LocationListener locationListener = new LocationListener()
        {
            //If location changes, update variables
            public void onLocationChanged(Location location)
            {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras)
            {

            }

            @Override
            public void onProviderEnabled(String provider)
            {

            }

            @Override
            public void onProviderDisabled(String provider)
            {

            }
        };

        //set GPS location update interval to 2s
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);

        Button saveButton = (Button) findViewById(R.id.saveButton);
        if (saveButton != null) {
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Context context = getApplicationContext();

                    //Create a file for each type of info. Titles, Descriptions, longitude and latitude of Saved Memories)
                    File titles = new File(context.getFilesDir(), getString(R.string.title));
                    File desc = new File(context.getFilesDir(), getString(R.string.desc));
                    File lon = new File(context.getFilesDir(), getString(R.string.lon));
                    File lat = new File(context.getFilesDir(), getString(R.string.lat));

                    //Title of Memory
                    EditText title = (EditText) findViewById(R.id.editText);
                    EditText description = (EditText) findViewById(R.id.editText2);

                    if (title != null && description != null)
                    {
                        String titleText = title.getText().toString();
                        String descText = description.getText().toString();

                        //Write info to each file
                        writeToFile(getString(R.string.title), String.format("%s\n", titleText));
                        writeToFile(getString(R.string.desc), String.format("%s\n", descText));
                        writeToFile(getString(R.string.lon), String.format("%f\n", longitude));
                        writeToFile(getString(R.string.lat), String.format("%f\n", latitude));

                        //Create toast to notify user that memory has been saved
                        CharSequence text = "Memory Saved!";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                    }
                }
            });
        }
    }

    //Write data to specified file
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

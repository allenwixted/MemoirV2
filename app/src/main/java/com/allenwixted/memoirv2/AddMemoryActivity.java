package com.allenwixted.memoirv2;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

    public static final int CAMERA_REQUEST = 10; //10 is unique identifier for camera photo capture
    public static double latitude;
    public static double longitude;
    private ImageView imgChosenPhoto; //This is the photo chosen by the user

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
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);

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

                        writeFile(getString(R.string.title), String.format("%s\n", titleText));
                        writeFile(getString(R.string.desc), String.format("%s\n", descText));
                        writeFile(getString(R.string.lat), String.format("%s\n", latitude));
                        writeFile(getString(R.string.lon), String.format("%s\n", longitude));

                        CharSequence text = readFile(getString(R.string.title));
                        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
                        toast.show();

                    }
                }
            });
        }

        Button photoButtonCapture = (Button) findViewById(R.id.photoButton);
        if (photoButtonCapture != null) {
            photoButtonCapture.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View cam) {

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            });
        }

        //get the image and cast to image view
        imgChosenPhoto = (ImageView) findViewById(R.id.chosenImage);
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


    public String readFile(String fileName)
    {
        Context context = getApplicationContext();
        String result = "";
        Log.i("DIRECTORY R", context.getFilesDir().toString());

            FileInputStream fis = null;

            try
            {
                fis = openFileInput(String.format("%s.txt",fileName));
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

                    result = reader.readLine();
                    Log.i("Read", result);

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
        //}
        return result;
    }

    public void writeFile(String fileName, String text)
    {
        Context context = getApplicationContext();
        Log.i("DIRECTORY W", context.getFilesDir().toString());

        FileOutputStream fOut = null;
        try {
            fOut = openFileOutput(String.format("%s.txt", fileName), MODE_APPEND);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write(text);
            Log.i("Write", text);
            osw.flush();
            osw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //did the user press OK?
        if(resultCode == RESULT_OK){
            if(requestCode == CAMERA_REQUEST){
                //comm back to the camera and get the image as a bitmap
                Bitmap cameraImage = (Bitmap) data.getExtras().get("data");
                imgChosenPhoto.setImageBitmap(cameraImage);
            }
        }
    }
}

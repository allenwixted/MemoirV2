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

// Stuarts Imports
import android.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
// End Stuart Imports


public class MapActivity extends AppCompatActivity {

    ArrayList<String> titles = new ArrayList<>(1);
    ArrayList<String> descr = new ArrayList<>(1);
    ArrayList<String> lats = new ArrayList<>(1);
    ArrayList<String> longs = new ArrayList<>(1);

    MapFragment mMapFragment;
    LatLng limerickLocation = new LatLng(52.66136550517293, -8.624267575625026);
    ArrayList<PictureMarkerDataModel> markerDataCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        titles.add("Proposal Spot");
        descr.add("Oh mu Gawd, Soooo romantic!");
        lats.add("-54.37834945");
        longs.add("8.545454523");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mMapFragment);
        fragmentTransaction.commit();

        markerDataCollection = new ArrayList<>();
        markerDataCollection.add(
                new PictureMarkerDataModel(
                        R.drawable.rubbish,
                        titles.get(0),
                        descr.get(0),
                        new LatLng(Double.parseDouble(lats.get(0)), Double.parseDouble(longs.get(0)))
                )
        );
        markerDataCollection.add(
                new PictureMarkerDataModel(
                        R.drawable.graffiti,
                        "Graffiti",
                        "Some nice graffiti in an alley",
                        new LatLng(52.663864238301855, -8.619117734316433)
                )
        );

        mMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                for (PictureMarkerDataModel markerData : markerDataCollection) {
                    googleMap.addMarker(new MarkerOptions()
                            .position(markerData.getPosition())
                            .title(markerData.getTitle())
                            .snippet(markerData.getSnippet())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    );
                }
                googleMap.moveCamera(CameraUpdateFactory
                        .newLatLngZoom(limerickLocation, 7));

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Toast.makeText(getApplicationContext(), "Marker clicked: " + marker.getTitle(), Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });

                googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        for (PictureMarkerDataModel markerData : markerDataCollection) {
                            if (markerData.getTitle().equals(marker.getTitle())) {
                                // create info contents as View
                                View contentView = getLayoutInflater().inflate(R.layout.activity_info_window_contents, null);
                                //View.inflate(getApplicationContext(), R.layout.info_window_contents, null);
                                // Set image
                                ImageView contentImageView = (ImageView) contentView.findViewById(R.id.info_window_image);
                                contentImageView.setImageResource(markerData.getImageResId());
                                // Set title
                                TextView contentTitleTextView = (TextView) contentView.findViewById(R.id.info_window_title);
                                contentTitleTextView.setText(markerData.getTitle());
                                // Set snippet
                                TextView contentSnippetTextView = (TextView) contentView.findViewById(R.id.info_window_snippet);
                                contentSnippetTextView.setText(markerData.getSnippet());
                                // return newly created View
                                return contentView;
                            }
                        }
                        return null;
                    }
                });
            }
        });

        Button goButton = (Button) findViewById(R.id.go_button);
        if (goButton != null) {
            goButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            googleMap.animateCamera(CameraUpdateFactory
                                    .newLatLngZoom(limerickLocation, 14));
                        }
                    });
                }
            });
        }

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
        File file = new File(String.format("%s",getString(R.string.lat)));
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
        File file = new File(String.format("%s"getString(R.string.lon)));
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

        File file = new File(String.format("%s"getString(R.string.title)));
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

        File file = new File(String.format("%s"), getString(R.string.desc)));
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
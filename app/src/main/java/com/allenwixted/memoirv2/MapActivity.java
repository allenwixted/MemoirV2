package com.allenwixted.memoirv2;

//API KEY: AIzaSyBihlHuDZ-WJL3LKjKMOmFlFWfxtl0UztE

//stu

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import java.util.Random;


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


public class MapActivity extends AddMemoryActivity {

    MapFragment mMapFragment;
    // Gets the users current location to view this area when map is opened. NOTE: on emulator it will give you 0,0 by default
    LatLng myCurrentLoc = new LatLng(AddMemoryActivity.latitude, AddMemoryActivity.longitude);
    // Creates a location for limerick, pressing button brings you there
    LatLng limerickLocation = new LatLng(52.66136550517293, -8.624267575625026);
    ArrayList<PictureMarkerDataModel> markerDataCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Random rand = new Random();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
//        readTitle();
//        readDescription();
//        readLatitude();
//        readLongitude();

        mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mMapFragment);
        fragmentTransaction.commit();

        markerDataCollection = new ArrayList<>();

        for(int i = 0; i < titles.size();i++){
        markerDataCollection.add(
                new PictureMarkerDataModel(
                        R.drawable.rubbish,
                        titles.get(i),
                        descriptions.get(i),
                        new LatLng(Double.parseDouble(latitudes.get(i)) + rand.nextInt(5), Double.parseDouble(longitudes.get(i) +rand.nextInt(5)))
                )
        );}

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
                        .newLatLngZoom(myCurrentLoc, 7));

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
            goButton.setBackgroundColor(Color.parseColor("#A8C7D9"));
            goButton.invalidate();
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


    }
    @Override
    protected void onPause()
    {
        super.onPause();
        mMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.clear();
            }
        });

    }

    @Override
    protected void onResume() {
        //GoogleMap googleMap = null;
        //googleMap.clear();

        markerDataCollection.clear();
        Random rand = new Random();
        super.onResume();

        for(int i = 0; i < titles.size();i++){
            markerDataCollection.add(
                    new PictureMarkerDataModel(R.drawable.rubbish,
                           titles.get(i),
                            descriptions.get(i),
                            // Replace the randoms with "+1" after debugging is complete
                            new LatLng(Double.parseDouble(latitudes.get(i)) + rand.nextInt(5), Double.parseDouble(longitudes.get(i) +rand.nextInt(5)))

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
                            .newLatLngZoom(myCurrentLoc, 7));

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
            // Debugging
            Log.i("map","TESTER "+ i);}

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

        if (id == R.id.action_help) {
            Intent i = new Intent(getApplicationContext(), helpActivity.class);
            startActivity(i);
            return true;

        } else {

        }

        return super.onOptionsItemSelected(item);
    }



//    public void readLatitude() {
//
//        final Context context = getApplicationContext();
//
//            FileInputStream fis = null;
//            try {
//
//                fis = openFileInput(String.format("%s.txt",getString(R.string.lat)));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
//                while (fis.available() > 0) {
//                    lats.add(reader.readLine());
//                    Log.i("ReadLatitude", "LATITUDE");
//                }
//            } catch (Exception e) {
//                Log.d("MemoryRead", e.toString());
//            } finally {
//                if (fis != null)
//                    try {
//                        fis.close();
//                    } catch (IOException ignored) {
//                    }
//            }
//        }
//
//
//
//    public void readLongitude() {
//
//        final Context context = getApplicationContext();
//
//            FileInputStream fis = null;
//            try {
//                fis = openFileInput(String.format("%s.txt",getString(R.string.lon)));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
//                while (fis.available() > 0) {
//
//                    longs.add(reader.readLine());
//                    Log.i("ReadLongitude", "LONGITUDE");
//                }
//            } catch (Exception e) {
//                Log.d("MemoryRead", e.toString());
//            } finally {
//                if (fis != null)
//                    try {
//                        fis.close();
//                    } catch (IOException ignored) {
//                    }
//            }
//
//    }
//
//    public void readTitle() {
//
//        final Context context = getApplicationContext();
//
//            FileInputStream fis = null;
//            try {
//                fis = openFileInput(String.format("%s.txt",getString(R.string.title)));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
//                while (fis.available() > 0) {
//
//                    titles.add(reader.readLine());
//                    Log.i("ReadTitles", "TITLES");
//                }
//            } catch (Exception e) {
//                Log.d("MemoryRead", e.toString());
//            } finally {
//                if (fis != null)
//                    try {
//                        fis.close();
//                    } catch (IOException ignored) {
//                    }
//            }
//        }



//    public void readDescription() {
//
//        final Context context = getApplicationContext();
//
//            FileInputStream fis = null;
//            try {
//                fis = openFileInput(String.format("%s.txt",getString(R.string.desc)));
//                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
//                while (fis.available() > 0) {
//
//                    descr.add(reader.readLine());
//                    Log.i("ReadDescription", "DESCRIPTION");
//                }
//            } catch (Exception e) {
//                Log.d("MemoryRead", e.toString());
//            } finally {
//                if (fis != null)
//                    try {
//                        fis.close();
//                    } catch (IOException ignored) {
//                    }
//            }
//        }

    }

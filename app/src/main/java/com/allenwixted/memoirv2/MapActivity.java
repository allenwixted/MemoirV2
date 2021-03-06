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

        mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mMapFragment);
        fragmentTransaction.commit();

        markerDataCollection = new ArrayList<>();

        if (!titles.isEmpty()) {

            for (int i = 0; i < titles.size(); i++) {
                markerDataCollection.add(
                        new PictureMarkerDataModel(
                                R.id.map,
                                titles.get(i),
                                descriptions.get(i),
                                new LatLng(Double.parseDouble(latitudes.get(i)) + rand.nextInt(5), Double.parseDouble(longitudes.get(i) + rand.nextInt(5)))
                        )
                );
            }

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


        } else {
            Context context = getApplicationContext();

            CharSequence text = "No Memories Logged!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            mMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    googleMap.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(myCurrentLoc, 7));
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
                                        .newLatLngZoom(myCurrentLoc, 14));
                            }
                        });
                    }
                });
            }
        }
    }

    @Override
    protected void onPause() {
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

        markerDataCollection.clear();
        Random rand = new Random();
        super.onResume();

        if (!titles.isEmpty()) {
            for (int i = 0; i < titles.size(); i++) {
                markerDataCollection.add(
                        new PictureMarkerDataModel(R.drawable.rubbish,
                                titles.get(i),
                                descriptions.get(i),
                                // Replace the randoms with "+1" after debugging is complete
                                new LatLng(Double.parseDouble(latitudes.get(i)) + rand.nextInt(5), Double.parseDouble(longitudes.get(i) + rand.nextInt(5)))

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
                                int foundImage = 0;
                                for (PictureMarkerDataModel markerData : markerDataCollection) {
                                    if (markerData.getTitle().equals(marker.getTitle())) {

                                        for (int i = 0; i < titles.size(); i++) {
                                            if (!imageNames.isEmpty()) {
                                                if (imageNames.get(i) == marker.getTitle()) {
                                                    foundImage = i;
                                                }
                                            }
                                        }


                                        // create info contents as View
                                        View contentView = getLayoutInflater().inflate(R.layout.activity_info_window_contents, null);
                                        // Set image
                                        ImageView contentImageView = (ImageView) contentView.findViewById(R.id.info_window_image);
                                        contentImageView.setImageBitmap(imageArray.get(foundImage));
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
            }

        } else {
            Context context = getApplicationContext();

            CharSequence text = "No Memories Logged!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
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

}

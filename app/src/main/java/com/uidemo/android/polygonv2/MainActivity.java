package com.uidemo.android.polygonv2;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    // LatLng Is Already Defined Class Representing A Pair Of Latitude And Longitude Coordinates
    // List Of LatLng To Add The Points (latitude - longtide) Of The Polygon
    List<LatLng> pointsList  = new ArrayList<>();
    Button checkLocation;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private final int locationRequestCode = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toast.makeText(this, "In", Toast.LENGTH_LONG);



        // Add The Points To The List
        // It Should Be A Query From The DB
        pointsList.add(new LatLng(46.57516,24.75057));
        pointsList.add(new LatLng(46.74545,24.81291));
        pointsList.add( new LatLng(46.85119,24.65076));
        pointsList.add(new LatLng(46.67404,24.57335));





        //LocationManager Class Provides Access To The System Location Services
        //These Services Allow Applications To Obtain Periodic Updates Of The Device's Geographical Location
        // In The Constructor We Specify The Service We Want To Access
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);




        if (Build.VERSION.SDK_INT >= 23)
            checkUserPermission();

        else getUserLocation();







   /*     checkLocation = (Button) findViewById(R.id.checkLocationBtn);
        checkLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get The User Location - (This Will Be Done Through LocationManager To Get His/Her Location Whenever Its Changed
                // The Values Of Lat And Lng Will Be Returned From Getlong & Getlat Methods
                // userLocation =  new LatLng (latitude , longtiude);


               LatLng userLocation = new LatLng(46.57276,24.66355);
                if (isUserWhithinBoundray(userLocation))
                    Toast.makeText(getApplicationContext(),"You Are Within The Boundray", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(),"You Are Out Of The Boundray", Toast.LENGTH_LONG).show();
              getUserLocation();
            }
        }); */


    }

    private void checkUserPermission() {


        if (Build.VERSION.SDK_INT >= 23) {

            //Check If Location Permission Already Enabled
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                getUserLocation();


                //Camers Persmisson Is Not Allowed
            else {


                // Show Message To The User Why We Need This Permission
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION))
                    Toast.makeText(this, "Need Location Permission To Give Access To The App", Toast.LENGTH_LONG);

                //Request Permission
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, locationRequestCode);

            }


        }
    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == locationRequestCode) {


            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                getUserLocation();

            else
                Toast.makeText(this, "The Permission Is Not Enabled We Shall Close The App", Toast.LENGTH_LONG);

        }


    }







    private void getUserLocation () {


        try {



            locationManager.requestLocationUpdates("gps", 1000, 0, new LocationListener() {

                @Override
                public void onLocationChanged(Location location) {




                    LatLng userLocation = new LatLng( location.getLatitude(),location.getLongitude());

                    if (isUserWhithinBoundray(userLocation))
                        Toast.makeText(getApplicationContext(),"You Are Within The Boundray", Toast.LENGTH_LONG).show();

                    else
                        Toast.makeText(getApplicationContext(),"You Are Out Of The Boundray", Toast.LENGTH_LONG).show();


                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                    //Called When The GPS Is Disabled By The User.


                }
            }
        );

     }

     catch (SecurityException e) { }

    }

    public Boolean isUserWhithinBoundray (LatLng userLocation) {

        return PolyUtil.containsLocation(userLocation,pointsList,false);
    }
}

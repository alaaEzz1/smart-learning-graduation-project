package com.elmohandes.smart_learnning.screens;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.elmohandes.smart_learnning.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;


public class bus_tracker_screen extends Fragment implements LocationListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: map variables
    SupportMapFragment mapFragment;
    FusedLocationProviderClient client;
    Location currentLocation;
    final static int locationRequest = 44;
    LocationRequest request;
    GoogleMap map;
    LatLng dreamSchool = new LatLng(29.955052486867476, 30.972626453912074);
    Marker busTrackerMarker , currentLocationMarker;

    DatabaseReference reference;
    LocationManager manager;
    private final int MIN_TIME = 1000;  // 1 sec
    private final int MIN_DISTANCE = 1; //1 m

    public bus_tracker_screen() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static bus_tracker_screen newInstance(String param1, String param2) {
        bus_tracker_screen fragment = new bus_tracker_screen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bus_tracker_screen, container,
                false);

        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission
        .ACCESS_COARSE_LOCATION} , locationRequest);

        client = LocationServices.getFusedLocationProviderClient
                (getContext());
        //get last location or current student location
        getCurrentLocation();
        //readChanges();
        getLocationUpdate();

        return v;
    }



            @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            getCurrentLocation();

        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, locationRequest);
        }
    }

    private void getCurrentLocation() {

        //set Location permission
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, locationRequest);
            return;
        }
        //get Last or current Location
        reference= FirebaseDatabase.getInstance().getReference().
                child("user Location");
        Task<Location> task = client.getLastLocation();
        //Add current Location in map and Add it in Amarket
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    Toast.makeText(getContext(), "Latitude = " + currentLocation.getLatitude()
                                    + " and Longitude = " + currentLocation.getLongitude(),
                            Toast.LENGTH_SHORT).show();
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            map=googleMap;
                            //get Latitude and Longitude
                            LatLng cuurentLatLng = new LatLng(currentLocation.getLatitude(),
                                    currentLocation.getLongitude());
                            // Add latitude and longitue to a marker
                            currentLocationMarker= map.addMarker(new MarkerOptions()
                            .title("I am here").position(cuurentLatLng)
                            .icon(bitmapDescriptorFromVector(getContext(),
                                    R.drawable.ic_baseline_person_24)));
                            busTrackerMarker= map.addMarker(new MarkerOptions()
                            .position(dreamSchool).title("tracking bus")
                            .icon(bitmapDescriptorFromVector(getContext(),
                                    R.drawable.ic_baseline_local_car_wash_24)));

                            //Zoom Location in map
                            map.moveCamera(CameraUpdateFactory.newLatLng(cuurentLatLng));
                            map.moveCamera(CameraUpdateFactory.
                                    newLatLngZoom(cuurentLatLng, 11));

                            //add paths between locations (draw al line between locations)
                            Polyline polyline= googleMap.addPolyline(new PolylineOptions()
                                    .clickable(true)
                                    .add(cuurentLatLng , dreamSchool).color(Color.BLUE));

                        }
                    });
                } else {
                    Toast.makeText(getContext(), "No Location",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getLocationUpdate() {
        if (manager != null) {
            // GPS_PROVIDER gives accurate Location but less frequently location update

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.
                    ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.
                    ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    manager.requestLocationUpdates(LocationManager.GPS_PROVIDER
                            , MIN_TIME, MIN_DISTANCE, this);
                    //NETWORK_PROVIDER give less accurate location but frequently location updates
                }else if(manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                    manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER
                            ,MIN_TIME,MIN_DISTANCE,this);
                }else {
                    Toast.makeText(getContext(), "No Provider Enabled",
                            Toast.LENGTH_SHORT).show();
                }
            }else {
                ActivityCompat.requestPermissions(getActivity(),new String[]
                        {Manifest.permission.ACCESS_FINE_LOCATION},locationRequest);
            }
        }
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context , int vectorResource){

        Drawable vectorDrawable = ContextCompat.getDrawable(context,vectorResource);
        vectorDrawable.setBounds(0,0,vectorDrawable.getIntrinsicWidth()
                ,vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight() ,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);

    }


    private void saveLocation(Location location) {
        reference.setValue(location);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull @NotNull String[] permissions,
                                           @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==locationRequest){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED){
                getCurrentLocation();
            }
        }

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (location != null){
            saveLocation(location);
        }else{
            Toast.makeText(getContext(), "Null Location", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}
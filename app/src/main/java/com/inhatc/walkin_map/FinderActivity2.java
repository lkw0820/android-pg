package com.inhatc.walkin_map;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inhatc.walkin_map.databinding.ActivityFinder2Binding;

import java.util.ArrayList;
import java.util.List;

public class FinderActivity2 extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private ActivityFinder2Binding binding;
    Button btnStart = null;
    Button btnStop = null;
    Button btnBack = null;
    LocationManager manager = null;
    private Location location;
    LatLng currentPosition;//시작 위치값
    LatLng lastPosition;//종료 위치값
    DO data = null;
    List<Polyline> polylines = new ArrayList<Polyline>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFinder2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //권한 요청
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("userLocation");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                data = dataSnapshot.getValue(DO.class);
                LatLng point = new LatLng(Double.parseDouble(data.getLatitude()), Double.parseDouble(data.getLongitude()));
                mMap.addMarker(new MarkerOptions().position(point).title("내위치"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(point));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(15.5f));
                //현재 위치값
                Toast.makeText(getApplicationContext(),"위도 : "+ data.getLatitude()+" 경도 : "+data.getLongitude(),Toast.LENGTH_LONG);

                //Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        btnStart = (Button) findViewById(R.id.btnStart2);
        btnStop = (Button) findViewById(R.id.btnStop2);
        btnBack = (Button) findViewById(R.id.btnBack);
        LatLng start = null;

        btnStart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                //위치값 실시간으로 얻어오기 권한요청 LocationManager LocationListener requestLocationUpdates
                manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                //location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, FinderActivity2.this);
                //현재 위치값
                currentPosition = new LatLng(Double.parseDouble(data.getLatitude()), Double.parseDouble(data.getLongitude()));

            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //위치값 받아오기 종료
                //파이어베이스에 저장하지 않음
                lastPosition = new LatLng(Double.parseDouble(data.getLatitude()), Double.parseDouble(data.getLongitude()));
                manager.removeUpdates(FinderActivity2.this);
                manager = null;
                //polyline 그리기
                PolylineOptions options = new PolylineOptions().add(currentPosition).add(lastPosition).width(15).color(Color.BLACK).geodesic(true);
                polylines.add(mMap.addPolyline(options));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition,18));
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //위치값 받아오기 종료
                //파이어베이스에 저장하지 않음
                //나가기
                manager.removeUpdates(FinderActivity2.this);
                manager = null;
                finish();
            }
        });
        //LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, this);

    }

    //위치정보가 변경되었을때 실행
    @Override
    public void onLocationChanged(@NonNull Location location) {
        //파이어베이스에 저장
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("userLocation");

        double lat = location.getLatitude();
        double lon = location.getLongitude();

        myRef.child("latitude").setValue(""+lat);
        myRef.child("longitude").setValue(""+lon);

    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
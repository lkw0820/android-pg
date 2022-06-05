package com.inhatc.walkin_map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class UserActivity extends AppCompatActivity implements LocationListener {

    Button btnStart = null;
    Button btnStop = null;
    LocationManager manager = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);

        //권한 요청
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},0);


        btnStart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                //위치값 실시간으로 얻어오기 권한요청 LocationManager LocationListener requestLocationUpdates
                manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, UserActivity.this);
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //위치값 받아오기 종룍
                //파이어베이스에 저장하지 않음
                manager.removeUpdates(UserActivity.this);
                manager = null;

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
}
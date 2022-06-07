package com.inhatc.walkin_map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RealMainActivity extends AppCompatActivity implements LocationListener {

    Button btnFinder = null;
    Button btnUser = null;
    LocationManager manager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_main);

        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, RealMainActivity.this);

        btnFinder = (Button)findViewById(R.id.btnFinder);
        btnUser = (Button)findViewById(R.id.btnUser);

        btnFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finderActivity 실행
                Intent i = new Intent(getApplicationContext(),FinderActivity2.class);
                startActivity(i);
                //finish(); //activity 실행후 이전 activity를 종료함
            }
        });

        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //userActivity 실행
                Intent i = new Intent(getApplicationContext(),UserActivity.class);
                startActivity(i);
                //finish();
            }
        });
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("userLocation");

        double lat = location.getLatitude();
        double lon = location.getLongitude();

        myRef.child("latitude").setValue(""+lat);
        myRef.child("longitude").setValue(""+lon);
    }
}
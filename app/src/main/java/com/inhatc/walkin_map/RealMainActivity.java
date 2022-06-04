package com.inhatc.walkin_map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RealMainActivity extends AppCompatActivity {

    Button btnFinder =null;
    Button btnUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_main);

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},0);


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
}
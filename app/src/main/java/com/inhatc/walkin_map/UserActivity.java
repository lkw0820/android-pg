package com.inhatc.walkin_map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserActivity extends AppCompatActivity {

    Button btnStop = null;
    String name = null;
    String email = null;
    String total = null;
    private TextView txtName;
    private TextView txtEmail;
    private TextView txtTotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        btnStop = (Button) findViewById(R.id.btnStop);

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                DO data = dataSnapshot.getValue(DO.class);
                //파이어베이스에서 이름 email 운동량 가져와야함
                total = data.getDistance();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
        txtName.setText(name);
        txtEmail.setText(email);
        txtName.setText(total);


        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),FinderActivity2.class);
                startActivity(i);
                //finish();
            }
        });

    }
}
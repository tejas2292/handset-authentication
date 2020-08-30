package com.example.demo1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivityAdmin extends AppCompatActivity {
    TextView mCountVendors, mCountDealers, mCountPersonal, mCountSerials;
    CardView mCardCheckUser, mCardSendPoints;
    Button mBtnSearchSerialNo;
    String countVendor, countDealer, countPersonal, countSerial;
    DatabaseReference referenceVendor, referenceDealer, referencePersonal, referenceSerials;
    int maxIDVendor = 0, maxIDDealer = 0, maxIDPersonal = 0, maxIDSerial = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home Screen Admin");
        setSupportActionBar(toolbar);

        mCountVendors = findViewById(R.id.tv_totalvendors);
        mCountDealers = findViewById(R.id.tv_totaldealers);
        mCountPersonal = findViewById(R.id.tv_totalpersonal);
        mCountSerials = findViewById(R.id.tv_totalserials);
        mCardCheckUser = findViewById(R.id.cardCheckUser);
        mCardSendPoints = findViewById(R.id.cardSendPoints);
        mBtnSearchSerialNo = findViewById(R.id.btnSearchSerialNoDealer);

        referenceVendor = FirebaseDatabase.getInstance().getReference("credentials").child("vendor");
        referenceDealer = FirebaseDatabase.getInstance().getReference("credentials").child("dealer");
        referencePersonal = FirebaseDatabase.getInstance().getReference("credentials").child("personal");
        referenceSerials = FirebaseDatabase.getInstance().getReference("serials");

        ////////////////////////////////////////////////////////////////////////////////////////////
        referenceVendor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    maxIDVendor = (int) dataSnapshot.getChildrenCount();
                    countVendor = String.valueOf(maxIDVendor);
                    mCountVendors.setText(countVendor);
                } else {
                    countVendor = String.valueOf(maxIDVendor);
                    mCountVendors.setText(countVendor);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivityAdmin.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
        referencePersonal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    maxIDPersonal = (int) dataSnapshot.getChildrenCount();
                    countPersonal = String.valueOf(maxIDPersonal);
                    mCountPersonal.setText(countPersonal);
                } else {
                    countPersonal = String.valueOf(maxIDPersonal);
                    mCountPersonal.setText(countPersonal);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivityAdmin.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
        referenceDealer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    maxIDDealer = (int) dataSnapshot.getChildrenCount();
                    countDealer = String.valueOf(maxIDDealer);
                    mCountDealers.setText(countDealer);
                } else {
                    countDealer = String.valueOf(maxIDDealer);
                    mCountDealers.setText(countDealer);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivityAdmin.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
        referenceSerials.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    maxIDSerial = (int) dataSnapshot.getChildrenCount();
                    countSerial = String.valueOf(maxIDSerial);
                    mCountSerials.setText(countSerial);
                } else {
                    countSerial = String.valueOf(maxIDSerial);
                    mCountSerials.setText(countSerial);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivityAdmin.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
        mCardSendPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCountVendors.getText().toString().equals("")) {
                    Toast.makeText(MainActivityAdmin.this, "Wait data is still loading..", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(MainActivityAdmin.this, AdminSendPointsOptions.class));
                    finish();
                }
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
        mCardCheckUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCountVendors.getText().toString().equals("")) {
                    Toast.makeText(MainActivityAdmin.this, "Wait data is still loading..", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(MainActivityAdmin.this, AdminCheckUser.class));
                }
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
        mBtnSearchSerialNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCountVendors.getText().toString().equals("")) {
                    Toast.makeText(MainActivityAdmin.this, "Wait data is still loading..", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(MainActivityAdmin.this, SearchSerialNoAdmin.class));
                }
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivityAdmin.this);
        builder.setMessage("Are you sure want to exit from app?");
        builder.setCancelable(false);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        //here exit app alert close............................................
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:

                Toast.makeText(this, "Settings..", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.logout:
                new UserCurrent(MainActivityAdmin.this).removeUser();
                Intent intent = new Intent(MainActivityAdmin.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

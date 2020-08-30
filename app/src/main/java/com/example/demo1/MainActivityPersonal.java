package com.example.demo1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

public class MainActivityPersonal extends AppCompatActivity {
    TextView mPersonalId, mPersonalName;
    String UserName, UNo, firstName, lastName;
    Button mBtnSearchSerialNo;
    public DatabaseReference referencePersonal, referenceMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_personal);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home Screen Personal");
        setSupportActionBar(toolbar);

        mPersonalId = findViewById(R.id.tv_personal_id);
        mPersonalName = findViewById(R.id.tv_personal_name);
        mBtnSearchSerialNo = findViewById(R.id.btnSearchSerialNoPersonal);

        UserName = new UserCurrent(MainActivityPersonal.this).getUsername().trim();

        referencePersonal = FirebaseDatabase.getInstance().getReference("data").child(UserName).child("3");
        referencePersonal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UNo = dataSnapshot.child("userNo").getValue().toString();
                //here we got the vendor id from data.. now we check it into credentials
                referenceMain = FirebaseDatabase.getInstance().getReference("credentials").child("personal").child(UNo);
                referenceMain.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        firstName = dataSnapshot.child("ownerFirstName").getValue().toString();
                        lastName = dataSnapshot.child("ownerLastName").getValue().toString();
                        mPersonalId.setText(UNo);
                        mPersonalName.setText(firstName+" "+lastName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(MainActivityPersonal.this, "Error: "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivityPersonal.this, "Error: "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

        mBtnSearchSerialNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPersonalId.getText().toString().equals("")) {
                    Toast.makeText(MainActivityPersonal.this, "Wait data is still loading..", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(MainActivityPersonal.this, SearchSerialNoPersonal.class));
                    finish();
                }
            }
        });

    }

    //here exit app start..........................................
    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivityPersonal.this);
        builder.setMessage("Are you sure want to exit from app?");
        builder.setCancelable(true);
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
                new UserCurrent(MainActivityPersonal.this).removeUser();
                Intent intent = new Intent(MainActivityPersonal.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

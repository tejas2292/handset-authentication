package com.example.demo1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdminCheckUserVendor extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView mText, mText2;
    EditText mSearchView;
    String userNo, active;
    int maxRegistered = 0;
    ArrayList<DataSetFireVendorRegisteredSerial> arrayListVendorRegisteredSerial;
    FirebaseRecyclerOptions<DataSetFireVendorRegisteredSerial> optionsVendorRegisteredSerial;
    FirebaseRecyclerAdapter<DataSetFireVendorRegisteredSerial, FirebaseViewHolderVendorRegisteredSerial> adapterVendorRegisteredSerial;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_check_user_vendor);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Vendor Registered Serials");
        setSupportActionBar(toolbar);

        userNo = getIntent().getStringExtra("userNo");
        active = getIntent().getStringExtra("active");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("HistoryVendor").child(userNo).child("serial");

        recyclerView = findViewById(R.id.recyclerView);
        mSearchView = findViewById(R.id.searchView);
        mText2 = findViewById(R.id.tv_active);
        mText = findViewById(R.id.tv_totalRegisteredSerial);
        recyclerView.setLayoutManager(new LinearLayoutManager(AdminCheckUserVendor.this));
        recyclerView.setHasFixedSize(true);
        arrayListVendorRegisteredSerial = new ArrayList<DataSetFireVendorRegisteredSerial>();

        if(active.equals("deactivate")){
            mText2.setText("Account of this vendor is deleted");
        }

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    maxRegistered = (int) dataSnapshot.getChildrenCount();
                    String count = String.valueOf(maxRegistered);
                    mText.setText("Total no of serials registered by this vendor are "+count);
                }
                else {
                    String count = String.valueOf(maxRegistered);
                    mText.setText("Total no of serials registered by this vendor are "+count);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        LoadData("");

        mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString() != null)
                {
                    LoadData(s.toString());
                }
                else {
                    LoadData("");
                }
            }
        });
    }

    private void LoadData(String data) {
        Query query = databaseReference.orderByChild("serialNo").startAt(data).endAt(data+"\uf8ff");
        optionsVendorRegisteredSerial = new FirebaseRecyclerOptions.Builder<DataSetFireVendorRegisteredSerial>().setQuery(query,DataSetFireVendorRegisteredSerial.class).build();

        adapterVendorRegisteredSerial = new FirebaseRecyclerAdapter<DataSetFireVendorRegisteredSerial, FirebaseViewHolderVendorRegisteredSerial>(optionsVendorRegisteredSerial) {
            @Override
            protected void onBindViewHolder(@NonNull FirebaseViewHolderVendorRegisteredSerial holder, int position, @NonNull DataSetFireVendorRegisteredSerial model) {
                holder.serialNo.setText(model.getSerialNo());
                //holder.date.setText(model.getDate());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(AdminCheckUserVendor.this, ""+model.getSerialNo(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminCheckUserVendor.this, AdminCheckUserVendor2.class);
                        intent.putExtra("serialNo", model.getSerialNo());
                        startActivity(intent);
                    }
                });
            }
            @NonNull
            @Override
            public FirebaseViewHolderVendorRegisteredSerial onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                return new FirebaseViewHolderVendorRegisteredSerial(LayoutInflater.from(AdminCheckUserVendor.this).inflate(R.layout.row4, viewGroup, false));
            }
        };
        adapterVendorRegisteredSerial.startListening();
        recyclerView.setAdapter(adapterVendorRegisteredSerial);

    }
}

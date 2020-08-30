package com.example.demo1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class HistoryVendorPoints1 extends AppCompatActivity {
    String vendorUserNo;
    DatabaseReference referenceHistoryVendor;

    EditText mSearchView;
    RecyclerView recyclerView;

    ArrayList<historyVendorUploadFinal> arrayListHistory;
    FirebaseRecyclerOptions<historyVendorUploadFinal> optionsHistory;
    FirebaseRecyclerAdapter<historyVendorUploadFinal, FirebaseViewHolderHistoryDealer> adapterHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_vendor_points1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("History");
        setSupportActionBar(toolbar);

        vendorUserNo = getIntent().getStringExtra("vendorUserNo");
        referenceHistoryVendor = FirebaseDatabase.getInstance().getReference("HistoryVendor").child(vendorUserNo).child("points");

        mSearchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(HistoryVendorPoints1.this));
        recyclerView.setHasFixedSize(true);

        arrayListHistory = new ArrayList<historyVendorUploadFinal>();

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
                if (s.toString() != null) {
                    LoadData(s.toString());
                } else {
                    LoadData("");
                }
            }
        });

    }

    private void LoadData(String data) {
        Query query = referenceHistoryVendor.orderByChild("dealerName").startAt(data).endAt(data + "\uf8ff");
        optionsHistory = new FirebaseRecyclerOptions.Builder<historyVendorUploadFinal>().setQuery(query, historyVendorUploadFinal.class).build();

        adapterHistory = new FirebaseRecyclerAdapter<historyVendorUploadFinal, FirebaseViewHolderHistoryDealer>(optionsHistory) {
            @Override
            protected void onBindViewHolder(@NonNull FirebaseViewHolderHistoryDealer holder, int position, @NonNull historyVendorUploadFinal model) {

                holder.date.setText(model.getDate());

                String status = model.getStatus();
                if(status.equals("ReceivedByAdmin"))
                {
                    String color = "#84E488";
                    holder.points.setTextColor(Color.parseColor(color));
                    holder.points.setText(model.getReceivedPoints());

                    holder.status.setText("Points received by:");
                    holder.nameandid.setText(model.getDealerName()+"\nTransaction ID: "+model.getTransactionId());
                }
                else if(status.equals("DeductedByAdmin"))
                {
                    String color = "#C86E6E";
                    holder.points.setTextColor(Color.parseColor(color));
                    holder.points.setText(model.getDeductedPoints());

                    holder.status.setText("Points deducted by:");
                    holder.nameandid.setText(model.getDealerName()+"\nTransaction ID: "+model.getTransactionId());
                }
                else if(status.equals("DeductedByOwn"))
                {
                    String color = "#C86E6E";
                    holder.points.setTextColor(Color.parseColor(color));
                    holder.points.setText(model.getDeductedPoints());

                    holder.status.setText("Registered new serial no:");
                    holder.nameandid.setText("Serial No: "+ model.getDealerName()+"\nInvoice: "+model.getInvoice()+
                            "\nTransaction ID: "+model.getTransactionId());
                }
                else if(status.equals("ReceivedByDealer"))
                {
                    String color = "#84E488";
                    holder.points.setTextColor(Color.parseColor(color));
                    holder.points.setText(model.getReceivedPoints());

                    holder.status.setText("Points received by dealer:");
                    holder.nameandid.setText("Name: "+model.getDealerName()+"\nLogin ID: "+model.getDealerId()+
                            "\nTransaction ID: "+model.getTransactionId());
                }


            }

            @NonNull
            @Override
            public FirebaseViewHolderHistoryDealer onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                return new FirebaseViewHolderHistoryDealer(LayoutInflater.from(HistoryVendorPoints1.this).inflate(R.layout.row_history_dealer, viewGroup, false));
            }
        };
        adapterHistory.startListening();
        recyclerView.setAdapter(adapterHistory);

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(HistoryVendorPoints1.this, HistoryVendorOptions.class);
        i.putExtra("uno",vendorUserNo);
        startActivity(i);
        finish();
    }
}

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
import android.view.ViewGroup;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class HistoryDealerPoints1 extends AppCompatActivity {
    String dealerUserNo;
    DatabaseReference referenceHistoryDealer;
    EditText mSearchView;
    RecyclerView recyclerView;

    ArrayList<historyDealerUploadFinal> arrayListHistory;
    FirebaseRecyclerOptions<historyDealerUploadFinal> optionsHistory;
    FirebaseRecyclerAdapter<historyDealerUploadFinal, FirebaseViewHolderHistoryDealer> adapterHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_dealer_points1);

        dealerUserNo = getIntent().getStringExtra("dealerUserNo");
        referenceHistoryDealer = FirebaseDatabase.getInstance().getReference("HistoryDealer").child(dealerUserNo);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("History");
        setSupportActionBar(toolbar);

        mSearchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(HistoryDealerPoints1.this));
        recyclerView.setHasFixedSize(true);

        arrayListHistory = new ArrayList<historyDealerUploadFinal>();

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
        Query query = referenceHistoryDealer.orderByChild("vendorName").startAt(data).endAt(data + "\uf8ff");
        optionsHistory = new FirebaseRecyclerOptions.Builder<historyDealerUploadFinal>().setQuery(query, historyDealerUploadFinal.class).build();

        adapterHistory = new FirebaseRecyclerAdapter<historyDealerUploadFinal, FirebaseViewHolderHistoryDealer>(optionsHistory) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull FirebaseViewHolderHistoryDealer holder, int position, @NonNull historyDealerUploadFinal model) {

                holder.date.setText(model.getDate());

                String status = model.getStatus();
                if(status.equals("ReceivedByAdmin"))
                {
                    String color = "#84E488";
                    holder.points.setTextColor(Color.parseColor(color));
                    holder.points.setText(model.getReceivedPoints());

                    holder.status.setText("Points received by:");
                    holder.nameandid.setText(model.getVendorName()+"\nTransaction ID: "+model.getTransactionId());
                }
                else if(status.equals("DeductedByAdmin"))
                {
                    String color = "#C86E6E";
                    holder.points.setTextColor(Color.parseColor(color));
                    holder.points.setText(model.getDeductedPoints());

                    holder.status.setText("Points deducted by:");
                    holder.nameandid.setText(model.getVendorName()+"\nTransaction ID: "+model.getTransactionId());
                }
                else if(status.equals("DeductedByOwn"))
                {
                    String color = "#C86E6E";
                    holder.points.setTextColor(Color.parseColor(color));
                    holder.points.setText(model.getDeductedPoints());

                    holder.status.setText("Points sent to vendor:");
                    holder.nameandid.setText("Name: "+ model.getVendorName()+"\nLogin ID: "+model.getVendorId()
                    +"\nShop Name: "+model.getVendorShopName()+"\nTransaction ID: "+model.getTransactionId());
                }


            }

            @NonNull
            @Override
            public FirebaseViewHolderHistoryDealer onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                return new FirebaseViewHolderHistoryDealer(LayoutInflater.from(HistoryDealerPoints1.this).inflate(R.layout.row_history_dealer, viewGroup, false));
            }
        };
        adapterHistory.startListening();
        recyclerView.setAdapter(adapterHistory);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(HistoryDealerPoints1.this, MainActivityDealer.class));
        finish();
    }
}

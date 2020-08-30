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

public class AdminHistory1 extends AppCompatActivity {
    DatabaseReference referenceHistoryAdmin;

    EditText mSearchView;
    RecyclerView recyclerView;

    ArrayList<historyAdminUploadFinal> arrayListHistory;
    FirebaseRecyclerOptions<historyAdminUploadFinal> optionsHistory;
    FirebaseRecyclerAdapter<historyAdminUploadFinal, FirebaseViewHolderHistoryDealer> adapterHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_history1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("History");
        setSupportActionBar(toolbar);

        referenceHistoryAdmin = FirebaseDatabase.getInstance().getReference("HistoryAdmin");

        mSearchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(AdminHistory1.this));
        recyclerView.setHasFixedSize(true);

        arrayListHistory = new ArrayList<historyAdminUploadFinal>();

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
        Query query = referenceHistoryAdmin.orderByChild("name").startAt(data).endAt(data + "\uf8ff");
        optionsHistory = new FirebaseRecyclerOptions.Builder<historyAdminUploadFinal>().setQuery(query, historyAdminUploadFinal.class).build();

        adapterHistory = new FirebaseRecyclerAdapter<historyAdminUploadFinal, FirebaseViewHolderHistoryDealer>(optionsHistory) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull FirebaseViewHolderHistoryDealer holder, int position, @NonNull historyAdminUploadFinal model) {

                holder.date.setText(model.getDate());

                String status = model.getStatus();
                if (status.equals("SentToVendor")) {
                    String color = "#84E488";
                    holder.points.setTextColor(Color.parseColor(color));
                    holder.points.setText(model.getSentPoints());

                    holder.status.setText("Points sent to vendor:");
                    holder.nameandid.setText("Name: " + model.getName() + "\nLogin ID: " + model.getLoginId()
                            + "\nShop Name: " + model.getShopName()+ "\nTransaction ID: "+model.getTransactionId());
                } else if (status.equals("DeductFromVendor")) {
                    String color = "#C86E6E";
                    holder.points.setTextColor(Color.parseColor(color));
                    holder.points.setText(model.getDiductPoints());

                    holder.status.setText("Points deducted from vendor:");
                    holder.nameandid.setText("Name: " + model.getName() + "\nLogin ID: " + model.getLoginId()
                            + "\nShop Name: " + model.getShopName() + "\nTransaction ID: " + model.getTransactionId());
                } else if (status.equals("SentToDealer")) {
                    String color = "#84E488";
                    holder.points.setTextColor(Color.parseColor(color));
                    holder.points.setText(model.getSentPoints());

                    holder.status.setText("Points sent to dealer:");
                    holder.nameandid.setText("Name: " + model.getName() + "\nLogin ID: " + model.getLoginId() +
                            "\nTransaction ID: " + model.getTransactionId());
                } else if (status.equals("DeductFromDealer")) {
                    String color = "#C86E6E";
                    holder.points.setTextColor(Color.parseColor(color));
                    holder.points.setText(model.getDiductPoints());

                    holder.status.setText("Points deducted from dealer:");
                    holder.nameandid.setText("Name: " + model.getName() + "\nLogin ID: " + model.getLoginId() +
                            "\nTransaction ID: " + model.getTransactionId());
                }


            }

            @NonNull
            @Override
            public FirebaseViewHolderHistoryDealer onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                return new FirebaseViewHolderHistoryDealer(LayoutInflater.from(AdminHistory1.this).inflate(R.layout.row_history_dealer, viewGroup, false));
            }
        };
        adapterHistory.startListening();
        recyclerView.setAdapter(adapterHistory);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AdminHistory1.this, AdminSendPointsOptions.class));
        finish();
    }
}

package com.example.demo1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class HistoryVendorSerials extends AppCompatActivity {
    ListView mListView;
    SearchView mSearchView;
    String vendorUserNo;
    ArrayList<String> myArrayList = new ArrayList<>();
    DatabaseReference referenceHistoryVendor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_vendor_serials);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("History");
        setSupportActionBar(toolbar);

        vendorUserNo = getIntent().getStringExtra("vendorUserNo");

        mSearchView = findViewById(R.id.searchView);

        referenceHistoryVendor = FirebaseDatabase.getInstance().getReference("HistoryVendor");
        mListView = findViewById(R.id.list_view);

        //listview retriving starts here........................................................

        final ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(HistoryVendorSerials.this, android.R.layout.simple_list_item_1, myArrayList);
        mListView.setAdapter(myArrayAdapter);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myArrayAdapter.getFilter().filter(newText);
                return false;
            }
        });

        referenceHistoryVendor.child(vendorUserNo).child("serial").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();
                while (i.hasNext()){
                    set.add(((DataSnapshot) i.next()).getKey());
                }
                myArrayAdapter.clear();
                myArrayAdapter.addAll(set);
                myArrayAdapter.notifyDataSetChanged();
                mListView.setAdapter(myArrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //listView data retriving ends here.....................................................

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(HistoryVendorSerials.this, HistoryVendorOptions.class);
        i.putExtra("uno",vendorUserNo);
        startActivity(i);
        finish();
    }
}

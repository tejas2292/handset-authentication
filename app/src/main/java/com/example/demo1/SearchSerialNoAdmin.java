package com.example.demo1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SearchSerialNoAdmin extends AppCompatActivity {
    ListView mListView;
    SearchView mSearchView;
    ArrayList<String> myArrayList = new ArrayList<>();
    DatabaseReference referenceVendor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_serial_no_admin);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Search Serial No");
        setSupportActionBar(toolbar);

        mSearchView = findViewById(R.id.searchView);

        referenceVendor = FirebaseDatabase.getInstance().getReference("serials");
        mListView = findViewById(R.id.list_view);

        //listview retriving starts here........................................................

        final ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(SearchSerialNoAdmin.this, android.R.layout.simple_list_item_1, myArrayList);
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

        referenceVendor.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final String value = dataSnapshot.getValue(RegisterNewSerialUpload.class).toString();
                myArrayList.add(value);
                myArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                myArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currentSerialNo = parent.getItemAtPosition(position).toString();
                Intent i = new Intent(SearchSerialNoAdmin.this, SearchSerialNoAdmin2.class);
                i.putExtra("serialNo",currentSerialNo);
                startActivity(i);
            }
        });

        //listView data retriving ends here.....................................................

    }

}

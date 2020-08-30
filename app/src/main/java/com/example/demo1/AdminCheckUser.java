package com.example.demo1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminCheckUser extends AppCompatActivity {
    RecyclerView recyclerView;
    EditText mSearchView;
    Button mBtnVendor, mBtnDealer, mBtnPersonal;
    String selectedButton = "vendor";
    ArrayList<DataSetFireDealer> arrayListDealer;
    FirebaseRecyclerOptions<DataSetFireDealer> optionsDealer;
    FirebaseRecyclerAdapter<DataSetFireDealer, FirebaseViewHolderDealer> adapterDealer;

    ArrayList<DataSetFireVendor> arrayListVendor;
    FirebaseRecyclerOptions<DataSetFireVendor> optionsVendor;
    FirebaseRecyclerAdapter<DataSetFireVendor, FirebaseViewHolderVendor> adapterVendor;

    ArrayList<DataSetFirePersonal> arrayListPersonal;
    FirebaseRecyclerOptions<DataSetFirePersonal> optionsPersonal;
    FirebaseRecyclerAdapter<DataSetFirePersonal, FirebaseViewHolderPersonal> adapterPersonal;

    LoadingDialog loadingDialog;
    DatabaseReference databaseReference, deleteReferenceData, deleteReferenceUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_check_user);

        mBtnVendor = findViewById(R.id.rd_vendor);
        mBtnDealer = findViewById(R.id.rd_dealer);
        mBtnPersonal = findViewById(R.id.rd_personal);
        recyclerView = findViewById(R.id.recyclerView);
        mSearchView = findViewById(R.id.searchView);
        recyclerView.setLayoutManager(new LinearLayoutManager(AdminCheckUser.this));
        recyclerView.setHasFixedSize(true);

        arrayListVendor = new ArrayList<DataSetFireVendor>();
        arrayListDealer = new ArrayList<DataSetFireDealer>();
        arrayListPersonal = new ArrayList<DataSetFirePersonal>();

        loadingDialog = new LoadingDialog(AdminCheckUser.this);

        deleteReferenceData = FirebaseDatabase.getInstance().getReference("data");
        deleteReferenceUserName = FirebaseDatabase.getInstance().getReference("username");

        mBtnVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedButton = "vendor";
                mBtnVendor.setBackground(getResources().getDrawable(R.drawable.button2));
                mBtnDealer.setBackground(getResources().getDrawable(R.drawable.button3));
                mBtnPersonal.setBackground(getResources().getDrawable(R.drawable.button3));
                LoadData("");
            }
        });
        mBtnDealer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedButton = "dealer";
                mBtnVendor.setBackground(getResources().getDrawable(R.drawable.button3));
                mBtnDealer.setBackground(getResources().getDrawable(R.drawable.button2));
                mBtnPersonal.setBackground(getResources().getDrawable(R.drawable.button3));
                LoadData("");
            }
        });
        mBtnPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedButton = "personal";
                mBtnVendor.setBackground(getResources().getDrawable(R.drawable.button3));
                mBtnDealer.setBackground(getResources().getDrawable(R.drawable.button3));
                mBtnPersonal.setBackground(getResources().getDrawable(R.drawable.button2));
                LoadData("");
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
                if (s.toString() != null) {
                    LoadData(s.toString());
                } else {
                    LoadData("");
                }
            }
        });
    }

    private void LoadData(String data) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("credentials").child(selectedButton);

        if (selectedButton.equals("vendor")) {
            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("Vendor List");
            setSupportActionBar(toolbar);

            Query query = databaseReference.orderByChild("userNo").startAt(data).endAt(data + "\uf8ff");
            optionsVendor = new FirebaseRecyclerOptions.Builder<DataSetFireVendor>().setQuery(query, DataSetFireVendor.class).build();

            adapterVendor = new FirebaseRecyclerAdapter<DataSetFireVendor, FirebaseViewHolderVendor>(optionsVendor) {
                @Override
                protected void onBindViewHolder(@NonNull FirebaseViewHolderVendor holder, int position, @NonNull DataSetFireVendor model) {
                    holder.id.setText(model.getUserNo());
                    holder.name.setText(model.getOwnerFirstName() + " " + model.getOwnerLastName());
                    holder.shopName.setText(model.getShopeName());
                    holder.contact.setText(model.getContact());
                    holder.points.setText(model.getPoints());
                    holder.gst.setText(model.getGst_No());

                    holder.deleteVendor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(AdminCheckUser.this);
                            builder.setMessage("Are you sure want to delete this account?");
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
                                    loadingDialog.startLoadingDialog();
                                    loadingDialog.setText("Deleting...");
                                    HashMap<String, Object> profileMap = new HashMap<>();
                                    profileMap.put("activity", "deactivate");

                                    databaseReference.child(model.getUserNo()).updateChildren(profileMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    });
                                    deleteReferenceData.child(model.getContact()).child("1").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    });
                                    deleteReferenceUserName.child(model.getUserName()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(AdminCheckUser.this, "Your account is deleted successfully...", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    loadingDialog.dismissDialog();
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.setCancelable(false);
                            alertDialog.show();                        }
                    });

                    String activity = model.getActivity();
                    if (activity.equals("active")) {
                        String color = "#84E488";
                        holder.itemView.setBackgroundColor(Color.parseColor(color));
                    } else if (activity.equals("deactivate")) {
                        String color = "#C86E6E";
                        holder.itemView.setBackgroundColor(Color.parseColor(color));
                        holder.deleteVendor.setVisibility(View.INVISIBLE);
                    }
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(AdminCheckUser.this, AdminCheckUserVendor.class);
                            intent.putExtra("userNo", model.getUserNo());
                            intent.putExtra("active", model.getActivity());
                            startActivity(intent);
                        }
                    });
                }

                @NonNull
                @Override
                public FirebaseViewHolderVendor onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                    return new FirebaseViewHolderVendor(LayoutInflater.from(AdminCheckUser.this).inflate(R.layout.row, viewGroup, false));
                }
            };
            adapterVendor.startListening();
            recyclerView.setAdapter(adapterVendor);

        }
        else if (selectedButton.equals("dealer")) {
            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("Dealer List");
            setSupportActionBar(toolbar);

            Query query = databaseReference.orderByChild("userNo").startAt(data).endAt(data + "\uf8ff");
            optionsDealer = new FirebaseRecyclerOptions.Builder<DataSetFireDealer>().setQuery(query, DataSetFireDealer.class).build();

            adapterDealer = new FirebaseRecyclerAdapter<DataSetFireDealer, FirebaseViewHolderDealer>(optionsDealer) {
                @Override
                protected void onBindViewHolder(@NonNull FirebaseViewHolderDealer holder, int position, @NonNull DataSetFireDealer model) {
                    holder.id.setText(model.getUserNo());
                    holder.name.setText(model.getOwnerFirstName() + " " + model.getOwnerLastName());
                    holder.contact.setText(model.getContact());
                    holder.points.setText(model.getPoints());

                    holder.deleteDealer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(AdminCheckUser.this);
                            builder.setMessage("Are you sure want to delete this account?");
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
                                    loadingDialog.startLoadingDialog();
                                    loadingDialog.setText("Deleting...");

                                    HashMap<String, Object> profileMap = new HashMap<>();
                                    profileMap.put("activity", "deactivate");

                                    databaseReference.child(model.getUserNo()).updateChildren(profileMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    });
                                    deleteReferenceData.child(model.getContact()).child("2").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    });
                                    deleteReferenceUserName.child(model.getUserName()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(AdminCheckUser.this, "Your account is deleted successfully...", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    loadingDialog.dismissDialog();
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.setCancelable(false);
                            alertDialog.show();                        }
                    });


                    String activity = model.getActivity();
                    if (activity.equals("active")) {
                        String color = "#84E488";
                        holder.itemView.setBackgroundColor(Color.parseColor(color));
                    } else if (activity.equals("deactivate")) {
                        String color = "#C86E6E";
                        holder.itemView.setBackgroundColor(Color.parseColor(color));
                        holder.deleteDealer.setVisibility(View.INVISIBLE);
                    }

                }

                @NonNull
                @Override
                public FirebaseViewHolderDealer onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                    return new FirebaseViewHolderDealer(LayoutInflater.from(AdminCheckUser.this).inflate(R.layout.row2, viewGroup, false));
                }
            };
            adapterDealer.startListening();
            recyclerView.setAdapter(adapterDealer);
        }
        else if (selectedButton.equals("personal")) {
            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("Personal List");
            setSupportActionBar(toolbar);

            Query query = databaseReference.orderByChild("userNo").startAt(data).endAt(data + "\uf8ff");
            optionsPersonal = new FirebaseRecyclerOptions.Builder<DataSetFirePersonal>().setQuery(query, DataSetFirePersonal.class).build();

            adapterPersonal = new FirebaseRecyclerAdapter<DataSetFirePersonal, FirebaseViewHolderPersonal>(optionsPersonal) {
                @Override
                protected void onBindViewHolder(@NonNull FirebaseViewHolderPersonal holder, int position, @NonNull DataSetFirePersonal model) {
                    holder.id.setText(model.getUserNo());
                    holder.name.setText(model.getOwnerFirstName() + " " + model.getOwnerLastName());
                    holder.contact.setText(model.getContact());

                    holder.deletePersonal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(AdminCheckUser.this);
                            builder.setMessage("Are you sure want to delete this account?");
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
                                    loadingDialog.startLoadingDialog();
                                    loadingDialog.setText("Deleting...");

                                    HashMap<String, Object> profileMap = new HashMap<>();
                                    profileMap.put("activity", "deactivate");

                                    databaseReference.child(model.getUserNo()).updateChildren(profileMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    });
                                    deleteReferenceData.child(model.getContact()).child("3").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    });
                                    deleteReferenceUserName.child(model.getUserName()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(AdminCheckUser.this, "Your account is deleted successfully...", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    loadingDialog.dismissDialog();
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.setCancelable(false);
                            alertDialog.show();                        }
                    });

                    String activity = model.getActivity();
                    if (activity.equals("active")) {
                        String color = "#84E488";
                        holder.itemView.setBackgroundColor(Color.parseColor(color));
                    } else if (activity.equals("deactivate")) {
                        String color = "#C86E6E";
                        holder.itemView.setBackgroundColor(Color.parseColor(color));
                        holder.deletePersonal.setVisibility(View.INVISIBLE);
                    }

                }

                @NonNull
                @Override
                public FirebaseViewHolderPersonal onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                    return new FirebaseViewHolderPersonal(LayoutInflater.from(AdminCheckUser.this).inflate(R.layout.row3, viewGroup, false));
                }
            };
            adapterPersonal.startListening();
            recyclerView.setAdapter(adapterPersonal);
        }
    }
}

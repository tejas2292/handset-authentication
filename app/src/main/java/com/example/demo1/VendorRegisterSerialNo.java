package com.example.demo1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iceteck.silicompressorr.FileUtils;
import com.iceteck.silicompressorr.SiliCompressor;
import com.squareup.picasso.Picasso;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class VendorRegisterSerialNo extends AppCompatActivity {
    EditText mInvoiceNo, mBuyerFirstName, mBuyerLastName, mContact1, mContact2, mBrandName, mModelNo,
            mEmail, mRecoveryMail;
    public TextView mVendorId, mShopName, mWarrantyPeriod, mAddress, mAddBrand;
    Button mBtnRegister, mSetImg1, mSetImg2;
    String invoiceNo, buyerFirstName, buyerLastName, contact1, contact2, brandName, modelNo, modelSerialNo,
            date, UNo, shopName, vendorPhoneNo, parentBrandName, email, recoveryMail;
    public static String modelSerialNoConfirm;
    public static EditText mModelSerialNo, mModelSerialNoConfirm;
    public static ImageView img1, img2;
    String Points, vendorAddress, timeStamp, timeStamp2;
    SearchableSpinner spinner, spinner2;
    public static Uri pickedImageUri, pickedImageUri2;
    DatabaseReference referenceVendor, reference, referenceBrand, referenceModel, historyVendor, transaction;
    StorageReference storageReference;
    LoadingDialog loadingDialog;
    public String currentPhotoPath, currentPhotoPath1;
    private static final int CAMERA_PERMISSION_CODE = 100;
    Uri url1, url2;

    int totalSerialVendor = 0;

    String stringTransaction;
    long transactionValue = 0;

    ArrayAdapter<String> adapter, adapter2;
    ArrayList<String> spinnerDataList, spinnerDataList2;

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_register_serial_no);

        loadingDialog = new LoadingDialog(VendorRegisterSerialNo.this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Register New Serial");
        setSupportActionBar(toolbar);
        // mAddBrand = findViewById(R.id.brand_add);
        spinner = findViewById(R.id.spinner_brand);
        spinner2 = findViewById(R.id.spinner_model);
        mWarrantyPeriod = findViewById(R.id.et_warrantyPeriod);
        mBtnRegister = findViewById(R.id.btn_register);
        img1 = findViewById(R.id.img_mobile_box);
        img2 = findViewById(R.id.img_invoice);
        mSetImg1 = findViewById(R.id.btn_mobBox);
        mSetImg2 = findViewById(R.id.btn_invoice);
        mVendorId = findViewById(R.id.tv_vendor_id);
        mShopName = findViewById(R.id.tv_vendor_shop_name);
        mInvoiceNo = findViewById(R.id.et_invoiceNo);
        mBuyerFirstName = findViewById(R.id.et_buyerFirstName);
        mBuyerLastName = findViewById(R.id.et_buyerLastName);
        mContact1 = findViewById(R.id.et_purchaserCont);
        mContact2 = findViewById(R.id.et_purchaserContAlterenative);
        mAddress = findViewById(R.id.tv_address);
        mModelSerialNo = findViewById(R.id.et_modelSerialNo);
        mModelSerialNoConfirm = findViewById(R.id.et_modelSerialNoConfirm);
        mEmail = findViewById(R.id.et_email);
        mRecoveryMail = findViewById(R.id.et_recovery_email);

        checkPermission(Manifest.permission.CAMERA,
                CAMERA_PERMISSION_CODE);

        UNo = getIntent().getStringExtra("uno");
        shopName = getIntent().getStringExtra("shop");
        vendorPhoneNo = getIntent().getStringExtra("vendorPhone");
        Points = getIntent().getStringExtra("points");
        vendorAddress = getIntent().getStringExtra("address");

        if (UNo != null) {
            reference = FirebaseDatabase.getInstance().getReference("credentials").child("vendor").child(UNo);
            referenceVendor = FirebaseDatabase.getInstance().getReference("serials");
            referenceBrand = FirebaseDatabase.getInstance().getReference().child("brands");
            historyVendor = FirebaseDatabase.getInstance().getReference().child("HistoryVendor");
            transaction = FirebaseDatabase.getInstance().getReference();



            ////////////////////////////////////////////////////////////////////////////////////////////
            historyVendor.child(UNo).child("points").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        totalSerialVendor = (int) dataSnapshot.getChildrenCount();
                    } else {
                        Toast.makeText(VendorRegisterSerialNo.this, "not present", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            ////////////////////////////////////////////////////////////////////////////////////////////


        } else {
            Toast.makeText(this, "Wait data is still loading..", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(VendorRegisterSerialNo.this, MainActivityVendor.class));
            finish();
        }
        storageReference = FirebaseStorage.getInstance().getReference();
        mVendorId.setText(UNo);
        mShopName.setText(shopName);
        mAddress.setText(vendorAddress);
        mSetImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImg1(VendorRegisterSerialNo.this);
            }
        });
        mSetImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImg2(VendorRegisterSerialNo.this);
            }
        });

        final DialogFragment dialogFragment = new DatePickerDialogTheme4();


  /*      mAddBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VendorRegisterSerialNo.this, addBrands.class));
            }
        });
*/        ////////////////////////////////////////////////////////////////////////////////////////////
        spinnerDataList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(VendorRegisterSerialNo.this, android.R.layout.simple_spinner_dropdown_item,
                spinnerDataList);

        spinnerDataList2 = new ArrayList<>();
        adapter2 = new ArrayAdapter<String>(VendorRegisterSerialNo.this, android.R.layout.simple_spinner_dropdown_item,
                spinnerDataList2);

        spinner.setAdapter(adapter);
        retriveSpinnerData();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parentBrandName = spinner.getSelectedItem().toString();
                brandName = spinner.getSelectedItem().toString();
                Toast.makeText(VendorRegisterSerialNo.this, "" + parentBrandName, Toast.LENGTH_SHORT).show();
                referenceModel = FirebaseDatabase.getInstance().getReference("brands").child(parentBrandName);
                referenceModel.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Set<String> set = new HashSet<String>();
                        Iterator i = dataSnapshot.getChildren().iterator();
                        while (i.hasNext()) {
                            set.add(((DataSnapshot) i.next()).getKey());
                        }
                        adapter2.clear();
                        adapter2.addAll(set);
                        adapter2.notifyDataSetChanged();
                        spinner2.setAdapter(adapter2);

                        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                modelNo = spinner2.getSelectedItem().toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invoiceNo = mInvoiceNo.getText().toString().trim();
                buyerFirstName = mBuyerFirstName.getText().toString().trim();
                buyerLastName = mBuyerLastName.getText().toString().trim();
                contact1 = mContact1.getText().toString().trim();
                contact2 = mContact2.getText().toString().trim();
                date = mWarrantyPeriod.getText().toString().trim();
                modelSerialNo = mModelSerialNo.getText().toString().trim();
                modelSerialNoConfirm = mModelSerialNoConfirm.getText().toString().trim();
                email = mEmail.getText().toString().trim();
                recoveryMail = mRecoveryMail.getText().toString().trim();

                if (invoiceNo.equals("") || buyerFirstName.equals("") || buyerLastName.equals("")
                        || contact1.equals("") || contact1.length() < 10 || contact2.equals("")
                        || contact2.length() < 10 || brandName.equals("") ||
                        modelNo.equals("") || date.equals("") || modelSerialNo.equals("") ||
                        modelSerialNoConfirm.equals("") || email.equals("") || recoveryMail.equals("")
                        || pickedImageUri == null || pickedImageUri2 == null) {
                    if (invoiceNo.equals("")) {
                        mInvoiceNo.setError("Provide Invoice No");
                    }
                    if (buyerFirstName.equals("")) {
                        mBuyerFirstName.setError("Provide Buyer First Name");
                    }
                    if (buyerLastName.equals("")) {
                        mBuyerLastName.setError("Provide Buyer Last Name");
                    }
                    if (contact1.equals("")) {
                        mContact1.setError("Provide Contact No");
                    }
                    if (contact1.length() < 10) {
                        mContact1.setError("Provide Correct Contact No");
                    }
                    if (contact2.equals("")) {
                        mContact2.setError("Provide Alternet Contact No");
                    }
                    if (contact2.length() < 10) {
                        mContact2.setError("Provide Correct Contact No");
                    }
                    if (email.equals("")) {
                        mEmail.setError("Provide Email-id");
                    }
                    if (recoveryMail.equals("")) {
                        mRecoveryMail.setError("Provide Recovery Email-id");
                    }
                    if (brandName.equals("")) {
                        mBrandName.setError("Provide Brand Name");
                    }
                    if (modelNo.equals("")) {
                        mModelNo.setError("Provide Model No");
                    }
                    if (date.equals("")) {
                        mWarrantyPeriod.setError("Provide Date");
                    }
                    if (modelSerialNo.equals("")) {
                        mModelSerialNo.setError("Provide Model Serial No");
                    }
                    if (modelSerialNoConfirm.equals("")) {
                        mModelSerialNoConfirm.setError("Confirm Model Serial No");
                    }
                    if(pickedImageUri == null)
                    {
                        Toast.makeText(VendorRegisterSerialNo.this, "Select image of mobile box..", Toast.LENGTH_SHORT).show();
                    }
                    if(pickedImageUri2 == null)
                    {
                        Toast.makeText(VendorRegisterSerialNo.this, "Select image of invoice..", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if (modelSerialNoConfirm.equals(modelSerialNo)) {
                        loadingDialog.startLoadingDialog();
                        loadingDialog.setText("Registering...");

                        referenceVendor.child(modelSerialNoConfirm).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    mModelSerialNoConfirm.setError("This Serial no is already registered..");
                                    loadingDialog.dismissDialog();
                                } else {
                                    if (pickedImageUri != null) {
                                        try {
                                            final File file = new File(SiliCompressor.with(VendorRegisterSerialNo.this)
                                                    .compress(FileUtils.getPath(VendorRegisterSerialNo.this, pickedImageUri), new File(VendorRegisterSerialNo.this.getCacheDir(), "temp")));
                                            Uri uri = Uri.fromFile(file);
                                            storageReference.child("serials").child(vendorPhoneNo).child("register/" + UUID.randomUUID().toString()).putFile(uri)
                                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                            file.delete();
                                                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                                                            while (!uri.isComplete()) ;
                                                            url1 = uri.getResult();

                                                            if (pickedImageUri2 != null) {
                                                                try {
                                                                    final File file2 = new File(SiliCompressor.with(VendorRegisterSerialNo.this)
                                                                            .compress(FileUtils.getPath(VendorRegisterSerialNo.this, pickedImageUri2), new File(VendorRegisterSerialNo.this.getCacheDir(), "temp")));
                                                                    Uri uri2 = Uri.fromFile(file2);
                                                                    storageReference.child("serialUpload/" + UUID.randomUUID().toString()).putFile(uri2)
                                                                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                                @Override
                                                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                                    file2.delete();
                                                                                    Task<Uri> uri2 = taskSnapshot.getStorage().getDownloadUrl();
                                                                                    while (!uri2.isComplete());
                                                                                    url2 = uri2.getResult();

                                                                                    loadingDialog.startLoadingDialog();
                                                                                    transaction.addValueEventListener(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                            if(dataSnapshot.exists())
                                                                                            {
                                                                                                stringTransaction = dataSnapshot.child("tvendor").getValue().toString();
                                                                                                long temp = Long.parseLong(stringTransaction);
                                                                                                transactionValue = temp + 1;
                                                                                            }
                                                                                        }

                                                                                        @Override
                                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                        }
                                                                                    });

                                                                                    timeStamp2 = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

                                                                                    final RegisterNewSerialUpload v1 = new RegisterNewSerialUpload(invoiceNo, buyerFirstName, buyerLastName, contact1, contact2,
                                                                                            brandName, modelNo, date, modelSerialNoConfirm, url1.toString(), url2.toString(), UNo, shopName, vendorPhoneNo, vendorAddress,
                                                                                            email , recoveryMail, timeStamp2);

                                                                                    referenceVendor.child(modelSerialNoConfirm).setValue(v1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                                            final historyVendorUploadFinal history = new historyVendorUploadFinal(String.valueOf(totalSerialVendor + 1),
                                                                                                    timeStamp2, modelSerialNoConfirm, "-", invoiceNo, "-", "100", "DeductedByOwn","V"+transactionValue);

                                                                                            transaction.child("tvendor").setValue(transactionValue).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void aVoid) {
                                                                                                    final HistoryVendorUpload h1 = new HistoryVendorUpload(modelSerialNoConfirm, timeStamp2, invoiceNo,"V"+transactionValue);
                                                                                                    historyVendor.child(UNo).child("serial").child(modelSerialNoConfirm).setValue(h1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                            int db = Integer.parseInt(Points);
                                                                                                            int val = 100;
                                                                                                            String dbNewPoints = String.valueOf(db - val);
                                                                                                            HashMap<String, Object> profileMap = new HashMap<>();
                                                                                                            profileMap.put("points", dbNewPoints);

                                                                                                            historyVendor.child(UNo).child("points").child(String.valueOf(totalSerialVendor + 1)).setValue(history)
                                                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                        @Override
                                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                                            reference.updateChildren(profileMap);
                                                                                                                            Toast.makeText(VendorRegisterSerialNo.this, "Data Registered Successfully", Toast.LENGTH_SHORT).show();
                                                                                                                            startActivity(new Intent(VendorRegisterSerialNo.this, MainActivityVendor.class));
                                                                                                                            finish();
                                                                                                                        }
                                                                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                                                                @Override
                                                                                                                public void onFailure(@NonNull Exception e) {
                                                                                                                    loadingDialog.dismissDialog();
                                                                                                                    Toast.makeText(VendorRegisterSerialNo.this, "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                                                                }
                                                                                                            });

                                                                                                        }
                                                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                                                        @Override
                                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                                            loadingDialog.dismissDialog();
                                                                                                            Toast.makeText(VendorRegisterSerialNo.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                                                        }
                                                                                                    });

                                                                                                }
                                                                                            });

                                                                                        }
                                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                                        @Override
                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                            loadingDialog.dismissDialog();
                                                                                            Toast.makeText(VendorRegisterSerialNo.this, "Failure of Data Regestration", Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    });


                                                                                }
                                                                            });
                                                                } catch (Exception e) {
                                                                    loadingDialog.dismissDialog();
                                                                    Toast.makeText(VendorRegisterSerialNo.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            } else {
                                                                loadingDialog.dismissDialog();
                                                                Toast.makeText(VendorRegisterSerialNo.this, "Please provide image of invoice", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        } catch (Exception e) {
                                            loadingDialog.dismissDialog();
                                            Toast.makeText(VendorRegisterSerialNo.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        loadingDialog.dismissDialog();
                                        Toast.makeText(VendorRegisterSerialNo.this, "Please provide image of mobile box", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    } else {
                        mModelSerialNoConfirm.setError("Serial No Dosen't Match");
                    }
                }
            }
        });

        mWarrantyPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.show(getSupportFragmentManager(), "theme 4");
            }
        });

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private void retriveSpinnerData() {
        referenceBrand.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();
                while (i.hasNext()) {
                    set.add(((DataSnapshot) i.next()).getKey());
                }
                adapter.clear();
                adapter.addAll(set);
                adapter.notifyDataSetChanged();
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(VendorRegisterSerialNo.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(VendorRegisterSerialNo.this, permission)
                == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(VendorRegisterSerialNo.this,
                    new String[]{permission},
                    requestCode);
        } else {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(VendorRegisterSerialNo.this,
                        "Camera Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(VendorRegisterSerialNo.this,
                        "Camera Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    private void selectImg1(Context context) {
        final CharSequence[] options = {"Take Photo", "Scan Barcode/ QR Code", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    dispatchTakePictureIntent();
                } else if (options[item].equals("Scan Barcode/ QR Code")) {
                    Intent intent = new Intent(VendorRegisterSerialNo.this, ContinuousCaptureActivity.class);
                    startActivity(intent);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }

        });
        builder.show();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    private void selectImg2(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    dispatchTakePictureIntent1();
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 101);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory().toString(), "whatever_directory_existing_or_not/sub_dir_if_needed/");
        storageDir.mkdirs();
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    private File createImageFile1() throws IOException {

        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory().toString(), "whatever_directory_existing_or_not/sub_dir_if_needed/");
        storageDir.mkdirs();
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentPhotoPath1 = image.getAbsolutePath();
        return image;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 99);
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    private void dispatchTakePictureIntent1() {
        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile1();
            } catch (IOException ex) {

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(VendorRegisterSerialNo.this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 100);
            }
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 99:
                    if (resultCode == Activity.RESULT_OK) {
                        File f = new File(currentPhotoPath);
                        //img1.setImageURI(Uri.fromFile(f));

                        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        pickedImageUri = Uri.fromFile(f);
                        Picasso.get().load(pickedImageUri).into(img1);
                        mModelSerialNo.setFocusable(true);
                        mModelSerialNo.setFocusableInTouchMode(true);
                        mModelSerialNo.setClickable(true);
                        mModelSerialNoConfirm.setFocusable(true);
                        mModelSerialNoConfirm.setFocusableInTouchMode(true);
                        mModelSerialNoConfirm.setClickable(true);
                        mediaScanIntent.setData(pickedImageUri);
                        VendorRegisterSerialNo.this.sendBroadcast(mediaScanIntent);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK) {
                        pickedImageUri = data.getData();

                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = VendorRegisterSerialNo.this.getContentResolver().query(pickedImageUri, filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();

                        ExifInterface exif = null;
                        try {
                            File pictureFile = new File(picturePath);
                            exif = new ExifInterface(pictureFile.getAbsolutePath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        int orientation = ExifInterface.ORIENTATION_NORMAL;

                        if (exif != null)
                            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                        switch (orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                Picasso.get().load(pickedImageUri).rotate(90f).into(img1);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                Picasso.get().load(pickedImageUri).rotate(180f).into(img1);
                                break;

                            case ExifInterface.ORIENTATION_ROTATE_270:
                                Picasso.get().load(pickedImageUri).rotate(270f).into(img1);
                                break;
                            default:
                                Picasso.get().load(pickedImageUri).into(img1);
                                break;
                        }

                        mModelSerialNo.setFocusable(true);
                        mModelSerialNo.setFocusableInTouchMode(true);
                        mModelSerialNo.setClickable(true);
                        mModelSerialNoConfirm.setFocusable(true);
                        mModelSerialNoConfirm.setFocusableInTouchMode(true);
                        mModelSerialNoConfirm.setClickable(true);

                    }
                    break;
                case 100:
                    if (resultCode == Activity.RESULT_OK) {
                        File f = new File(currentPhotoPath1);
                        //img2.setImageURI(Uri.fromFile(f));

                        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        pickedImageUri2 = Uri.fromFile(f);
                        Picasso.get().load(pickedImageUri2).into(img2);
                        mediaScanIntent.setData(pickedImageUri2);
                        VendorRegisterSerialNo.this.sendBroadcast(mediaScanIntent);
                    }

                    break;
                case 101:
                    if (resultCode == RESULT_OK) {
                        pickedImageUri2 = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = VendorRegisterSerialNo.this.getContentResolver().query(pickedImageUri2, filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();

                        ExifInterface exif = null;
                        try {
                            File pictureFile = new File(picturePath);
                            exif = new ExifInterface(pictureFile.getAbsolutePath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        int orientation = ExifInterface.ORIENTATION_NORMAL;

                        if (exif != null)
                            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                        switch (orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                Picasso.get().load(pickedImageUri2).rotate(90f).into(img2);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                Picasso.get().load(pickedImageUri2).rotate(180f).into(img2);
                                break;

                            case ExifInterface.ORIENTATION_ROTATE_270:
                                Picasso.get().load(pickedImageUri2).rotate(270f).into(img2);
                                break;
                            default:
                                Picasso.get().load(pickedImageUri2).into(img2);
                                break;
                        }
                    }
                    break;
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    public static class DatePickerDialogTheme4 extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        String date;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            //these three lines are used to for cancle set previous dates
            calendar.add(Calendar.DATE, 0);
            Date newDate = calendar.getTime();
            datePickerDialog.getDatePicker().setMinDate(newDate.getTime() - (newDate.getTime() % (24 * 60 * 60 * 1000)));
            //here it ends
            return datePickerDialog;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            int month2 = month + 1;
            String formattedMonth = "" + month2;
            String formattedDayOfMonth = "" + day;

            if(month2 < 10){

                formattedMonth = "0" + month2;
            }
            if(day < 10){

                formattedDayOfMonth = "0" + day;
            }
            TextView textView = getActivity().findViewById(R.id.et_warrantyPeriod);
            textView.setText(formattedDayOfMonth + "/" + formattedMonth + "/" + year);
            date = textView.getText().toString().trim();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onBackPressed() {
        startActivity(new Intent(VendorRegisterSerialNo.this, MainActivityVendor.class));
        finish();
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////
}

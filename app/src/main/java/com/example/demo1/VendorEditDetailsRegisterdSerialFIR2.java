package com.example.demo1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iceteck.silicompressorr.FileUtils;
import com.iceteck.silicompressorr.SiliCompressor;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import static com.example.demo1.R.drawable.file_upload;

public class VendorEditDetailsRegisterdSerialFIR2 extends AppCompatActivity {
    Button mBtnAddFirImg, mBtnUploadFirImg, mBtnFinish;
    String FIRNo, PoliceStationAddress;
    ImageView img1;
    EditText mFirNo, mPoliceStationAddress;
    TextView txtCount;
    int firImgCount = 5;
    String currentPhotoPath1;
    Uri pickedImageUri2, url2;
    String serial,vendorPhoneNo;
    StorageReference storageReference;
    DatabaseReference referenceVendor,referenceVendor2;
    LoadingDialog loadingDialog;
    LinearLayout linearMain;
    RelativeLayout firHide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_edit_details_registerd_serial_f_i_r2);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Claim FIR");
        setSupportActionBar(toolbar);

        loadingDialog = new LoadingDialog(VendorEditDetailsRegisterdSerialFIR2.this);

        vendorPhoneNo = getIntent().getStringExtra("vendorPhoneNo");
        serial = getIntent().getStringExtra("serial");
        storageReference = FirebaseStorage.getInstance().getReference();
        referenceVendor = FirebaseDatabase.getInstance().getReference("serials").child(serial).child("FIR");
        referenceVendor2 = FirebaseDatabase.getInstance().getReference("serials").child(serial).child("FIRIMAGE");

        txtCount = findViewById(R.id.textViewCountImage);
        mBtnAddFirImg = findViewById(R.id.btnAddFirImg);
        mBtnUploadFirImg = findViewById(R.id.btnUploadFirImg);
        linearMain = findViewById(R.id.linearMain);
        firHide = findViewById(R.id.firHide);
        img1 = findViewById(R.id.firimg1);
        mBtnFinish = findViewById(R.id.finish);
        mFirNo = findViewById(R.id.et_firNo);
        mPoliceStationAddress = findViewById(R.id.et_policestationaddress);

        mBtnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();
                loadingDialog.setText("Registering your FIR");

                FIRNo = mFirNo.getText().toString();
                PoliceStationAddress = mPoliceStationAddress.getText().toString();

                if(FIRNo.equals("") || PoliceStationAddress.equals("")){
                    loadingDialog.dismissDialog();
                    if(FIRNo.equals("")){
                        mFirNo.setError("Provide FIR No.");
                    }
                    else if(PoliceStationAddress.equals("")){
                        mPoliceStationAddress.setError("Provide Police Station Address.");
                    }
                }
                else {
                    final UploadFIR u1 = new UploadFIR(FIRNo, PoliceStationAddress);

                    referenceVendor.setValue(u1).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(VendorEditDetailsRegisterdSerialFIR2.this, "FIR claimed successfully.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(VendorEditDetailsRegisterdSerialFIR2.this, MainActivityVendor.class));
                            finish();
                            loadingDialog.dismissDialog();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loadingDialog.dismissDialog();
                            Toast.makeText(VendorEditDetailsRegisterdSerialFIR2.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        mBtnAddFirImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firImgCount > 0) {
                    selectImg1(VendorEditDetailsRegisterdSerialFIR2.this);
                }
                else {
                    txtCount.setText("Add Photos:\n(you can't add more images now)");
                    Toast.makeText(VendorEditDetailsRegisterdSerialFIR2.this, "You cant upload more than 5 images..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBtnUploadFirImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();
                loadingDialog.setText("Uploading..");
                if (firImgCount > 0) {
                    if (pickedImageUri2 != null) {
                        try {
                            final File file2 = new File(SiliCompressor.with(VendorEditDetailsRegisterdSerialFIR2.this)
                                    .compress(FileUtils.getPath(VendorEditDetailsRegisterdSerialFIR2.this, pickedImageUri2), new File(VendorEditDetailsRegisterdSerialFIR2.this.getCacheDir(), "temp")));
                            Uri uri2 = Uri.fromFile(file2);
                            storageReference.child("serials").child(vendorPhoneNo).child("fir/" + UUID.randomUUID().toString()).putFile(uri2)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            file2.delete();
                                            Task<Uri> uri2 = taskSnapshot.getStorage().getDownloadUrl();
                                            while (!uri2.isComplete());
                                            url2 = uri2.getResult();

                                            referenceVendor2.child(String.valueOf(firImgCount)).setValue(url2.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    img1.setImageDrawable(getApplicationContext().getResources().getDrawable(file_upload));
                                                    pickedImageUri2 = null;
                                                    firImgCount--;
                                                    if(firImgCount == 0)
                                                    {
                                                        Toast.makeText(VendorEditDetailsRegisterdSerialFIR2.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                                        txtCount.setText("Add Photos:\n(you can't add more images now)");
                                                        firHide.setVisibility(View.INVISIBLE);
                                                        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mBtnFinish.getLayoutParams();
                                                        lp.addRule(RelativeLayout.BELOW, linearMain.getId());
                                                        mBtnFinish.setLayoutParams(lp);
                                                        loadingDialog.dismissDialog();
                                                    }
                                                    else {
                                                        Toast.makeText(VendorEditDetailsRegisterdSerialFIR2.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                                        txtCount.setText("Add Photos:\n(you can add upto " + firImgCount + " images only)");
                                                        loadingDialog.dismissDialog();
                                                    }
                                                }
                                            });
                                        }

                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(VendorEditDetailsRegisterdSerialFIR2.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    loadingDialog.dismissDialog();
                                }
                            });
                        } catch (Exception e) {
                            loadingDialog.dismissDialog();
                            Toast.makeText(VendorEditDetailsRegisterdSerialFIR2.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else{
                    loadingDialog.dismissDialog();
                    txtCount.setText("Add Photos:\n(you can't add more images now)");
                    Toast.makeText(VendorEditDetailsRegisterdSerialFIR2.this, "You cant upload more than 5 images..", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

            ////////////////////////////////////////////////////////////////////////////////////////////////
            private void selectImg1(Context context) {
                final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                //builder.setTitle("Choose your profile picture");

                builder.setItems(options, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (options[item].equals("Take Photo")) {
                            dispatchTakePictureIntent1();
                        } else if (options[item].equals("Choose from Gallery")) {
                            try {
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto, 101);
                            }
                            catch (Exception e){
                                Toast.makeText(context, "try again.", Toast.LENGTH_SHORT).show();
                            }

                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }

            private void dispatchTakePictureIntent1() {
                Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    photoFile = createImageFile1();
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(VendorEditDetailsRegisterdSerialFIR2.this,
                                "com.example.android.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, 100);
                    }
                }
            }

            private File createImageFile1() {

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "_";
                File storageDir = new File(Environment.getExternalStorageDirectory().toString(), "whatever_directory_existing_or_not/sub_dir_if_needed/");
                storageDir.mkdirs();
                File image = null;
                try {
                    image = File.createTempFile(
                            imageFileName,
                            ".jpg",
                            storageDir
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
                currentPhotoPath1 = image.getAbsolutePath();
                return image;
            }

            @Override
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                if (resultCode != RESULT_CANCELED) {
                    switch (requestCode) {
                        case 100:
                            if (resultCode == Activity.RESULT_OK) {
                                File f = new File(currentPhotoPath1);
                                //img2.setImageURI(Uri.fromFile(f));

                                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                pickedImageUri2 = Uri.fromFile(f);
                                Picasso.get().load(pickedImageUri2).into(img1);
                                mediaScanIntent.setData(pickedImageUri2);
                                VendorEditDetailsRegisterdSerialFIR2.this.sendBroadcast(mediaScanIntent);
                            }

                            break;
                        case 101:
                            if (resultCode == RESULT_OK) {
                                pickedImageUri2 = data.getData();
                                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                                Cursor cursor = VendorEditDetailsRegisterdSerialFIR2.this.getContentResolver().query(pickedImageUri2, filePathColumn, null, null, null);
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
                                        Picasso.get().load(pickedImageUri2).rotate(90f).into(img1);
                                        break;
                                    case ExifInterface.ORIENTATION_ROTATE_180:
                                        Picasso.get().load(pickedImageUri2).rotate(180f).into(img1);
                                        break;

                                    case ExifInterface.ORIENTATION_ROTATE_270:
                                        Picasso.get().load(pickedImageUri2).rotate(270f).into(img1);
                                        break;
                                    default:
                                        Picasso.get().load(pickedImageUri2).into(img1);
                                        break;
                                }

                            }
                            break;
                    }
                }
            }

            ////////////////////////////////////////////////////////////////////////////////////////////////

            @Override
            public void onBackPressed() {
                final AlertDialog.Builder builder = new AlertDialog.Builder(VendorEditDetailsRegisterdSerialFIR2.this);
                builder.setMessage("Are you sure want to go back on main screen?. if any data is uploaded it will be deleted.");
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
                        referenceVendor2.removeValue();
                        startActivity(new Intent(VendorEditDetailsRegisterdSerialFIR2.this,
                                MainActivityVendor.class));
                        finish();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                //here exit app alert close............................................
            }
        }

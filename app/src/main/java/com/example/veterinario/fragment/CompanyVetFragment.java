package com.example.veterinario.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.veterinario.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;


public class CompanyVetFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    Button buttonSend;
    ImageButton imageButtonSelectCompany;
    ImageButton imageButtonUpCompany;

    ImageView imageViewPhotoCompanyVet;

    Uri ImageUri;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("DataVet");

    StorageReference storageReference;

    EditText editTextNameCompanyVet,editTextAddress,editTextBusinessHours,
            editTextEmail,editTextPhone,editTextCellPhone,editTextFacebook,
            editTextIntagram,editTextTwitter,editTextPageLane,editTextDescription,
            editTextImgPhoto;

    ProgressBar progressBar;
    String imageUrl;

    public CompanyVetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = Objects.requireNonNull(getContext()).getContentResolver();
        MimeTypeMap mimeTypeMap =  MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadFile() {
        if (ImageUri != null){
            final StorageReference fileReference = storageReference.child( editTextImgPhoto.getText().toString() +  "." + getFileExtension(ImageUri));
            fileReference.putFile(ImageUri).
                    addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            },500);
                            Toast.makeText(getContext(),"Upload successful",Toast.LENGTH_LONG).show();
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imageUrl = uri.toString();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 *taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);
                        }
                    });
        }
        else {
            Toast.makeText(getContext(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            ImageUri = data.getData();
            Picasso.with(getActivity()).load(ImageUri).into(imageViewPhotoCompanyVet);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company_vet, container, false);
        imageButtonSelectCompany = view.findViewById(R.id.select_company_vet);
        imageButtonUpCompany= view.findViewById(R.id.up_image_Company);
        imageViewPhotoCompanyVet= view.findViewById(R.id.iv_result_company_vet);
        progressBar = view.findViewById(R.id.progress_bar_company_vet);
        editTextImgPhoto = view.findViewById(R.id.img_photo_company_vet);

        storageReference = FirebaseStorage.getInstance().getReference("ImageCompanyVet");

        imageButtonUpCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        imageButtonSelectCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();
            }
        });

        editTextImgPhoto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                imageButtonSelectCompany.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editTextNameCompanyVet = view.findViewById(R.id.nameCompanyVet);
        editTextAddress = view.findViewById(R.id.address);
        editTextBusinessHours = view.findViewById(R.id.business_hours);
        editTextPageLane = view.findViewById(R.id.lane_page);
        editTextEmail = view.findViewById(R.id.email);
        editTextPhone= view.findViewById(R.id.phone);
        editTextCellPhone = view.findViewById(R.id.cell_phone);
        editTextFacebook = view.findViewById(R.id.facebook);
        editTextIntagram = view.findViewById(R.id.instagram);
        editTextTwitter = view.findViewById(R.id.twitter);
        editTextDescription = view.findViewById(R.id.description);
        buttonSend = view.findViewById(R.id.sendCompanyVet);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> dataCompanyVet  = new HashMap<>();
                String name_company = editTextNameCompanyVet.getText().toString();
                String address = editTextAddress.getText().toString();
                String business_hours = editTextBusinessHours.getText().toString();
                String lane_page = editTextPageLane.getText().toString();
                String email = editTextEmail.getText().toString();
                String phone = editTextPhone.getText().toString();
                double cell_phone = Double.parseDouble (editTextCellPhone.getText().toString());
                String facebook = editTextFacebook.getText().toString();
                String instagram = editTextIntagram.getText().toString();
                String twitter = editTextTwitter.getText().toString();
                String description = editTextDescription.getText().toString();
                dataCompanyVet.put("nameCompany",name_company);
                dataCompanyVet.put("address",address);
                dataCompanyVet.put("business_hours",business_hours);
                dataCompanyVet.put("lane_page",lane_page);
                dataCompanyVet.put("email",email);
                dataCompanyVet.put("phone",phone);
                dataCompanyVet.put("cell_phone",cell_phone);
                dataCompanyVet.put("facebook",facebook);
                dataCompanyVet.put("instagram",instagram);
                dataCompanyVet.put("twitter",twitter);
                dataCompanyVet.put("description",description);
                dataCompanyVet.put("imageUrl",imageUrl);
                myRef.child("InscriptionCompanyVet").push().setValue(dataCompanyVet);
            }
        });

        return view;

    }


}
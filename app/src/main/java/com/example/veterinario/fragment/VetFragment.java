package com.example.veterinario.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.example.veterinario.Upload;
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

public class VetFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    Button buttonSendVet;

    ImageButton buttonUp;
    ImageButton buttonSelect;

    ImageView imageViewPhotoVet;

    Uri ImageUri;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("DataVet");

    StorageReference storageReference;

    EditText editTextName, editTextAddress, editTextEmail, editTextProfilePro,
            editTextCellPhone, editTextFacebook, editTextInstagram, editTextTwitter,
            editTextImgPhoto;

    ProgressBar progressBar;
    String imageUrl1;

    public VetFragment() {
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
            final StorageReference fileReference = storageReference.child( System.currentTimeMillis() +  "." + getFileExtension(ImageUri));
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
                                    imageUrl1 = uri.toString();
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
            Picasso.with(getActivity()).load(ImageUri).into(imageViewPhotoVet);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vet, container, false);
        buttonSelect = view.findViewById(R.id.select);
        buttonUp = view.findViewById(R.id.up_image);
        imageViewPhotoVet = view.findViewById(R.id.iv_result);
        progressBar = view.findViewById(R.id.progress_bar);
        //editTextImgPhoto = view.findViewById(R.id.img_photo_vet);

        storageReference = FirebaseStorage.getInstance().getReference("ImageVet");

        buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();
            }
        });


        /*editTextImgPhoto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                buttonSelect.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/


        editTextName = view.findViewById(R.id.name_vet);
        editTextAddress = view.findViewById(R.id.address_vet);
        editTextEmail = view.findViewById(R.id.email_vet);
        editTextProfilePro = view.findViewById(R.id.profileProVet);
        editTextCellPhone = view.findViewById(R.id.cell_phone_vet);
        editTextFacebook = view.findViewById(R.id.facebook_vet);
        editTextInstagram = view.findViewById(R.id.instagram_vet);
        editTextTwitter = view.findViewById(R.id.twitter_vet);
        buttonSendVet = view.findViewById(R.id.sendVet);

        buttonSendVet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> dataVet = new HashMap<>();
                String nameVet = editTextName.getText().toString();
                String address = editTextAddress.getText().toString();
                String email = editTextEmail.getText().toString();
                String profile = editTextProfilePro.getText().toString();
                double cell_phone = Double.parseDouble(editTextCellPhone.getText().toString());
                String facebook = editTextFacebook.getText().toString();
                String instragram = editTextInstagram.getText().toString();
                String twitter = editTextTwitter.getText().toString();
                dataVet.put("name", nameVet);
                dataVet.put("address", address);
                dataVet.put("email", email);
                dataVet.put("profile", profile);
                dataVet.put("cell_phone", cell_phone);
                dataVet.put("facebook", facebook);
                dataVet.put("instagram", instragram);
                dataVet.put("twitter", twitter);
                dataVet.put("imageUrl",imageUrl1);
                myRef.child("InscriptionVet").push().setValue(dataVet);
            }
        });
        return view;
    }
}
package com.example.veterinario.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.veterinario.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class VetFragment extends Fragment {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    EditText editTextName,editTextAddress,editTextEmail,editTextProfilePro,
            editTextCellPhone,editTextFacebook,editTextInstagram,editTextTwitter;
    Button buttonSendVet;

    public VetFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vet, container, false);

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
                Map<String, Object> dataVet  = new HashMap<>();
                String nameVet = editTextName.getText().toString();
                String address = editTextAddress.getText().toString();
                String email = editTextEmail.getText().toString();
                String profile = editTextProfilePro.getText().toString();
                double cell_phone = Double.parseDouble(editTextCellPhone.getText().toString());
                String facebook = editTextFacebook.getText().toString();
                String instragram = editTextInstagram.getText().toString();
                String twitter = editTextTwitter.getText().toString();
                dataVet.put("name", nameVet);
                dataVet.put("address",address);
                dataVet.put("email",email);
                dataVet.put("profile",profile);
                dataVet.put("cell_phone",cell_phone);
                dataVet.put("facebook",facebook);
                dataVet.put("instagram",instragram);
                dataVet.put("twitter",twitter);
                myRef.child("InscriptionVet").push().setValue(dataVet);
            }
        });
        return view;
    }
}
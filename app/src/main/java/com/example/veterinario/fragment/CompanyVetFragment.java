package com.example.veterinario.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
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


public class CompanyVetFragment extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    EditText editTextNameCompanyVet,editTextAddress,editTextBusinessHours,
            editTextEmail,editTextPhone,editTextCellPhone,editTextFacebook,
            editTextIntagram,editTextTwitter,editTextPageLane,editTextDescription;
    Button buttonSend;




    public CompanyVetFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company_vet, container, false);
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
                myRef.child("InscriptionCompanyVet").push().setValue(dataCompanyVet);
            }
        });

        return view;

    }


}
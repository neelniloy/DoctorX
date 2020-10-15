package com.sarker.hellodoctor.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.sarker.hellodoctor.AddDoctor;
import com.sarker.hellodoctor.R;


public class AdminPanel extends Fragment {

    private Button addDoctor,viewDoctor;
    private FirebaseAuth mAuth;



    public AdminPanel() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_admin_panel, container, false);

        addDoctor = view.findViewById(R.id.add_student);
        viewDoctor = view.findViewById(R.id.view_student);


        mAuth = FirebaseAuth.getInstance();

        addDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent n = new Intent(getActivity(), AddDoctor.class);
                startActivity(n);


            }
        });



        viewDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Toast.makeText(getActivity(), "Feature will be added soon", Toast.LENGTH_SHORT).show();


            }
        });





        return view;
    }
}
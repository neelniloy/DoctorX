package com.sarker.hellodoctor.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sarker.hellodoctor.DoctorList;
import com.sarker.hellodoctor.Login;
import com.sarker.hellodoctor.R;
import com.sarker.hellodoctor.adapter.SliderAdapter;
import com.sarker.hellodoctor.model.SlideInfo;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;


public class Home extends Fragment {


    SliderView sliderView;
    SliderAdapter adapter;

    private FirebaseAuth mfirebaseAuth;
    private DatabaseReference SlideRef;
    private String current_user_id;
    private CardView cardiologist,dentist,neurologists,dermatologists,psychiatrists,gynecologist;
    private TextView login;

    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        login = view.findViewById(R.id.tv_login_page);
        cardiologist = view.findViewById(R.id.Cardiologists);
        dentist = view.findViewById(R.id.Dentist);
        gynecologist = view.findViewById(R.id.Gynecologist);
        psychiatrists = view.findViewById(R.id.Psychiatrist);
        neurologists = view.findViewById(R.id.Neurologists);
        dermatologists = view.findViewById(R.id.Dermatologists);

        sliderView = view.findViewById(R.id.imageSlider);
        adapter = new SliderAdapter(getActivity());
        sliderView.setSliderAdapter(adapter);


        mfirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseuser = mfirebaseAuth.getCurrentUser();

        if (mFirebaseuser != null) {

            current_user_id = mfirebaseAuth.getCurrentUser().getUid();
            login.setVisibility(View.GONE);

        } else {
            login.setVisibility(View.VISIBLE);
        }

        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(R.color.colorPrimary);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();

        final List<SlideInfo> sliderItemList = new ArrayList<>();

        SlideRef = FirebaseDatabase.getInstance().getReference().child("SlideShow");

        SlideRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                        SlideInfo posts = postSnapshot.getValue(SlideInfo.class);

                        String key = postSnapshot.getKey();
                        String description = snapshot.child(key).child("description").getValue().toString();
                        String url = snapshot.child(key).child("imageUrl").getValue().toString();

                        posts.setImageKey(key);
                        posts.setImageUrl(url);
                        posts.setDescription(description);

                        sliderItemList.add(posts);

                    }
                    adapter.renewItems((ArrayList<SlideInfo>) sliderItemList);
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent n = new Intent(getActivity(), Login.class);
               startActivity(n);
               getActivity().finish();
            }
        });

        cardiologist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent n = new Intent(getActivity(), DoctorList.class);
                n.putExtra("dept","Cardiologist");
                startActivity(n);

            }
        });

        neurologists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent n = new Intent(getActivity(), DoctorList.class);
                n.putExtra("dept","Neurologists");
                startActivity(n);

            }
        });

        dermatologists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent n = new Intent(getActivity(), DoctorList.class);
                n.putExtra("dept","Dermatologists");
                startActivity(n);

            }
        });

        dentist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent n = new Intent(getActivity(), DoctorList.class);
                n.putExtra("dept","Dentist");
                startActivity(n);

            }
        });

        gynecologist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent n = new Intent(getActivity(), DoctorList.class);
                n.putExtra("dept","Gynecologist");
                startActivity(n);

            }
        });


        psychiatrists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent n = new Intent(getActivity(), DoctorList.class);
                n.putExtra("dept","Psychiatrists");
                startActivity(n);

            }
        });


        return view;
    }

}
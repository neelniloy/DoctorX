package com.sarker.hellodoctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DoctorDetails extends AppCompatActivity {

    private ImageView back;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private String current_user_id,dept,id,n;
    private DatabaseReference DoctorRef;
    private TextView name,degree,designation,hospital,phone,time,department;
    private CircleImageView profile;
    private Button appointment;
    private Boolean user = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_doctor_details);

        Intent i = getIntent();
        dept = i.getStringExtra("dept");
        id = i.getStringExtra("doctor_id");

        name = findViewById(R.id.doctorName);
        degree = findViewById(R.id.doctorDegree);
        phone = findViewById(R.id.doctorPhone);
        designation = findViewById(R.id.doctorDesignation);
        hospital = findViewById(R.id.doctorHospital);
        time = findViewById(R.id.doctorTime);
        profile = findViewById(R.id.doctor_profile);
        department = findViewById(R.id.doctorDept);
        back = findViewById(R.id.back);
        appointment = findViewById(R.id.btn_make_appoinment);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser mFirebaseuser = mAuth.getCurrentUser();

        if (mFirebaseuser != null) {

            current_user_id = mAuth.getCurrentUser().getUid();
            user = true;


        } else {
            user = false;
        }


        DoctorRef = FirebaseDatabase.getInstance().getReference("DoctorsData").child(dept).child(id);

        DoctorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                     n = snapshot.child("doctorName").getValue().toString();
                    String d = snapshot.child("doctorDegree").getValue().toString();
                    String dd = snapshot.child("doctorDesignation").getValue().toString();
                    String h = snapshot.child("doctorHospital").getValue().toString();
                    String url = snapshot.child("doctorImage").getValue().toString();
                    String p = snapshot.child("doctorPhone").getValue().toString();
                    String t = snapshot.child("doctorVisitingHour").getValue().toString();

                    name.setText(n);
                    degree.setText(d);
                    designation.setText(dd);
                    hospital.setText(h);
                    phone.setText(p);
                    time.setText(t);
                    department.setText(dept);

                    if (!url.equals(" ")){
                        Picasso.get().load(url).fit().placeholder(R.drawable.doctor_icon).into(profile);
                    }



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    Intent i = new Intent(DoctorDetails.this, Appointment.class);
                    i.putExtra("name",n);
                    startActivity(i) ;

            }
        });

    }
}
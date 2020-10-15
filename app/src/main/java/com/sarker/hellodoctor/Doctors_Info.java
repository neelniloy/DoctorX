package com.sarker.hellodoctor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class Doctors_Info extends AppCompatActivity {

    private CircleImageView doctorImage;
    private TextView doctorName,doctorDegree,doctorDesignation,doctorTime,doctorDate;
    private Button appointmentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors__info);

        doctorImage=findViewById(R.id.doctorImageId);
        doctorName=findViewById(R.id.doctorNameId);
        doctorDegree=findViewById(R.id.doctorDegreeId);
        doctorDesignation=findViewById(R.id.doctorDesignationId);
        doctorTime=findViewById(R.id.doctorTimeId);
        doctorDate=findViewById(R.id.doctorDateId);
        appointmentButton=findViewById(R.id.appointmentButtonId);

        appointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Doctors_Info.this, "Appointment Placed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
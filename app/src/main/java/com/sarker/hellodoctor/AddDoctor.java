package com.sarker.hellodoctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.sarker.hellodoctor.fragment.AdminPanel;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddDoctor extends AppCompatActivity {

    private ImageView back,editProfile;
    private TextInputEditText Name,Designation,Degree,VisitingHour, Phone,Hospital;
    private TextInputLayout nameEditTextLayout,designationTextLayout,phoneEditTextLayout, degreeEditTextLayout, visitingEditTextLayout,hospitalEditTextLayout;
    private Button btnAdd;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private String user_id,gender="",url,department;
    private DatabaseReference DoctorRef;
    private RadioButton male, female, custom;
    private CircleImageView doctorProfile;

    StorageReference storageReference;

    StorageTask storageTask;
    Uri contentURI,resultUri;

    private static final int PICK_FROM_GALLERY = 1;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK && data.getData() != null) {
            contentURI = data.getData();

            CropImage.activity(contentURI)
                    .setAspectRatio(1,1)
                    .start(AddDoctor.this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();

                Picasso.get().load(resultUri).fit().centerInside().into(doctorProfile);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_add_doctor);

        back = findViewById(R.id.back);
        doctorProfile = findViewById(R.id.doctorProfile);
        editProfile = findViewById(R.id.edit_doctor_profile);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnAdd = findViewById(R.id.btn_add_doctor);

        Name = findViewById(R.id.et_doctor_name);
        Designation = findViewById(R.id.et_designation);
        Degree = findViewById(R.id.et_degree);
        Hospital = findViewById(R.id.et_hospital_name);
        Phone = findViewById(R.id.et_contact_number);
        VisitingHour = findViewById(R.id.et_visiting_our);

        male = findViewById(R.id.g_m);
        female = findViewById(R.id.g_f);
        custom = findViewById(R.id.g_c);


        nameEditTextLayout = findViewById(R.id.editTextNameLayout);
        designationTextLayout = findViewById(R.id.editTextDesignationLayout);
        degreeEditTextLayout = findViewById(R.id.editTextDegreeLayout);
        phoneEditTextLayout = findViewById(R.id.editTextPhoneLayout);
        visitingEditTextLayout = findViewById(R.id.editTextVisitingHourLayout);
        hospitalEditTextLayout = findViewById(R.id.editTextHospitalLayout);



        mAuth = FirebaseAuth.getInstance();

        user_id = mAuth.getCurrentUser().getUid();

        storageReference = FirebaseStorage.getInstance().getReference().child("DoctorImage");;
        DoctorRef = FirebaseDatabase.getInstance().getReference().child("DoctorsData");


        String[] Dept = new String[] {"Cardiologist", "Neurologists", "Dermatologists", "Dentist","Gynecologist","Psychiatrists"};

        final AutoCompleteTextView dropdown = findViewById(R.id.departmentDropdown);

        final ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(
                        this,
                        R.layout.support_simple_spinner_dropdown_item,
                        Dept);
        dropdown.setAdapter(adapter);


        dropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                department = adapter.getItem(position);
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String name = Name.getText().toString();
                String designation = Designation.getText().toString();
                String degree = Degree.getText().toString();
                String hospital = Hospital.getText().toString();
                String phone = Phone.getText().toString();
                String hour = VisitingHour.getText().toString();


                if (name.isEmpty()) {
                    nameEditTextLayout.setError("*Name required");
                    Name.requestFocus();

                }
                else if (designation.isEmpty()) {
                    designationTextLayout.setError("*Designation required");
                    Designation.requestFocus();
                }
                else if (degree.isEmpty()) {
                    degreeEditTextLayout.setError("*Degree required");
                    Degree.requestFocus();
                }

                else if (hospital.isEmpty()) {
                    hospitalEditTextLayout.setError("*Hospital Name required");
                    Hospital.requestFocus();
                }
                else if (!(male.isChecked() || female.isChecked() || custom.isChecked())) {

                    Toast.makeText(AddDoctor.this, "Select Gender", Toast.LENGTH_SHORT).show();

                }

                else if (phone.isEmpty()) {
                    phoneEditTextLayout.setError("*Phone required");
                    Phone.requestFocus();
                }
                else if (hour.isEmpty()) {
                    visitingEditTextLayout.setError("*Visiting hour required");
                    VisitingHour.requestFocus();
                }
                else if (!(name.isEmpty() && designation.isEmpty() && degree.isEmpty() && hospital.isEmpty() && phone.isEmpty() && hour.isEmpty())) {

                    progressDialog = new ProgressDialog(AddDoctor.this);
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("Adding Information...");

                    if (resultUri!= null) {
                        SaveChange();
                    }else {
                            url = " ";
                            sendUserData();
                    }




                } else {
                    progressDialog.dismiss();
                    Toast.makeText(AddDoctor.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
                }
            }

        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_FROM_GALLERY);
            }
        });

    }

    private void sendUserData() {

        String name = Name.getText().toString();
        String designation = Designation.getText().toString();
        String degree = Degree.getText().toString();
        String hospital = Hospital.getText().toString();
        String phone = Phone.getText().toString();
        String hour = VisitingHour.getText().toString();

        String saveCurrentDate,saveCurrentTime;

        if (male.isChecked()){
            gender = "Male";
        }
        if (female.isChecked()) {
            gender = "Female";
        }
        if (custom.isChecked()){
            gender = "Custom";
        }




        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calFordDate.getTime());

        Map reg = new HashMap();

        reg.put("doctorName", name);
        reg.put("doctorDesignation", designation);
        reg.put("doctorDegree", degree);
        reg.put("doctorDept", department);
        reg.put("doctorGender", gender);
        reg.put("doctorHospital", hospital);
        reg.put("doctorPhone", phone);
        reg.put("doctorVisitingHour", hour);
        reg.put("adminUID", user_id);
        reg.put("doctorImage", url);
        reg.put("addedTime",saveCurrentTime);
        reg.put("addedDate",saveCurrentDate);

        DoctorRef.child(department).child(name.replaceAll("\\s+", "")+getDateInMillis(saveCurrentTime)).updateChildren(reg);


        progressDialog.dismiss();
        Toast.makeText(AddDoctor.this, "Information Successfully Added", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(AddDoctor.this,MainActivity.class);
        intent.putExtra("from","addDoctor");
        startActivity(intent);
        finish();


    }

    public static long getDateInMillis(String srcDate) {
        SimpleDateFormat desiredFormat = new SimpleDateFormat(
                "hh:mm a");

        long dateInMillis = 0;
        try {
            Date date = desiredFormat.parse(srcDate);
            dateInMillis = date.getTime();
            return dateInMillis;
        } catch (ParseException | java.text.ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void SaveChange() {


            UUID random = UUID.randomUUID();

            StorageReference fileReference = storageReference.child(String.valueOf(random));
            storageTask = fileReference.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri uri = urlTask.getResult();
                    url = uri.toString();

                    sendUserData();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }

}

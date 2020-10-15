package com.sarker.hellodoctor.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.sarker.hellodoctor.Login;
import com.sarker.hellodoctor.MainActivity;
import com.sarker.hellodoctor.R;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import static android.app.Activity.RESULT_OK;


public class Profile extends Fragment {


    private ImageView signOut,editModeOn,editModeOff,eName,eEmail,ePhone,eBlood,eProfile;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private String current_user_id,n,e,p,b;
    private DatabaseReference userRef;
    private StorageReference storageReference;
    private TextView name,email,phone,blood;
    private CircleImageView profile;

    private static final int PICK_FROM_GALLERY = 1;
    StorageTask storageTask;
    Uri contentURI,resultUri;



    public Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        progressDialog = new ProgressDialog(getActivity());

        name = view.findViewById(R.id.userName);
        email = view.findViewById(R.id.userEmail);
        phone = view.findViewById(R.id.userPhone);
        blood = view.findViewById(R.id.userBlood);
        profile = view.findViewById(R.id.user_profile);

        eName = view.findViewById(R.id.edit_name);
        eEmail = view.findViewById(R.id.edit_email);
        ePhone = view.findViewById(R.id.edit_phone);
        eBlood = view.findViewById(R.id.edit_blood);
        eProfile = view.findViewById(R.id.edit_profile);
        editModeOn = view.findViewById(R.id.edit_mode_on);
        editModeOff = view.findViewById(R.id.edit_mode_off);



        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseuser = mAuth.getCurrentUser();

        if (mFirebaseuser != null) {

            current_user_id = mAuth.getCurrentUser().getUid();


        } else {

        }

        signOut = view.findViewById(R.id.signOut);

        storageReference = FirebaseStorage.getInstance().getReference().child("UserImage").child(current_user_id);;
        userRef = FirebaseDatabase.getInstance().getReference("UsersData").child(current_user_id);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    n = snapshot.child("userName").getValue().toString();
                    e = snapshot.child("userEmail").getValue().toString();
                    p = snapshot.child("userPhone").getValue().toString();
                    b = snapshot.child("userBlood").getValue().toString();
                    String url = snapshot.child("userImage").getValue().toString();

                    name.setText(n);
                    email.setText(e);
                    phone.setText(p);
                    blood.setText(b);

                    if (!url.equals(" ")){
                        Picasso.get().load(url).fit().placeholder(R.drawable.doctor_icon).into(profile);
                    }



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        editModeOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editModeOff.setVisibility(View.VISIBLE);
                editModeOn.setVisibility(View.GONE);
                eName.setVisibility(View.VISIBLE);
                eEmail.setVisibility(View.VISIBLE);
                ePhone.setVisibility(View.VISIBLE);
                eBlood.setVisibility(View.VISIBLE);
                eProfile.setVisibility(View.VISIBLE);

                Toast.makeText(getActivity(), "Edit Mode On", Toast.LENGTH_SHORT).show();


            }
        });

        editModeOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editModeOn.setVisibility(View.VISIBLE);
                editModeOff.setVisibility(View.GONE);
                eName.setVisibility(View.GONE);
                eEmail.setVisibility(View.GONE);
                ePhone.setVisibility(View.GONE);
                eBlood.setVisibility(View.GONE);
                eProfile.setVisibility(View.GONE);


                Toast.makeText(getActivity(), "Edit Mode Off", Toast.LENGTH_SHORT).show();

            }
        });

        eProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_FROM_GALLERY);
            }
        });


        eName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText input = new EditText(getActivity());
                input.setText("    "+n);


                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setMessage("Enter Name")
                        .setView(input)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String text = input.getText().toString().trim();

                                if(text.isEmpty()){
                                    Toast.makeText(getActivity(), "Empty Field", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    userRef.child("userName").setValue(text);

                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();

            }
        });

        eEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText input = new EditText(getActivity());
                input.setText("    "+e);


                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setMessage("Enter Email")
                        .setView(input)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String text = input.getText().toString().trim();

                                if(text.isEmpty()){
                                    Toast.makeText(getActivity(), "Empty Field", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    userRef.child("userEmail").setValue(text);

                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();

            }
        });

        ePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText input = new EditText(getActivity());
                input.setText("    "+p);


                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setMessage("Enter Phone")
                        .setView(input)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String text = input.getText().toString().trim();

                                if(text.isEmpty()){
                                    Toast.makeText(getActivity(), "Empty Field", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    userRef.child("userPhone").setValue(text);

                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();

            }
        });


        eBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText input = new EditText(getActivity());
                input.setText("    "+b);


                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setMessage("Enter Blood Group")
                        .setView(input)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String text = input.getText().toString().trim();

                                if(text.isEmpty()){
                                    Toast.makeText(getActivity(), "Empty Field", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    userRef.child("userBlood").setValue(text);

                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();

            }
        });


        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setMessage("Are you sure you want to log out?");
                builder1.setCancelable(false);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                progressDialog = new ProgressDialog(getActivity());
                                progressDialog.show();
                                progressDialog.setMessage("Signing Out...");

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mAuth.signOut();
                                        progressDialog.dismiss();
                                        getActivity().finishAffinity();
                                        startActivity(new Intent(getActivity(), Login.class));
                                    }
                                },1500);
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

//                Window v =((AlertDialog)alert11).getWindow();
//                //view.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                v.setBackgroundDrawableResource(R.drawable.custom_button_so);

                alert11.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
                alert11.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));

                Button btnPositive = alert11.getButton(AlertDialog.BUTTON_POSITIVE);
                Button btnNegative = alert11.getButton(AlertDialog.BUTTON_NEGATIVE);

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
                layoutParams.weight = 10;
                btnPositive.setLayoutParams(layoutParams);
                btnNegative.setLayoutParams(layoutParams);


            }
        });

        return view;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FROM_GALLERY
                && resultCode == Activity.RESULT_OK && data.getData() != null) {

            contentURI = data.getData();

            CropImage.activity(contentURI)
                    .setAspectRatio(1,1)
                    .start(getActivity(),this);


        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();

                Picasso.get().load(resultUri).fit().centerInside().into(profile);
                SaveChange();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }



    public void SaveChange() {
        if (resultUri!= null) {

            progressDialog.setMessage("Updating Profile Pic");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);

            StorageReference fileReference = storageReference.child("ProfilePic");
            storageTask = fileReference.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri uri = urlTask.getResult();
                    String url = uri.toString();

                    userRef.child("userImage").setValue(url);
                    Toast.makeText(getActivity(), "Update Successfully!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(getActivity(), "Select Image", Toast.LENGTH_SHORT).show();
        }
    }
}
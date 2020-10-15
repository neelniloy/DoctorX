package com.sarker.hellodoctor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Registration extends AppCompatActivity {

    private TextView logIn;
    private TextInputEditText schoolName, userEmail,userPhone, userPass;
    private TextInputLayout nameEditTextLayout,phoneEditTextLayout, emailEditTextLayout, passEditTextLayout;
    private Button btnSignup;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private String user_id;
    private DatabaseReference current_user_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_registration);

        logIn = findViewById(R.id.tv_login);
        btnSignup = findViewById(R.id.cv_signup);

        schoolName = findViewById(R.id.et_name);
        userEmail = findViewById(R.id.et_remail);
        userPhone = findViewById(R.id.et_phone);
        userPass = findViewById(R.id.et_rpass);

        nameEditTextLayout = findViewById(R.id.editTextSchoolNameLayout);
        emailEditTextLayout = findViewById(R.id.editTextSignUpEmailLayout);
        phoneEditTextLayout = findViewById(R.id.editTextPhoneLayout);
        passEditTextLayout = findViewById(R.id.editTextSignUpPassLayout);

        //btnSignup.setEnabled(false);


        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Registration.this, Login.class);startActivity(i) ;
                finish();
            }
        });



        mAuth = FirebaseAuth.getInstance();


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email = userEmail.getText().toString();
                String password = userPass.getText().toString();
                String sname = schoolName.getText().toString();
                String phone = userPhone.getText().toString();


                     if (sname.isEmpty()) {
                         nameEditTextLayout.setError("*Name required");
                         schoolName.requestFocus();

                     }
                     else if (email.isEmpty()) {
                         emailEditTextLayout.setError("*Email required");
                         userEmail.requestFocus();
                     }
                     else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                         emailEditTextLayout.setError("*Enter a valid Email Address");
                         userEmail.requestFocus();
                     }
                     else if (phone.isEmpty()) {
                         phoneEditTextLayout.setError("*Phone required");
                         userPhone.requestFocus();
                     }
                     else if (phone.length() != 11){
                         phoneEditTextLayout.setError("*Minimum length of a Number should be 11");
                         userPhone.requestFocus();
                         return;
                     }
                     else if (!Patterns.PHONE.matcher(phone).matches()){
                         phoneEditTextLayout.setError("*Enter a valid Phone Number");
                         userPhone.requestFocus();
                         return;
                     }
                     else if (password.isEmpty()) {
                         passEditTextLayout.setError("*Password required");
                         userPass.requestFocus();
                     }
                     else if (password.length() < 6){
                         passEditTextLayout.setError("*Minimum length of a Password should be 6");
                         userPass.requestFocus();
                     }
                     else if (!(isNetworkAvaliable(Registration.this))) {
                    Toast.makeText(Registration.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                }
                else if (!(email.isEmpty() && password.isEmpty())) {

                    progressDialog = new ProgressDialog(Registration.this);
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setTitle("Creating Account");
                    progressDialog.setMessage("Please wait a moment...");

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(Registration.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            } else {

                                sendUserData();

                            }
                        }
                    });
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(Registration.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    private void sendUserData() {

        user_id = mAuth.getCurrentUser().getUid();
        current_user_db = FirebaseDatabase.getInstance().getReference().child("UsersData").child(user_id);


        String email = userEmail.getText().toString();
        String sname = schoolName.getText().toString();
        String phone = userPhone.getText().toString();
        String saveCurrentDate, saveCurrentTime;


        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calFordDate.getTime());

        Map reg = new HashMap();

        reg.put("userName", sname);
        reg.put("userEmail", email);
        reg.put("userPhone", phone);
        reg.put("userType", "none");
        reg.put("userBlood", "none");
        reg.put("userImage", " ");
        reg.put("userUID", user_id);
        reg.put("userMembershipTime",saveCurrentTime);
        reg.put("userMembershipDate",saveCurrentDate);

        current_user_db.updateChildren(reg);



        progressDialog.dismiss();
        Toast.makeText(Registration.this, "Registration Successful", Toast.LENGTH_SHORT).show();

        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(Registration.this, Login.class));
        finish();


    }

    public static boolean isNetworkAvaliable(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if ((connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)
                || (connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState() == NetworkInfo.State.CONNECTED)) {
            return true;
        } else {
            return false;
        }
    }
}
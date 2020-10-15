package com.sarker.hellodoctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sarker.hellodoctor.fragment.AdminPanel;
import com.sarker.hellodoctor.fragment.Home;
import com.sarker.hellodoctor.fragment.Profile;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private String current_user_id = " ",from;

    private FrameLayout frameLayout;
    private BottomNavigationView bottomNavigationView;
    private Home homeFragment;
    private Profile profileFragment;
    private AdminPanel adminFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_main);


        Intent i = getIntent();
        from = i.getStringExtra("from");

        //Initialization

        bottomNavigationView = findViewById(R.id.bottom_nav);
        frameLayout = findViewById(R.id.main_fram);


        homeFragment = new Home();
        profileFragment = new Profile();
        adminFragment = new AdminPanel();

        //End

        if (from!=null){

            if (from.equals("addDoctor")){
                setFragment(adminFragment);
            }else if(from.equals("profile")){
                setFragment(profileFragment);
            }

        }else {
            //set default fragment
            setFragment(homeFragment);
        }



        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseuser = mAuth.getCurrentUser();

        if (mFirebaseuser != null) {

            current_user_id = mAuth.getCurrentUser().getUid();

        } else {

            Menu nav_Menu = bottomNavigationView.getMenu();
            nav_Menu.findItem(R.id.nav_admin).setVisible(false);
            nav_Menu.findItem(R.id.nav_profile).setVisible(false);

        }




        userRef = FirebaseDatabase.getInstance().getReference("UsersData").child(current_user_id);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    String type = snapshot.child("userType").getValue().toString();

                    if (type.equals("admin")){

                        Menu nav_Menu = bottomNavigationView.getMenu();
                        nav_Menu.findItem(R.id.nav_admin).setVisible(true);

                    }else {
                        Menu nav_Menu = bottomNavigationView.getMenu();
                        nav_Menu.findItem(R.id.nav_admin).setVisible(false);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){

                    case R.id.nav_home : {
                        //bottomNavigationView.setItemBackgroundResource(R.color.colorPrimary);
                        setFragment(homeFragment);
                        return true;
                    }
                    case R.id.nav_admin : {
                        //bottomNavigationView.setItemBackgroundResource(R.color.colorPrimary);
                        setFragment(adminFragment);
                        return true;
                    }
                    case R.id.nav_profile : {
                        //bottomNavigationView.setItemBackgroundResource(R.color.colorPrimary);
                        setFragment(profileFragment);
                        return true;
                    }
                    default:
                        return false;

                }
            }
        });



    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_fram,fragment);
        fragmentTransaction.commit();
    }

}

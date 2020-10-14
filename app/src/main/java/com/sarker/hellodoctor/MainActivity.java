package com.sarker.hellodoctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.sarker.hellodoctor.fragment.Home;
import com.sarker.hellodoctor.fragment.Profile;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference slideRef;

    private FrameLayout frameLayout;
    private BottomNavigationView bottomNavigationView;
    private Home homeFragment;
    private Profile profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_main);

        //Initialization

        bottomNavigationView = findViewById(R.id.bottom_nav);
        frameLayout = findViewById(R.id.main_fram);

        homeFragment = new Home();
        profileFragment = new Profile();


        //End

        //set default fragment
        setFragment(homeFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){

                    case R.id.nav_home : {
                        //bottomNavigationView.setItemBackgroundResource(R.color.colorPrimary);
                        setFragment(homeFragment);
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

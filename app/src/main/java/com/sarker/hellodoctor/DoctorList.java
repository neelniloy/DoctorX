package com.sarker.hellodoctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sarker.hellodoctor.model.DoctorInfo;
import com.squareup.picasso.Picasso;

public class DoctorList extends AppCompatActivity {

    private FirebaseAuth mfirebaseAuth;
    private DatabaseReference DoctorRef;

    private RecyclerView sRecyclerView;
    private String current_user_id = " ",dept;
    private ImageView back;
    private ImageView empty;
    private EditText search;

    private FirebaseRecyclerAdapter<DoctorInfo,SearchViewHolder> dAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_doctor_list);


        Intent i = getIntent();
        dept = i.getStringExtra("dept");

        mfirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseuser = mfirebaseAuth.getCurrentUser();

        if (mFirebaseuser != null) {

            current_user_id = mfirebaseAuth.getCurrentUser().getUid();

        } else {

        }

        DoctorRef = FirebaseDatabase.getInstance().getReference("DoctorsData").child(dept);

        back = findViewById(R.id.back);
        empty = findViewById(R.id.emptya_data);
        search = findViewById(R.id.search_field);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });

        sRecyclerView = findViewById(R.id.doctor_list_rv);
        sRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerV = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //        linearLayoutManager.setReverseLayout(true);
        //        linearLayoutManager.setStackFromEnd(true);
        sRecyclerView.setLayoutManager(linearLayoutManagerV);

        search.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable mEdit)
            {

                if(search.getText().toString().length()>0){

                    String str = search.getText().toString();

                    StringBuilder capitalizedString = new StringBuilder();
                    String[] splited = str.trim().split("\\s+");

                    for (String string : splited) {
                        String s1 = string.substring(0, 1).toUpperCase();
                        String nameCapitalized = s1 + string.substring(1);

                        capitalizedString.append(nameCapitalized);
                        capitalizedString.append(" ");
                    }

                    searchStudent(capitalizedString.toString().trim());

                }


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after){}

            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        searchStudent(search.getText().toString());



    }

    private void searchStudent(String search) {


        FirebaseRecyclerOptions<DoctorInfo> options =
                new FirebaseRecyclerOptions.Builder<DoctorInfo>()
                        .setQuery(DoctorRef.orderByChild("doctorName").startAt(search).endAt(search+"\uf8ff"),DoctorInfo.class)
                        .build();



        dAdapter = new FirebaseRecyclerAdapter<DoctorInfo, SearchViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SearchViewHolder holder, final int i, @NonNull DoctorInfo search) {

                final String key = dAdapter.getRef(i).getKey();

                holder.Name.setText(search.getDoctorName());

                if (search.getDoctorDegree().length()>30){
                    holder.Degree.setText(search.getDoctorDegree().substring(0,27)+"...");
                }else {
                    holder.Degree.setText(search.getDoctorDegree());
                }



                Picasso.get().load(search.getDoctorImage()).fit().centerInside().placeholder(R.drawable.doctor_icon).into(holder.Image);


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(v.getContext(), DoctorDetails.class);
                        intent.putExtra("doctor_id", key);
                        intent.putExtra("dept", dept);
                        startActivity(intent);
                    }
                });


            }

            @NonNull
            @Override
            public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.doctor_item, parent, false);

                return new SearchViewHolder(view);
            }

            @Override
            public void onDataChanged() {
                // if there are no chat messages, show a view that invites the user to add a message
                empty.setVisibility(
                        dAdapter.getItemCount() == 0 ? View.VISIBLE : View.INVISIBLE
                );
            }

        };




        dAdapter.startListening();
        sRecyclerView.setAdapter(dAdapter);



    }

    //view holder
    public static class SearchViewHolder extends RecyclerView.ViewHolder {

        public TextView Name,Degree;
        public CircleImageView Image;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.doctor_name);
            Degree = itemView.findViewById(R.id.doctor_degree);
            Image = itemView.findViewById(R.id.doctorImage);


        }
    }
}
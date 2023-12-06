package com.example.pro1121_nhom3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pro1121_nhom3.adapter.userAdapter;
import com.example.pro1121_nhom3.model.nguoidung;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {
    private RecyclerView recylerviewuser;
    private ArrayList<nguoidung> listUser;
    private userAdapter user_adapter;
    private boolean userchecked = false;
    TextView tvxemnv;
    CardView btnv;
    ImageView ivMenuBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ivMenuBack = findViewById(R.id.ivMenuBack);
        btnv = findViewById(R.id.btnvuser);
        tvxemnv = findViewById(R.id.tvxemnvuser);

        Intent i = getIntent();
        if(i.getBooleanExtra("nva", false)){
            btnv.setVisibility(View.GONE);
        }

        recylerviewuser = findViewById(R.id.recyclerviewuser);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("nguoidung");
        recylerviewuser.setHasFixedSize(true);
        recylerviewuser.setLayoutManager(new LinearLayoutManager(this));
        listUser = new ArrayList<>();
        user_adapter = new userAdapter(listUser, this);
        recylerviewuser.setAdapter(user_adapter);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listUser.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    nguoidung nd = dataSnapshot.getValue(nguoidung.class);
                    nd.setTendangnhap(dataSnapshot.getKey());
                    if(nd.getRole() == 1) listUser.add(nd);
                }
                user_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!userchecked){
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            listUser.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                nguoidung nd = dataSnapshot.getValue(nguoidung.class);
                                nd.setTendangnhap(dataSnapshot.getKey());
                                if(nd.getRole() == 3) listUser.add(nd);
                            }
                            user_adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    userchecked = true;
                    tvxemnv.setText("All Users");
                    btnv.setBackgroundColor(Color.GRAY);
                }else{
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            listUser.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                nguoidung nd = dataSnapshot.getValue(nguoidung.class);
                                nd.setTendangnhap(dataSnapshot.getKey());
                                if(nd.getRole() == 1) listUser.add(nd);
                            }
                            user_adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    tvxemnv.setText("Employee List");
                    btnv.setBackgroundColor(Color.GREEN);
                    userchecked = false;
                }
            }
        });


        ivMenuBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}
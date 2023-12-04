package com.example.pro1121_nhom3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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
    ImageView ivMenuBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ivMenuBack = findViewById(R.id.ivMenuBack);


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
                    listUser.add(nd);
                }
                user_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
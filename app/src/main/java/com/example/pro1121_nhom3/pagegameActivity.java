package com.example.pro1121_nhom3;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pro1121_nhom3.model.game;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class pagegameActivity extends AppCompatActivity {

    TextView tvten, tvngayph, tvsellcount, tvmota, tvloaigame, tvnph;
    Button btbuy;
    ImageView banner;
    game gameIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagegame);
        tvten = findViewById(R.id.tvtengamepage);
        tvngayph = findViewById(R.id.tvngayphpage);
        tvsellcount = findViewById(R.id.tvsellcountpage);
        tvmota = findViewById(R.id.tvmotapage);
        tvloaigame = findViewById(R.id.tvloaigamepage);
        banner = findViewById(R.id.bannergamepage);
        btbuy = findViewById(R.id.btbuynowpage);
        tvnph =findViewById(R.id.tvnphpage);
        gameIndex = new game();
        SharedPreferences sharedPref = getSharedPreferences("infogame", Context.MODE_PRIVATE);
        String magame = sharedPref.getString("magame", "null");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("game");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren())
                {
                    if(data.getKey().equals(magame))
                    {
                        gameIndex = data.getValue(game.class);
                        tvten.setText(gameIndex.getTengame());
                        tvloaigame.setText("Category: " + gameIndex.getLoaigame().getTenloai());
                        tvngayph.setText(gameIndex.getNgayph());
                        tvmota.setText(gameIndex.getMota());
                        tvnph.setText(gameIndex.getNph());
                        tvsellcount.setText(gameIndex.getSellcount() + " copies sold");
                        Glide.with(pagegameActivity.this).load(gameIndex.getImg()).into(banner);

                        if(gameIndex.getGiaban()==0){
                            btbuy.setText("Free to Play");
                        }else{
                            btbuy.setText((int)gameIndex.getGiaban()+ " VND");
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if(currentUser != null)
                {
                    String userEmail = currentUser.getEmail();

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("nguoidung");
                    Query query = databaseReference.orderByChild("email").equalTo(userEmail);

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                    userSnapshot.getRef().child("cart").push().setValue(magame);
                                    Toast.makeText(pagegameActivity.this, "Đã thêm vào giỏ hàng! Hãy chuyển qua trang giỏ hàng để thanh toán", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Xử lý lỗi nếu có
                        }
                    });
                }
            }
        });


    }


}
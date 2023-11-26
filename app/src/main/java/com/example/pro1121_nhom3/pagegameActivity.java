package com.example.pro1121_nhom3;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pro1121_nhom3.model.game;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class pagegameActivity extends AppCompatActivity {

    TextView tvten, tvngayph, tvsellcount, tvmota, tvloaigame;
    Button btbuy;
    ImageView banner;
    game GameIndex;


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
        GameIndex = new game();
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
                        GameIndex = data.getValue(game.class);
                        tvten.setText(GameIndex.getTengame());
                        tvngayph.setText(GameIndex.getNgayph());
                        tvsellcount.setText("Đã bán " + GameIndex.getSellcount() + " bản");
                        tvmota.setText(GameIndex.getMota());
                        tvloaigame.setText("Category: " + GameIndex.getLoaigame().getTenloai());
                        Glide.with(pagegameActivity.this).load(GameIndex.getImg()).into(banner);
                        if(GameIndex.getGiaban()==0)
                        {
                            btbuy.setText("FREE TO PLAY");
                        }else{
                            btbuy.setText((int)GameIndex.getGiaban() + " VND");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

}
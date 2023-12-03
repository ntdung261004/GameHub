package com.example.pro1121_nhom3;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.example.pro1121_nhom3.databinding.ActivityPagegameBinding;
import com.example.pro1121_nhom3.fragment.cart_Fragment;
import com.example.pro1121_nhom3.model.game;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.Locale;

public class pagegameActivity extends AppCompatActivity {

    TextView tvten, tvngayph, tvsellcount, tvmota, tvloaigame, tvnph, tvlikecount;
    String magame;
    Button btbuy;
    ExtendedFloatingActionButton btback;
    ImageView banner;
    ImageView btlike;
    game gameIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagegame);
        tvten = findViewById(R.id.tvtengamepage);
        tvngayph = findViewById(R.id.tvngayphpage);
        tvlikecount = findViewById(R.id.tvlikecountpagegame);
        tvsellcount = findViewById(R.id.tvsellcountpage);
        tvmota = findViewById(R.id.tvmotapage);
        tvloaigame = findViewById(R.id.tvloaigamepage);
        banner = findViewById(R.id.bannergamepage);
        btbuy = findViewById(R.id.btbuynowpage);
        btback = findViewById(R.id.btBackpagegame);
        btlike = findViewById(R.id.btlikepagegame);
        tvnph =findViewById(R.id.tvnphpage);
        gameIndex = new game();

        checkIfUserHas();
        checkIfUserLiked();


        SharedPreferences sharedPref = getSharedPreferences("infogame", Context.MODE_PRIVATE);
        magame = sharedPref.getString("magame", "null");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("game");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                        tvsellcount.setText((int)gameIndex.getSellcount() + " VND");
                        tvlikecount.setText(gameIndex.getLikecount()+"");
                        Glide.with(pagegameActivity.this).load(gameIndex.getImg()).into(banner);

                        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                        if(gameIndex.getGiaban()==0){
                            btbuy.setText("Free to Play");
                        }else{
                            btbuy.setText(format.format((int)gameIndex.getGiaban()));
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
                                    DatabaseReference gameref = FirebaseDatabase.getInstance().getReference("game");
                                    gameref.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for(DataSnapshot gamesnap : snapshot.getChildren())
                                            {
                                                if(gamesnap.getKey().equals(magame))
                                                {
                                                    game game1 = gamesnap.getValue(game.class);
                                                    game1.setMagame(magame);
                                                    userSnapshot.getRef().child("cart").child(magame).setValue(game1);
                                                    Snackbar.make(findViewById(R.id.pagegameview), "Đã thêm vào giỏ hàng!", Snackbar.LENGTH_LONG).setAction("Thanh toán", new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            Intent intent = new Intent(pagegameActivity.this, MainActivity.class);
                                                            intent.putExtra("okok",1);
                                                            startActivity(intent);
                                                        }
                                                    }).show();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });



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

        btback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if(currentUser != null)
                {
                    String userEmail = currentUser.getEmail();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("nguoidung");
                    Query query = ref.orderByChild("email").equalTo(userEmail);

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                for (DataSnapshot userSnapshot : snapshot.getChildren()){
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("game");
                                    databaseReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for(DataSnapshot gamesnap : snapshot.getChildren())
                                            {
                                                if(gamesnap.getKey().equals(magame))
                                                {
                                                    game game1 = gamesnap.getValue(game.class);
                                                    game1.setMagame(gamesnap.getKey());

                                                    userSnapshot.getRef().child("like_list").child(magame).setValue(game1);

                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    Toast.makeText(pagegameActivity.this, "Đã thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                                    myRef.child(magame).child("likecount").setValue(ServerValue.increment(1));
                                    int likecheck = Integer.parseInt(tvlikecount.getText().toString());
                                    tvlikecount.setText((likecheck+1) + "");
                                    btlike.setImageResource(R.drawable.heart1);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });


    }

    private void checkIfUserLiked()
    {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null)
        {
            String userEmail = currentUser.getEmail();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("nguoidung");
            Query query = ref.orderByChild("email").equalTo(userEmail);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for (DataSnapshot userSnapshot : snapshot.getChildren()){
                            userSnapshot.child("like_list").getRef().addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot likedsnap : snapshot.getChildren())
                                    {
                                        if(likedsnap.getKey().equals(magame))
                                        {
                                            btlike.setEnabled(false);
                                            btlike.setImageResource(R.drawable.heart1);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    private void checkIfUserHas()
    {
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
                            userSnapshot.child("game").getRef().addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot game : snapshot.getChildren())
                                    {
                                        if(magame.equals(game.getKey()))
                                        {
                                            btbuy.setEnabled(false);
                                            btbuy.setText("Đã sở hữu");
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


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

}
package com.example.pro1121_nhom3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import com.example.pro1121_nhom3.adapter.AdminGameAdapter;
import com.example.pro1121_nhom3.adapter.gameLibAdapter;
import com.example.pro1121_nhom3.model.game;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {
    ImageView ivMenuBack;
    FloatingActionButton floatAdd;
    private RecyclerView recyclerviewgame;
    ArrayList<game> listgame;
    private SearchView searchView;
    private AdminGameAdapter adminGameAdapter;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        floatAdd = findViewById(R.id.floatAdd);
        ivMenuBack = findViewById(R.id.ivMenuBack);
        listgame = new ArrayList<>();
        searchView  = findViewById(R.id.searchView);
        recyclerviewgame = findViewById(R.id.recyclerviewgame);
        recyclerviewgame.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<game> options = new FirebaseRecyclerOptions.Builder<game>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("game"), game.class)
                .build();
        adminGameAdapter = new AdminGameAdapter(options, this);
        listgame = new ArrayList<>();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adminGameAdapter.filterByName(newText);
                Log.d("SearchView", "Query text changed to: " + newText);
                adminGameAdapter.filterByName(newText);
                return true;
            }
        });
        recyclerviewgame.setAdapter(adminGameAdapter);
        ivMenuBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        floatAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GameActivity.this, AddGameActivity.class);
                startActivity(i);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        adminGameAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adminGameAdapter.stopListening();
    }

}
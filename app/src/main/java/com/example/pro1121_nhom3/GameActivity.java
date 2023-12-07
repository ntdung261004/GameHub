package com.example.pro1121_nhom3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pro1121_nhom3.adapter.AdminGameAdapter;
import com.example.pro1121_nhom3.model.game;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

public class GameActivity extends AppCompatActivity {
    ImageView ivMenuBack;
    FloatingActionButton floatAdd;
    private RecyclerView recyclerviewgame;
    private SearchView searchView;
    private AdminGameAdapter adminGameAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        floatAdd = findViewById(R.id.floatAdd);
        ivMenuBack = findViewById(R.id.ivMenuBack);
        searchView  = findViewById(R.id.searchView);
        recyclerviewgame = findViewById(R.id.recyclerviewgame);

        recyclerviewgame.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<game> options = new FirebaseRecyclerOptions.Builder<game>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("game"), game.class)
                .build();

        adminGameAdapter = new AdminGameAdapter(options, this);
        recyclerviewgame.setAdapter(adminGameAdapter);

        // Set up SearchView listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("SearchView", "Query text changed to: " + newText);
                adminGameAdapter.getFilter().filter(newText);
                return true;
            }
        });

        // Back button click listener
        ivMenuBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Floating action button click listener
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

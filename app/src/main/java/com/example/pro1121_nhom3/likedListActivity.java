package com.example.pro1121_nhom3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pro1121_nhom3.adapter.gameCartAdapter;
import com.example.pro1121_nhom3.adapter.gameLikedListAdapter;
import com.example.pro1121_nhom3.model.game;

import java.util.ArrayList;

public class likedListActivity extends AppCompatActivity {

    RecyclerView rcvlikedlist;
    Button btback;
    ArrayList<game> likedList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_list);
        rcvlikedlist = findViewById(R.id.rcvlikedlist);
        likedList = new ArrayList<>();
        btback = findViewById(R.id.btbacklikedlist);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvlikedlist.setLayoutManager(linearLayoutManager);
        gameLikedListAdapter likelistAdapter = new gameLikedListAdapter(likedList, this);
        likelistAdapter.getLikedList(likedList);
        rcvlikedlist.setAdapter(likelistAdapter);

        btback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
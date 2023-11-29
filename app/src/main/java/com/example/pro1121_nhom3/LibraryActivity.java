package com.example.pro1121_nhom3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.pro1121_nhom3.adapter.gameCartAdapter;
import com.example.pro1121_nhom3.adapter.gameLibAdapter;
import com.example.pro1121_nhom3.model.game;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class LibraryActivity extends AppCompatActivity {

    RecyclerView rcvLib;
    ArrayList<game> listgame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_da_buy);
        listgame = new ArrayList<>();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(LibraryActivity.this, 4);
        gameLibAdapter libAdapter = new gameLibAdapter(listgame, this);
        libAdapter.getLibList(listgame);
        rcvLib.setAdapter(libAdapter);
        rcvLib.setLayoutManager(gridLayoutManager);
    }
}
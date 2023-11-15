package com.example.pro1121_nhom3.dao;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.pro1121_nhom3.adapter.gameAdapter;
import com.example.pro1121_nhom3.model.game;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class gameDAO {

    Context context;

    public gameDAO(Context context)
    {
        this.context = context;
    }



}

package com.example.pro1121_nhom3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pro1121_nhom3.adapter.AdminGameAdapter;
import com.example.pro1121_nhom3.adapter.userAdapter;
import com.example.pro1121_nhom3.model.game;
import com.example.pro1121_nhom3.model.nguoidung;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {
    ImageView ivMenuBack;
    FloatingActionButton floatAdd;

    private ArrayList<game> listGame;
    private RecyclerView recyclerviewgame;
    private AdminGameAdapter adminGameAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        floatAdd = findViewById(R.id.floatAdd);
        ivMenuBack = findViewById(R.id.ivMenuBack);


        recyclerviewgame = findViewById(R.id.recyclerviewgame);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("game");
        recyclerviewgame.setHasFixedSize(true);
        recyclerviewgame.setLayoutManager(new LinearLayoutManager(this));
        listGame = new ArrayList<>();
        adminGameAdapter = new AdminGameAdapter(listGame, this);
        recyclerviewgame.setAdapter(adminGameAdapter);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    game adminGame = dataSnapshot.getValue(game.class);
                    listGame.add(adminGame);
                }
                adminGameAdapter.notifyDataSetChanged();
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

        floatAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogThem();
            }
        });


    }

    private void showDialogThem(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_adminthemgame,null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.show();

        EditText edtTenGame = view.findViewById(R.id.edtTenGame);
        EditText edtMaLoai = view.findViewById(R.id.edtMaLoai);
        EditText edtNPH = view.findViewById(R.id.edtNPH);
        EditText edtGia = view.findViewById(R.id.edtGia);
        Button btnAdd = view.findViewById(R.id.btnAdd);
        Button btnCancel = view.findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtTenGame.getText().toString();
                edtMaLoai.getText().toString();
                edtNPH.getText().toString();
                edtGia.getText().toString();
            }
        });
    }


}
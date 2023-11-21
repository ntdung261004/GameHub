package com.example.pro1121_nhom3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GameActivity extends AppCompatActivity {
    ImageView ivMenuBack;
    FloatingActionButton floatAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        floatAdd = findViewById(R.id.floatAdd);
        ivMenuBack = findViewById(R.id.ivMenuBack);
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
    }
}
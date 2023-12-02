package com.example.pro1121_nhom3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AdminActivity extends AppCompatActivity {

    ImageView ivAdminGame, ivAdminBill, ivAdminUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        ivAdminUser = findViewById(R.id.ivAdminUser);
        ivAdminGame = findViewById(R.id.ivAdminGame);
        ivAdminBill = findViewById(R.id.ivAdminBill);

        ivAdminUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminActivity.this, UserActivity.class);
                startActivity(i);
            }
        });

        ivAdminBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminActivity.this, hoadonActivity.class);
                startActivity(i);
            }
        });

        ivAdminGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminActivity.this, GameActivity.class);
                startActivity(i);
            }
        });
    }
}
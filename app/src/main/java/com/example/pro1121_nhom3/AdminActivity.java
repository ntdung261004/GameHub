package com.example.pro1121_nhom3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class AdminActivity extends AppCompatActivity {

    LinearLayout luser, lbill, lgame, lpass, lempl, llogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        luser = findViewById(R.id.linerUser);
        lbill = findViewById(R.id.linerbill);
        lgame = findViewById(R.id.linerGAME);
        lpass = findViewById(R.id.linerDoiMK);
        lempl = findViewById(R.id.linearAddNV);
        llogout = findViewById(R.id.linerLogout);

        luser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminActivity.this, UserActivity.class);
                startActivity(i);
            }
        });

        lbill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminActivity.this, hoadonActivity.class);
                startActivity(i);
            }
        });

        lgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminActivity.this, GameActivity.class);
                startActivity(i);
            }
        });


    }
}
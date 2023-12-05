package com.example.pro1121_nhom3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdminActivity extends AppCompatActivity {

    LinearLayout luser, lbill, lgame, lpass, lempl, llogout;
    TextView logout,adminchange;

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
        logout= findViewById(R.id.logout);
        adminchange=findViewById(R.id.adminchange);
        adminchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, changespassword.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSavedCredentials();

                // Navigate back to LoginActivity
                Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                AdminActivity.this.finish();
            }
        });

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
    private void clearSavedCredentials() {
        SharedPreferences sharedPreferences = AdminActivity.this.getSharedPreferences("loginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();  // Clear all saved preferences
        editor.apply();
    }
}
package com.example.pro1121_nhom3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class MenuActivity extends AppCompatActivity {

    Toolbar toolbarMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        toolbarMenu = findViewById(R.id.toolbarMenu);
        setSupportActionBar(toolbarMenu);
        getSupportActionBar().setTitle("MENU");
        getSupportActionBar().setLogo(R.mipmap.avt);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

    }
}
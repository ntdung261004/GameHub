package com.example.pro1121_nhom3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.pro1121_nhom3.adapter.spinnerAdapter;
import com.example.pro1121_nhom3.model.category;
import com.example.pro1121_nhom3.model.game;
import com.example.pro1121_nhom3.model.loaigame;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FilterActivity extends AppCompatActivity {

    Spinner spinnerFilter1, spinnerFilter2;
    RecyclerView rcvFilter;
    ArrayList<loaigame> listLoaiGame;
    ArrayList<game> listGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        spinnerFilter1 = findViewById(R.id.spinnerFilter1);
        spinnerFilter2 = findViewById(R.id.spinnerFilter2);
        rcvFilter = findViewById(R.id.rcvFilter);
        listGame = new ArrayList<>();
        listLoaiGame = new ArrayList<>();

        spinnerAdapter spinnerAdapter = new spinnerAdapter(this, R.layout.itemspinner_selected, listLoaiGame, listGame);
        spinnerFilter1.setAdapter(spinnerAdapter);
        spinnerAdapter.getCategoryGame("Action", listGame);
        spinnerAdapter.getCategory(listLoaiGame);

        spinnerFilter1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
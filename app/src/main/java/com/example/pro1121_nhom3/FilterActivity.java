package com.example.pro1121_nhom3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.pro1121_nhom3.adapter.spinnerAdapter;
import com.example.pro1121_nhom3.model.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FilterActivity extends AppCompatActivity {

    Spinner spinnerFilter1, spinnerFilter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        spinnerFilter1 = findViewById(R.id.spinnerFilter1);
        spinnerFilter2 = findViewById(R.id.spinnerFilter2);

        spinnerAdapter spinnerAdapter = new spinnerAdapter(this, R.layout.itemspinner_selected, getListCategory());
        spinnerFilter1.setAdapter(spinnerAdapter);
        spinnerFilter1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private List<category> getListCategory()
    {
        List<category> list  = new ArrayList<>();
        list.add(new category("haha"));
        list.add(new category("hehe"));

        return list;
    }
}
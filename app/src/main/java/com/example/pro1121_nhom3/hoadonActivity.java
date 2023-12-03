package com.example.pro1121_nhom3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.pro1121_nhom3.adapter.billAdapter;
import com.example.pro1121_nhom3.model.hoadon;

import java.util.ArrayList;
import java.util.Date;

public class hoadonActivity extends AppCompatActivity {

    RecyclerView rcvbill;
    ArrayList<hoadon> hdList;
    ImageView btback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoadon);
        rcvbill = findViewById(R.id.rcvbill);
        hdList = new ArrayList<>();
        btback = findViewById(R.id.ivMenuBackbill);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvbill.setLayoutManager(linearLayoutManager);
        billAdapter billadapter = new billAdapter(hdList, this);
        billadapter.getBillList(hdList);
        rcvbill.setAdapter(billadapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcvbill.addItemDecoration(dividerItemDecoration);

        btback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}
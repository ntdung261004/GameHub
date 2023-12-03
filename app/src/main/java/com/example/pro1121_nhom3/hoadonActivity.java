package com.example.pro1121_nhom3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pro1121_nhom3.adapter.billAdapter;
import com.example.pro1121_nhom3.model.hoadon;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class hoadonActivity extends AppCompatActivity {

    RecyclerView rcvbill;
    ArrayList<hoadon> hdList;
    ImageView btback, btquery;
    EditText etdatefrom, etdateto;
    TextView tvdoanhso, tvdoanhthu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoadon);
        rcvbill = findViewById(R.id.rcvbill);
        hdList = new ArrayList<>();
        btback = findViewById(R.id.ivMenuBackbill);
        etdatefrom = findViewById(R.id.etdatefrombill);
        etdateto = findViewById(R.id.etdatetobill);
        btquery = findViewById(R.id.btquerybill);
        tvdoanhso = findViewById(R.id.tvdoanhsobill);
        tvdoanhthu = findViewById(R.id.tvdoanhthubill);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvbill.setLayoutManager(linearLayoutManager);
        billAdapter billadapter = new billAdapter(hdList, this);
        billadapter.getBillListByDate(hdList, "", "");
        rcvbill.setAdapter(billadapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcvbill.addItemDecoration(dividerItemDecoration);

        DatabaseReference tempRef = FirebaseDatabase.getInstance().getReference("TempData");
        tempRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvdoanhso.setText(snapshot.child("doanhso").getValue(int.class)+"");
                tvdoanhthu.setText(snapshot.child("doanhthu").getValue(int.class)+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btquery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                billadapter.getBillListByDate(hdList, etdatefrom.getText().toString(), etdateto.getText().toString());
            }
        });

    }

}
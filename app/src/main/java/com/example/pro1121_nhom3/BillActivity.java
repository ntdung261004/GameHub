package com.example.pro1121_nhom3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pro1121_nhom3.adapter.BillAdapter;
import com.example.pro1121_nhom3.model.game;
import com.example.pro1121_nhom3.model.hoadon;
import com.example.pro1121_nhom3.viewmodel.FirebaseViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BillActivity extends AppCompatActivity {
    ImageView ivMenuBack;

    private BillAdapter billAdapter;
    private RecyclerView recyclerviewbill;

    private FirebaseViewModel firebaseViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        ivMenuBack = findViewById(R.id.ivMenuBack);
        recyclerviewbill = findViewById(R.id.recyclerviewbill);

//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        DatabaseReference databaseReference = firebaseDatabase.getReference("hoadon");
        recyclerviewbill.setHasFixedSize(true);
        recyclerviewbill.setLayoutManager(new LinearLayoutManager(this));
        billAdapter = new BillAdapter();
        recyclerviewbill.setAdapter(billAdapter);

        firebaseViewModel = new ViewModelProvider(this).get(FirebaseViewModel.class);
        firebaseViewModel.getAllData();
        firebaseViewModel.getBillMutableLiveData().observe(this, new Observer<ArrayList<hoadon>>() {
            @Override
            public void onChanged(ArrayList<hoadon> listBill) {
                billAdapter.setListBill(listBill);
                billAdapter.notifyDataSetChanged();
            }
        });
        firebaseViewModel.getDatabaseErrorMutableLiveData().observe(this, new Observer<DatabaseError>() {
            @Override
            public void onChanged(DatabaseError error) {
                Toast.makeText(BillActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });




//        listBill = new ArrayList<>();
//
//
//        billAdapter = new BillAdapter(this, listBill);
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        recyclerviewbill.setLayoutManager(linearLayoutManager);
//        recyclerviewbill.setAdapter(billAdapter);
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    hoadon hd = dataSnapshot.getValue(hoadon.class);
//                    listBill.add(hd);
//                }
//                billAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });



        ivMenuBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}
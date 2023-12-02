package com.example.pro1121_nhom3.repo;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.pro1121_nhom3.model.game;
import com.example.pro1121_nhom3.model.hoadon;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseRepo {

    private DatabaseReference databaseReference;
    private OnRealTimeDbTaskComplete onRealTimeDbTaskComplete;

    public FirebaseRepo(OnRealTimeDbTaskComplete onRealTimeDbTaskComplete){
        this.onRealTimeDbTaskComplete = onRealTimeDbTaskComplete;
        databaseReference = FirebaseDatabase.getInstance().getReference("hoadon");
    }

    public void getAllData(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<hoadon> listBill = new ArrayList<>();
                for(DataSnapshot hdsnap : snapshot.getChildren()){
                    hoadon hd = hdsnap.getValue(hoadon.class);
                    listBill.add(hd);
                }
                onRealTimeDbTaskComplete.onSuccess(listBill);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onRealTimeDbTaskComplete.onFailure(error);
            }
        });
    }

    public interface OnRealTimeDbTaskComplete{
        void onSuccess(ArrayList<hoadon> listBill);
        void onFailure(DatabaseError error);
    }
}

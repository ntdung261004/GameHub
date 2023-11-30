package com.example.pro1121_nhom3.repo;

import android.util.Log;

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
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("hoadon");
    }

    public void getAllData(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<hoadon> listBill = new ArrayList<>();
                for(DataSnapshot ds : snapshot.getChildren()){
                    hoadon hd = new hoadon();
                    hd.setNguoidung_tendangnhap(ds.child("nguoidung_tendangnhap").getValue(String.class));
                    hd.setNgaymua(ds.child("ngaymua").getValue(String.class));
                    hd.setThanhtien(ds.child("thanhtien").getValue(Float.class));

                    GenericTypeIndicator<ArrayList<game>> genericTypeIndicator =
                            new GenericTypeIndicator<ArrayList<game>>(){};
                    Log.d("MYLOG","list: "+ ds.child("game").getValue());
                    //ds.child("game").toString()
                    hd.setListGame(ds.child("game").getValue(genericTypeIndicator));


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
        void onSuccess(List<hoadon> listBill);
        void onFailure(DatabaseError error);
    }
}

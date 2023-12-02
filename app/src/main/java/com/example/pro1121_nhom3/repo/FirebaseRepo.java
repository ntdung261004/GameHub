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
import java.util.HashMap;
import java.util.Map;

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
                ArrayList<hoadon> listBill = new ArrayList<>();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    hoadon hd = new hoadon();
                    hd.setNguoidung_tendangnhap(ds.child("nguoidung_tendangnhap").getValue(String.class));
                    hd.setNgaymua(ds.child("ngaymua").getValue(String.class));
                    hd.setThanhtien(ds.child("thanhtien").getValue(Float.class));






                    Log.d("MYLOG", "ngdung: " + ds.child("nguoidung_tendangnhap").getValue(String.class));
                    Log.d("MYLOG", "ngdung: " + ds.child("ngaymua").getValue(String.class));
                    Log.d("MYLOG", "ngdung: " + ds.child("thanhtien").getValue(Float.class));
                    Log.d("MYLOG", "list: " + ds.child("game").getValue());


                    //Log.d("MYLOG","ngdung: "+ ds.child("nguoidung_tendangnhap").getValue(String.class));
//                    hd.setNguoidung_tendangnhap(ds.child("nguoidung_tendangnhap").getValue(String.class));
//                    hd.setNgaymua(ds.child("ngaymua").getValue(String.class));
//                    hd.setThanhtien(ds.child("thanhtien").getValue(Float.class));
                    //ArrayList<game> li = ds.child("game").getValue(ArrayList.class);
                    //  Log.d("MYLOG","li: "+ li);

                    GenericTypeIndicator<ArrayList<game>> genericTypeIndicator =
                            new GenericTypeIndicator<ArrayList<game>>(){};
                    try {
                        Log.d("MYLOG","ok: "+ ds.child("game").getValue(genericTypeIndicator));

//                        ArrayList<game> gameList = ds.child("game").getValue(genericTypeIndicator);
//                        hd.setListGame(gameList);
                    }catch (Exception e ){
                        Log.d("MYLOG","fail: "+ e);
                    }
//                    Log.d("MYLOG","list: "+ ds.child("game").getValue());
//                    //ds.child("game").toString()
//                    hd.setListGame(ds.child("game").getValue(genericTypeIndicator));


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

package com.example.pro1121_nhom3.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pro1121_nhom3.R;
import com.example.pro1121_nhom3.model.game;
import com.example.pro1121_nhom3.model.hoadon;
import com.example.pro1121_nhom3.model.nguoidung;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.adminBillViewHolder> {

    private List<hoadon> listBill;

    public void setListBill(List<hoadon> listBill){
        this.listBill = listBill;
    }

    @NonNull
    @Override
    public adminBillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill, parent, false);
        return new adminBillViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull adminBillViewHolder holder, int position) {
        hoadon gameBill = listBill.get(position);

        holder.titleBill.setText(gameBill.getNguoidung_tendangnhap());
        holder.date.setText(gameBill.getNgaymua());
        //holder.tvBillQuantity.setText(listGame.size());
        holder.tvBillTotal.setText(gameBill.getThanhtien()+"đ");


//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        DatabaseReference databaseReference = firebaseDatabase.getReference("game");
        holder.nestedRCV.setHasFixedSize(true);

        holder.nestedRCV.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        BillConAdapter billConAdapter = new BillConAdapter();
        billConAdapter.setListGame(gameBill.getListGame());
        holder.nestedRCV.setAdapter(billConAdapter);
        billConAdapter.notifyDataSetChanged();


//        listGame = new ArrayList<>();
//        BillConAdapter billConAdapter = new BillConAdapter(gameBill.getListGame());
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(c);
//        holder.nestedRCV.setLayoutManager(linearLayoutManager);
//        holder.nestedRCV.setAdapter(billConAdapter);

//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    game g = dataSnapshot.getValue(game.class);
//                    listGame.add(g);
//                }
//                billConAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        if(listBill != null)
        {
            return listBill.size();
        }
        return 0;
    }

    public class adminBillViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgBill;
        private TextView titleBill, date, tvBillQuantity, tvBillTotal;
        private RecyclerView nestedRCV;
        public adminBillViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBill = itemView.findViewById(R.id.profile_image);
            titleBill = itemView.findViewById(R.id.titleBill);
            date = itemView.findViewById(R.id.date);
            tvBillQuantity = itemView.findViewById(R.id.tvBillQuantity);
            tvBillTotal = itemView.findViewById(R.id.tvBillTotal);
            nestedRCV = itemView.findViewById(R.id.nestedRCV);
        }
    }
}

package com.example.pro1121_nhom3.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pro1121_nhom3.R;
import com.example.pro1121_nhom3.model.game;
import com.example.pro1121_nhom3.model.hoadon;
import com.example.pro1121_nhom3.pagegameActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class billAdapter extends RecyclerView.Adapter<billAdapter.gameViewHolder>{
    private List<hoadon> listHoadon;
    private Context context;

    public billAdapter(List<hoadon> listHoadon, Context context)
    {
        this.listHoadon = listHoadon;
        this.context = context;

    }

    @NonNull
    @Override
    public billAdapter.gameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.itembill, parent, false);

        return new billAdapter.gameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull billAdapter.gameViewHolder holder, int position) {
        hoadon hdinex = listHoadon.get(position);
        Glide.with(context).load(hdinex.getNguoidung().getAvatar()).into(holder.avtuser);
        Glide.with(context).load(hdinex.getGame().getImg()).into(holder.bannergame);
        holder.tvtennd.setText(hdinex.getNguoidung().getTennd());
        holder.tvusername.setText(hdinex.getNguoidung().getTendangnhap());
        holder.tvtengame.setText(hdinex.getGame().getTengame());
        holder.tvmagame.setText(hdinex.getGame().getMagame());
        holder.tvngaybill.setText(hdinex.getNgaymua());
        holder.tvthanhtien.setText((int)hdinex.getGame().getGiaban()+"");


    }


    @Override
    public int getItemCount() {
        if(listHoadon != null)
        {
            return listHoadon.size();
        }
        return 0;
    }

    public class gameViewHolder extends RecyclerView.ViewHolder {

        private TextView tvtennd, tvusername, tvtengame, tvmagame, tvngaybill, tvthanhtien;
        private ImageView avtuser, bannergame;

        public gameViewHolder(@NonNull View itemView) {
            super(itemView);
            tvtennd = itemView.findViewById(R.id.tvtenuserbill);
            tvusername = itemView.findViewById(R.id.tvusernamebill);
            tvtengame = itemView.findViewById(R.id.tvtengamebill);
            tvmagame = itemView.findViewById(R.id.tvmagamebill);
            tvngaybill = itemView.findViewById(R.id.tvngaybill);
            tvthanhtien = itemView.findViewById(R.id.tvthanhtoanbill);
            avtuser = itemView.findViewById(R.id.avtuserbill);
            bannergame = itemView.findViewById(R.id.bannergamebill);

        }
    }

    public void getBillList(ArrayList<hoadon> billList)
    {
        DatabaseReference billRef = FirebaseDatabase.getInstance().getReference("hoadon");
        billRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                billList.clear();
                for(DataSnapshot billsnap : snapshot.getChildren())
                {
                    hoadon hoadon1 = billsnap.getValue(hoadon.class);
                    billList.add(hoadon1);
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

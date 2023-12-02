package com.example.pro1121_nhom3.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pro1121_nhom3.R;
import com.example.pro1121_nhom3.model.game;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BillConAdapter extends RecyclerView.Adapter<BillConAdapter.adminBillConViewHolder> {

    private ArrayList<game> listGame;

    public void setListGame(ArrayList<game> listGame){
        this.listGame = listGame;
        this.listGame.removeAll(Collections.singleton(null));
    }

    @NonNull
    @Override
    public adminBillConViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill_con, parent, false);
        return new adminBillConViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull adminBillConViewHolder holder, int position) {
        game gameBillCon = listGame.get(position);

        String imgBillCon= gameBillCon.getImg();
        if (imgBillCon != null && !imgBillCon.isEmpty()) {
            Picasso.get().load(imgBillCon).into(holder.imgBillGame);
        } else {

        }

        holder.tvBillTenGame.setText(gameBillCon.getTengame());
        holder.tvBillGia.setText(gameBillCon.getGiaban()+"đ");

    }

    @Override
    public int getItemCount() {
        if(listGame != null)
        {
            return listGame.size();
        }
        return 0;
    }

    public class adminBillConViewHolder extends RecyclerView.ViewHolder{

        private TextView tvBillTenGame, tvBillGia;
        private ImageView imgBillGame;

        public adminBillConViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBillTenGame = itemView.findViewById(R.id.tvBillTenGame);
            tvBillGia = itemView.findViewById(R.id.tvBillGiaGame);
            imgBillGame = itemView.findViewById(R.id.ivBillCon);
        }
    }
}

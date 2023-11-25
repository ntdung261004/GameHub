package com.example.pro1121_nhom3.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pro1121_nhom3.R;
import com.example.pro1121_nhom3.model.nguoidung;

import java.util.ArrayList;

public class userAdapter extends RecyclerView.Adapter<userAdapter.userViewHolder> {

    private ArrayList<nguoidung> listUser;
    private Context c;

    public userAdapter(ArrayList<nguoidung> listUser, Context c) {
        this.listUser = listUser;
        this.c = c;
    }

    @NonNull
    @Override
    public userViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(c).inflate(R.layout.item_user, parent, false);
        return new userViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull userViewHolder holder, int position) {
        holder.tvUserName.setText(listUser.get(position).getTennd());
        holder.tvUserWallet.setText(String.valueOf(listUser.get(position).getWallet()));
    }

    @Override
    public int getItemCount() {
        if(listUser != null)
        {
            return listUser.size();
        }
        return 0;
    }

    public class userViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUserName, tvUserWallet, tvUserWishlist, tvUserGame;
        public userViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserWallet = itemView.findViewById(R.id.tvUserWallet);
            tvUserWishlist = itemView.findViewById(R.id.tvUserWishlist);
            tvUserGame = itemView.findViewById(R.id.tvUserGame);
        }
    }
}

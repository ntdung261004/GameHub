package com.example.pro1121_nhom3.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pro1121_nhom3.AdminActivity;
import com.example.pro1121_nhom3.LoginActivity;
import com.example.pro1121_nhom3.MainActivity;
import com.example.pro1121_nhom3.R;
import com.example.pro1121_nhom3.model.nguoidung;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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

        nguoidung user = listUser.get(position);
        Glide.with(c).load(user.getAvatar()).into(holder.imgUser);
        holder.tvUserName.setText(user.getTennd());
        holder.tvUserWallet.setText(user.getWallet() + "đ");

        if(user.getRole() == 2)
        {
            holder.backgrounduser.setBackgroundColor(Color.rgb(255,100,100));
        }
        if(user.getRole() == 3)
        {
            holder.backgrounduser.setBackgroundColor(Color.rgb(100,100,255));
        }

        holder.backgrounduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(c);
                builder.setTitle("Xoá User");
                builder.setMessage("Hành động này không thể hoàn tác");

                builder.setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("nguoidung")
                                .child(user.getTendangnhap()).removeValue();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(c, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });


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
        private ImageView imgUser;
        private TextView tvUserName, tvUserWallet;
        private LinearLayout backgrounduser;
        public userViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.user_img);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserWallet = itemView.findViewById(R.id.tvUserWallet);
            backgrounduser = itemView.findViewById(R.id.backgrounduser);
        }
    }
}

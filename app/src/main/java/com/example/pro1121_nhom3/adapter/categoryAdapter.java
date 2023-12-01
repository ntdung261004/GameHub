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
import com.example.pro1121_nhom3.FilterActivity;
import com.example.pro1121_nhom3.R;
import com.example.pro1121_nhom3.model.game;
import com.example.pro1121_nhom3.model.loaigame;
import com.example.pro1121_nhom3.pagegameActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class categoryAdapter extends RecyclerView.Adapter<categoryAdapter.gameViewHolder> {

    private List<loaigame> listLoaiGame;
    private Context context;

    public categoryAdapter(List<loaigame> listLoaiGame, Context context) {
        this.listLoaiGame = listLoaiGame;
        this.context = context;

    }

    @NonNull
    @Override
    public gameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.itemcategorynf, parent, false);
        return new gameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull gameViewHolder holder, int position) {
        holder.tvloaigame.setText(listLoaiGame.get(position).getTenloai());

        holder.tvloaigame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, FilterActivity.class);
                i.putExtra("cateindex", position+1);
                context.startActivity(i);
            }
        });
    }


    @Override
    public int getItemCount() {
        if (listLoaiGame != null) {
            return listLoaiGame.size();
        }
        return 0;
    }

    public class gameViewHolder extends RecyclerView.ViewHolder {

        private TextView tvloaigame;

        public gameViewHolder(@NonNull View itemView) {
            super(itemView);
            tvloaigame = itemView.findViewById(R.id.tvloaigamenf);

        }
    }

    public void getAllCategory(ArrayList<loaigame> listLoaiGame) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("loaigame");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listLoaiGame.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    loaigame loaigame1 = data.getValue(loaigame.class);
                    loaigame1.setMaloai(data.getKey());
                    listLoaiGame.add(loaigame1);
                }

                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}

package com.example.pro1121_nhom3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pro1121_nhom3.R;
import com.example.pro1121_nhom3.model.game;

import java.util.ArrayList;

public class AdminGameAdapter extends RecyclerView.Adapter<AdminGameAdapter.admingameViewHolder> {

    private ArrayList<game> listGame;
    private Context c;

    public AdminGameAdapter(ArrayList<game> listGame, Context c) {
        this.listGame = listGame;
        this.c = c;
    }

    @NonNull
    @Override
    public admingameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.item_game, parent, false);
        return new admingameViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull admingameViewHolder holder, int position) {
        holder.tvAdminTengame.setText(listGame.get(position).getTengame());
        holder.tvAdminLoaigame.setText(listGame.get(position).getLoaigame().getTenloai());
        holder.tvAdminNph.setText(listGame.get(position).getNph());
        holder.tvAdminGiaban.setText(String.valueOf(listGame.get(position).getGiaban()));
    }

    @Override
    public int getItemCount() {
        if(listGame != null)
        {
            return listGame.size();
        }
        return 0;
    }

    public class admingameViewHolder extends RecyclerView.ViewHolder{

        private TextView tvAdminTengame, tvAdminLoaigame, tvAdminNph, tvAdminGiaban;
        public admingameViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAdminTengame = itemView.findViewById(R.id.tvAdminTengame);
            tvAdminLoaigame = itemView.findViewById(R.id.tvAdminLoaigame);
            tvAdminNph = itemView.findViewById(R.id.tvAdminNph);
            tvAdminGiaban = itemView.findViewById(R.id.tvAdminGiaban);
        }
    }
}

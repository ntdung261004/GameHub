package com.example.pro1121_nhom3.adapter;

import android.app.Activity;
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

public class gameAdapter extends RecyclerView.Adapter<gameAdapter.gameViewHolder>{

    private ArrayList<game> listGame;
    private Context context;

    public gameAdapter(ArrayList<game> listGame, Context context)
    {
        this.listGame = listGame;
        this.context = context;
    }

    @NonNull
    @Override
    public gameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_game, parent, false);
        return new gameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull gameViewHolder holder, int position) {
        game gameindex = listGame.get(position);
        if(gameindex == null)
        {
            return;
        }

        holder.tvten.setText(gameindex.getTengame());
        holder.nhaph.setText(gameindex.getNph());
        holder.giaban.setText(gameindex.getGiaban()+"");
        holder.tenloai.setText(gameindex.getLoaigame().getTenloai());


    }

    @Override
    public int getItemCount() {
        if(listGame != null)
        {
            return listGame.size();
        }
        return 0;
    }

    public class gameViewHolder extends RecyclerView.ViewHolder {

        private TextView tvten;
        private TextView nhaph;
        private TextView giaban;
        private TextView tenloai;

        public gameViewHolder(@NonNull View itemView) {
            super(itemView);
            tvten = itemView.findViewById(R.id.tvtengame);
            nhaph = itemView.findViewById(R.id.tvnph);
            giaban = itemView.findViewById(R.id.tvgiaban);
            tenloai = itemView.findViewById(R.id.tvloaigame);
        }
    }
}

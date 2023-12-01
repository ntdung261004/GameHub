package com.example.pro1121_nhom3.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pro1121_nhom3.R;
import com.example.pro1121_nhom3.model.game;

import java.util.ArrayList;

public class gameLibAdapter extends RecyclerView.Adapter<gameLibAdapter.ViewHolder> {

    private ArrayList<game> originalList;
    private ArrayList<game> filteredList;
    private Context context;

    public gameLibAdapter(ArrayList<game> list, Context context) {
        this.originalList = new ArrayList<>(list);
        this.filteredList = new ArrayList<>(list);
        this.context = context;
    }

    public void setOriginalList(ArrayList<game> list) {
        originalList = new ArrayList<>(list);
        filteredList = new ArrayList<>(list);
    }

    public void filter(String query) {
        filteredList.clear();

        if (TextUtils.isEmpty(query)) {
            filteredList.addAll(originalList);
        } else {
            query = query.toLowerCase().trim();
            for (game game : originalList) {
                if (game.getTengame().toLowerCase().contains(query)) {
                    filteredList.add(game);
                }
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlibrary, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        game currentGame = filteredList.get(position);

        // Populate views with game data
        holder.tvtengamelib.setText(currentGame.getTengame());
        holder.tvnphlib.setText(currentGame.getNph());
        Glide.with(context).load(currentGame.getImg()).into(holder.bannergamelib);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bannergamelib;
        TextView tvtengamelib, tvnphlib;

        public ViewHolder(View itemView) {
            super(itemView);
            bannergamelib = itemView.findViewById(R.id.bannergamelib);
            tvtengamelib = itemView.findViewById(R.id.tvtengamelib);
            tvnphlib = itemView.findViewById(R.id.tvnphlib);
        }
    }
}

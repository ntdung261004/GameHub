package com.example.pro1121_nhom3.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pro1121_nhom3.R;
import com.example.pro1121_nhom3.model.search;
import com.example.pro1121_nhom3.pagegameActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class searchAdapter extends RecyclerView.Adapter<searchAdapter.ViewHolder> {

    private List<search> searchList;
    private Context context;

    public searchAdapter(List<search> searchList, Context context) {
        this.searchList = searchList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        search searchItem = searchList.get(position);


        String imgUrl = searchItem.getImg();
        if (imgUrl != null && !imgUrl.isEmpty()) {
            Picasso.get().load(imgUrl).into(holder.search_image);
        } else {

        }

        // Set the game name
        holder.search_name.setText(searchItem.getTengame());

        // Handle item click if needed
        holder.itemView.setOnClickListener(v -> {

        });
        holder.linear_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = context.getSharedPreferences("infogame", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("magame",searchItem.getMagame());
                editor.apply();
                context.startActivity(new Intent(context, pagegameActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView search_image;
        TextView search_name;
        LinearLayout linear_search;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            search_image = itemView.findViewById(R.id.search_image);
            search_name = itemView.findViewById(R.id.searh_name);
            linear_search=itemView.findViewById(R.id.linear_search);
        }
    }
    public void setFilter(List<search> searchList) {
        this.searchList = searchList;
        notifyDataSetChanged();
    }

}

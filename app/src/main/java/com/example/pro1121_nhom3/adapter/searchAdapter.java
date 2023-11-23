package com.example.pro1121_nhom3.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pro1121_nhom3.R;
import com.example.pro1121_nhom3.model.search;

import java.util.ArrayList;
import java.util.List;

public class searchAdapter extends RecyclerView.Adapter<searchAdapter.searchviewHolder> {

    private List<search> listsearch;
    private List<search> originalList;
    private OnItemClickListener listener;

    public searchAdapter(List<search> listsearch) {
        this.listsearch = listsearch;
        this.originalList = new ArrayList<>(listsearch);
    }


    public void filterByName(String name) {
        listsearch.clear();
        if (name.isEmpty()) {
            listsearch.addAll(originalList);
        } else {
            for (search search : originalList) {
                if (search.getName().toLowerCase().contains(name.toLowerCase())) {
                    listsearch.add(search);
                }
            }
        }
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(search item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public searchviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
        return new searchviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull searchviewHolder holder, int position) {
        search search = listsearch.get(position);
        if (search == null) {
            return;
        }
        holder.searh_image.setImageResource(search.getImageResource());
        holder.search_name.setText(search.getName());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listener != null) {
                    listener.onItemClick(search);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listsearch.size();
    }

    public class searchviewHolder extends RecyclerView.ViewHolder {
        private TextView search_name;
        private ImageView searh_image;

        public searchviewHolder(@NonNull View itemView) {
            super(itemView);
            search_name = itemView.findViewById(R.id.searh_name);
            searh_image = itemView.findViewById(R.id.search_image);
        }
    }
}

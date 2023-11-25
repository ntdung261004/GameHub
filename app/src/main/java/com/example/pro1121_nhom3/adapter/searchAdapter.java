package com.example.pro1121_nhom3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pro1121_nhom3.R;
import com.example.pro1121_nhom3.model.game;
import com.example.pro1121_nhom3.model.search;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class searchAdapter extends RecyclerView.Adapter<searchAdapter.searchviewHolder> {

    private List<game> listsearch;
    private List<game> originalList;
    private Context context;
    private OnItemClickListener listener;

    public searchAdapter(List<game> listsearch, Context context) {
        this.listsearch = listsearch;
        this.originalList = new ArrayList<>(listsearch);
        this.context = context;
    }


    public void filterByName(String name) {
        listsearch.clear();
        if (name.isEmpty()) {
            listsearch.addAll(originalList);
        } else {
            for (game search : originalList) {
                if (search.getTengame().toLowerCase().contains(name.toLowerCase())) {
                    listsearch.add(search);
                }
            }
        }
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(game item);
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
        game search = listsearch.get(position);
        if (search == null) {
            return;
        }
        Glide.with(context).load(search.getImg()).into(holder.searh_image);
        holder.search_name.setText(search.getTengame());


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

    public void getAllGame(ArrayList<game> listGame)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("game");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listGame.clear();

                for(DataSnapshot data : snapshot.getChildren())
                {
                    game game1 = data.getValue(game.class);
                    listGame.add(game1);
                }

                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}

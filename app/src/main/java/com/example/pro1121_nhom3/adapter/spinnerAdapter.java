package com.example.pro1121_nhom3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pro1121_nhom3.R;
import com.example.pro1121_nhom3.model.category;
import com.example.pro1121_nhom3.model.game;
import com.example.pro1121_nhom3.model.loaigame;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class spinnerAdapter extends ArrayAdapter {

    private ArrayList<loaigame> dsloaigame;
    private ArrayList<game> dsgame;

    public spinnerAdapter(@NonNull Context context, int resource, ArrayList<loaigame> dsloaigame, ArrayList<game> dsgame) {
        super(context, resource);
        this.dsloaigame = dsloaigame;
        this.dsgame = dsgame;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemspinner_selected, parent, false);
        TextView tvCategory = convertView.findViewById(R.id.tvspinner_sle);

        loaigame loaigame1 = dsloaigame.get(position);
        if(loaigame1 != null)
        {
            tvCategory.setText(loaigame1.getTenloai());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemspinner, parent, false);
        TextView tvCategory = convertView.findViewById(R.id.tv_spinner);

        loaigame loaigame1 = dsloaigame.get(position);
        if(loaigame1 != null)
        {
            tvCategory.setText(loaigame1.getTenloai());
        }
        return convertView;
    }

    public void getCategoryGame(String abc, ArrayList<game> dsgame)
    {
        DatabaseReference gameRef = FirebaseDatabase.getInstance().getReference("game");
        gameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dsgame.clear();
                for(DataSnapshot gamesnap : snapshot.getChildren())
                {
                    game game1 = gamesnap.getValue(game.class);
                    game1.setMagame(gamesnap.getKey());

                    if(game1.getLoaigame().getTenloai().equals(abc))
                    {
                        dsgame.add(game1);
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getCategory(ArrayList<loaigame> ds)
    {
        DatabaseReference loaigameRef = FirebaseDatabase.getInstance().getReference("loaigame");
        loaigameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot loaigamesnap : snapshot.getChildren())
                {
                    loaigame loaigame1 = loaigamesnap.getValue(loaigame.class);
                    loaigame1.setMaloai(loaigamesnap.getKey());

                    ds.add(loaigame1);
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

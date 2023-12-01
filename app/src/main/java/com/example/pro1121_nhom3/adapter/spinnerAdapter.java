package com.example.pro1121_nhom3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pro1121_nhom3.R;
import com.example.pro1121_nhom3.model.game;
import com.example.pro1121_nhom3.model.loaigame;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class spinnerAdapter extends ArrayAdapter<loaigame> {

    public spinnerAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemspinner_selected, parent, false);
        TextView tvCategory = convertView.findViewById(R.id.tvspinner_sle);

        loaigame loaigame1 = getItem(position);
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

        loaigame loaigame1 = getItem(position);
        if(loaigame1 != null)
        {
            tvCategory.setText(loaigame1.getTenloai());

        }
        return convertView;
    }


    public void getCategory(ArrayList<loaigame> dsloaigame)
    {
        DatabaseReference loaigameRef = FirebaseDatabase.getInstance().getReference("loaigame");
        loaigameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dsloaigame.clear();
                dsloaigame.add(new loaigame("vidu1", "All"));
                for(DataSnapshot loaigamesnap : snapshot.getChildren())
                {
                    loaigame loaigame1 = loaigamesnap.getValue(loaigame.class);
                    loaigame1.setMaloai(loaigamesnap.getKey());

                    dsloaigame.add(loaigame1);
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

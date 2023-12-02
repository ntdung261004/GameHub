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
import com.example.pro1121_nhom3.model.loaigame;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class spinnerAdapterAddgame extends ArrayAdapter<loaigame> {
    public spinnerAdapterAddgame(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemspinneraddgame, parent, false);
        TextView tvCategory = convertView.findViewById(R.id.tvspinneraddgame);

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
                for(DataSnapshot loaigamesnap : snapshot.getChildren())
                {
                    loaigame loaigame1 = loaigamesnap.getValue(loaigame.class);
                    loaigame1.setMaloai(loaigamesnap.getKey());

                    dsloaigame.add(loaigame1);
                }
                dsloaigame.add(new loaigame("mm1", "Thêm thể loại mới..."));
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

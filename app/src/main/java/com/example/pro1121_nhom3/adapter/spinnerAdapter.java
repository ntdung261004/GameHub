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

import java.util.List;


public class spinnerAdapter extends ArrayAdapter {


    public spinnerAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemspinner_selected, parent, false);
        TextView tvCategory = convertView.findViewById(R.id.tvspinner_sle);

        category category1 = (category) this.getItem(position);
        if(category1 != null)
        {
            tvCategory.setText(category1.getName());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemspinner, parent, false);
        TextView tvCategory = convertView.findViewById(R.id.tv_spinner);

        category category1 = (category) this.getItem(position);
        if(category1 != null)
        {
            tvCategory.setText(category1.getName());
        }
        return convertView;
    }
}

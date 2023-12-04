package com.example.pro1121_nhom3.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pro1121_nhom3.R;
import com.example.pro1121_nhom3.model.game;
import com.example.pro1121_nhom3.model.hoadon;
import com.example.pro1121_nhom3.pagegameActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class billAdapter extends RecyclerView.Adapter<billAdapter.gameViewHolder>{
    private List<hoadon> listHoadon;
    private Context context;

    public billAdapter(List<hoadon> listHoadon, Context context)
    {
        this.listHoadon = listHoadon;
        this.context = context;

    }

    @NonNull
    @Override
    public billAdapter.gameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.itembill, parent, false);

        return new billAdapter.gameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull billAdapter.gameViewHolder holder, int position) {
        if(listHoadon != null)
        {
            hoadon hdinex = listHoadon.get(position);
            Glide.with(context).load(hdinex.getNguoidung().getAvatar()).into(holder.avtuser);
            Glide.with(context).load(hdinex.getGame().getImg()).into(holder.bannergame);
            holder.tvtennd.setText(hdinex.getNguoidung().getTennd());
            holder.tvusername.setText(hdinex.getNguoidung().getTendangnhap());
            holder.tvtengame.setText(hdinex.getGame().getTengame());
            holder.tvmagame.setText(hdinex.getGame().getMagame());
            holder.tvngaybill.setText(hdinex.getNgaymua());
            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            String thanhtienFormatted = format.format((int)hdinex.getGame().getGiaban());
            holder.tvthanhtien.setText(thanhtienFormatted);

        }


    }


    @Override
    public int getItemCount() {
        if(listHoadon != null)
        {
            return listHoadon.size();
        }
        return 0;
    }

    public class gameViewHolder extends RecyclerView.ViewHolder {

        private TextView tvtennd, tvusername, tvtengame, tvmagame, tvngaybill, tvthanhtien;
        private ImageView avtuser, bannergame;

        public gameViewHolder(@NonNull View itemView) {
            super(itemView);
            tvtennd = itemView.findViewById(R.id.tvtenuserbill);
            tvusername = itemView.findViewById(R.id.tvusernamebill);
            tvtengame = itemView.findViewById(R.id.tvtengamebill);
            tvmagame = itemView.findViewById(R.id.tvmagamebill);
            tvngaybill = itemView.findViewById(R.id.tvngaybill);
            tvthanhtien = itemView.findViewById(R.id.tvthanhtoanbill);
            avtuser = itemView.findViewById(R.id.avtuserbill);
            bannergame = itemView.findViewById(R.id.bannergamebill);

        }
    }


    public void getBillListByDate(ArrayList<hoadon> billList, String datefrom, String dateto)
    {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        DatabaseReference billRef = FirebaseDatabase.getInstance().getReference("hoadon");
        billRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                billList.clear();
                float tongdoanhthu = 0;
                int doanhso = 0;
                for(DataSnapshot billsnap : snapshot.getChildren())
                {
                    Boolean check = true;
                    Date tgianbill = new Date();
                    Date from = new Date();
                    Date to  = new Date();
                    hoadon hoadon1 = billsnap.getValue(hoadon.class);
                    try {
                        tgianbill = format.parse(hoadon1.getNgaymua());
                        if(!datefrom.equals("")) from = format.parse(datefrom);
                        if(!dateto.equals("")) to = format.parse(dateto);
                    } catch (ParseException e) {
                    }

                    if(!datefrom.equals("")){
                        if(tgianbill.compareTo(from) < 0) check = false;
                    }
                    if(!dateto.equals("")){
                        if(tgianbill.compareTo(to) > 0) check = false;
                    }

                    if(check){
                        billList.add(hoadon1);
                        doanhso++;
                        tongdoanhthu += hoadon1.getGame().getGiaban();
                    }

                }
                DatabaseReference tempRef = FirebaseDatabase.getInstance().getReference("TempData");
                tempRef.child("doanhso").setValue(doanhso);
                tempRef.child("doanhthu").setValue(tongdoanhthu);
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

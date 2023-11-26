package com.example.pro1121_nhom3.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pro1121_nhom3.R;
import com.example.pro1121_nhom3.model.game;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminGameAdapter extends RecyclerView.Adapter<AdminGameAdapter.admingameViewHolder> {

    private ArrayList<game> listGame;
    private Context c;

    public AdminGameAdapter(ArrayList<game> listGame, Context c) {
        this.listGame = listGame;
        this.c = c;
    }

    @NonNull
    @Override
    public admingameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.item_game, parent, false);
        return new admingameViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull admingameViewHolder holder, int position) {

        holder.tvAdminTengame.setText(listGame.get(position).getTengame());
        holder.tvAdminLoaigame.setText(listGame.get(position).getLoaigame().getTenloai());
        holder.tvAdminNph.setText(listGame.get(position).getNph());
        holder.tvAdminGiaban.setText(listGame.get(position).getGiaban() + "đ");

        holder.ivSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogSua(listGame.get(holder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        if(listGame != null)
        {
            return listGame.size();
        }
        return 0;
    }

    public class admingameViewHolder extends RecyclerView.ViewHolder{

        private TextView tvAdminTengame, tvAdminLoaigame, tvAdminNph, tvAdminGiaban;

        private ImageView ivSua, ivXoa;
        public admingameViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAdminTengame = itemView.findViewById(R.id.tvAdminTengame);
            tvAdminLoaigame = itemView.findViewById(R.id.tvAdminLoaigame);
            tvAdminNph = itemView.findViewById(R.id.tvAdminNph);
            tvAdminGiaban = itemView.findViewById(R.id.tvAdminGiaban);
            ivSua = itemView.findViewById(R.id.ivSua);
            ivXoa = itemView.findViewById(R.id.ivXoa);
        }
    }

    private void showDialogSua(game trochoi){
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        LayoutInflater layoutInflater = ((Activity)c).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_adminthemgame, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.show();

        TextView txtTieuDe = view.findViewById(R.id.txtTieuDe);
        EditText edtTenGame = view.findViewById(R.id.edtTenGame);
        EditText edtMaLoai = view.findViewById(R.id.edtMaLoai);
        EditText edtNPH = view.findViewById(R.id.edtNPH);
        EditText edtGia = view.findViewById(R.id.edtGia);
        Button btnUpdate = view.findViewById(R.id.btnAdd);
        Button btnCancel = view.findViewById(R.id.btnCancel);

        txtTieuDe.setText("UPDATE A GAME");
        btnUpdate.setText("Update");

        edtTenGame.setText(trochoi.getTengame());
        edtMaLoai.setText(trochoi.getLoaigame().getTenloai());
        edtNPH.setText(trochoi.getNph());
        edtGia.setText(trochoi.getGiaban()+"đ");

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> map = new HashMap<>();
                map.put("tengame",edtTenGame.getText().toString());
                map.put("tenloai",edtMaLoai.getText().toString());
                map.put("nph",edtTenGame.getText().toString());
                map.put("giaban",edtTenGame.getText().toString());

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("game");


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

    }


}

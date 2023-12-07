package com.example.pro1121_nhom3.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pro1121_nhom3.R;
import com.example.pro1121_nhom3.UpdateGameActivity;
import com.example.pro1121_nhom3.model.game;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdminGameAdapter extends FirebaseRecyclerAdapter<game, AdminGameAdapter.admingameViewHolder>{

    private List<game> originalList;
    private List<game> filteredList;
    private Context c;
    public AdminGameAdapter(@NonNull FirebaseRecyclerOptions<game> options, Context c) {
        super(options);
        this.c = c;
        filteredList = new ArrayList<>();
        originalList = new ArrayList<>();
    }
    @Override
    protected void onBindViewHolder(@NonNull admingameViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull game model) {
        String key = getSnapshots().getSnapshot(position).getKey();
        model.setMagame(key);
        String imgGame = model.getImg();
        if (filteredList.size() > 0 && position < filteredList.size()) {
            String keyy = filteredList.get(position).getMagame();
            model.setMagame(key);
        }
        if (imgGame != null && !imgGame.isEmpty()) {
            Picasso.get().load(imgGame).into(holder.imgGame);
        } else {

        }

        holder.tvAdminTengame.setText(model.getTengame());
        holder.tvAdminLoaigame.setText(model.getLoaigame().getTenloai());
        holder.tvAdminNph.setText(model.getNph());
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String giabanFormatted = format.format(model.getGiaban());
        holder.tvAdminGiaban.setText(giabanFormatted);

        holder.ivXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.tvAdminTengame.getContext());
                builder.setTitle("Xoá game");
                builder.setMessage("Hành động này không thể hoàn tác");

                builder.setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("game")
                                .child(getRef(position).getKey()).removeValue();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.tvAdminTengame.getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });

        holder.ivSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(c, UpdateGameActivity.class);
                i.putExtra("magameadmin", model.getMagame());
                i.putExtra("img", model.getImg());
                i.putExtra("tengame", model.getTengame());
                i.putExtra("tenloai", model.getLoaigame().getTenloai());
                i.putExtra("ngayph", model.getNgayph());
                i.putExtra("nph", model.getNph());
                i.putExtra("giaban", model.getGiaban());
                i.putExtra("mota", model.getMota());
                c.startActivity(i);
            }
        });
        // Add the game to the original list
        originalList.add(model);
    }


    @NonNull
    @Override
    public admingameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game, parent, false);
        return new admingameViewHolder(v);
    }

    class admingameViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgGame;
        private TextView tvAdminTengame, tvAdminLoaigame, tvAdminNph, tvAdminGiaban;

        private ImageView ivSua, ivXoa;
        public admingameViewHolder(@NonNull View itemView) {
            super(itemView);
            imgGame = (ImageView) itemView.findViewById(R.id.game_img);
            tvAdminTengame = (TextView) itemView.findViewById(R.id.tvAdminTengame);
            tvAdminLoaigame = (TextView) itemView.findViewById(R.id.tvAdminLoaigame);
            tvAdminNph = (TextView) itemView.findViewById(R.id.tvAdminNph);
            tvAdminGiaban = (TextView) itemView.findViewById(R.id.tvAdminGiaban);
            ivSua = (ImageView) itemView.findViewById(R.id.ivSua);
            ivXoa = (ImageView) itemView.findViewById(R.id.ivXoa);
        }
    }
    public void filterByName(String name) {
        filteredList.clear();
        for (game model : originalList) {
            if (model.getTengame().toLowerCase().contains(name.toLowerCase())) {
                filteredList.add(model);
            }
        }
        notifyDataSetChanged();
    }
}
package com.example.pro1121_nhom3.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pro1121_nhom3.R;
import com.example.pro1121_nhom3.UpdateGameActivity;
import com.example.pro1121_nhom3.model.game;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdminGameAdapter extends FirebaseRecyclerAdapter<game, AdminGameAdapter.admingameViewHolder> implements Filterable {
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
    protected void onBindViewHolder(@NonNull admingameViewHolder holder, int position, @NonNull game model) {
        String key = getRef(position).getKey();
        model.setMagame(key);

        String imgGame = model.getImg();
        if (filteredList.size() > 0 && position < filteredList.size()) {
            String keyy = filteredList.get(position).getMagame();
            model.setMagame(key);
        }

        if (imgGame != null && !imgGame.isEmpty()) {
            Picasso.get().load(imgGame).into(holder.imgGame);
        } else {
            // Xử lý trường hợp ảnh rỗng hoặc null
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
                showDeleteDialog(key);
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
    }

    @NonNull
    @Override
    public admingameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game, parent, false);
        return new admingameViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    class admingameViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgGame;
        private TextView tvAdminTengame, tvAdminLoaigame, tvAdminNph, tvAdminGiaban;
        private ImageView ivSua, ivXoa;

        public admingameViewHolder(@NonNull View itemView) {
            super(itemView);
            imgGame = itemView.findViewById(R.id.game_img);
            tvAdminTengame = itemView.findViewById(R.id.tvAdminTengame);
            tvAdminLoaigame = itemView.findViewById(R.id.tvAdminLoaigame);
            tvAdminNph = itemView.findViewById(R.id.tvAdminNph);
            tvAdminGiaban = itemView.findViewById(R.id.tvAdminGiaban);
            ivSua = itemView.findViewById(R.id.ivSua);
            ivXoa = itemView.findViewById(R.id.ivXoa);
        }
    }

    @Override
    public Filter getFilter() {
        return gameFilter;
    }

    private Filter gameFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<game> filteredResults = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredResults.addAll(originalList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (game item : originalList) {
                    if (item.getTengame().toLowerCase().contains(filterPattern)) {
                        filteredResults.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredResults;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList.clear();
            filteredList.addAll((List<game>) results.values);
            notifyDataSetChanged();
        }
    };

    private void showDeleteDialog(String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Xoá game");
        builder.setMessage("Hành động này không thể hoàn tác");

        builder.setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseDatabase.getInstance().getReference().child("game").child(key).removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(c, "Đã xoá", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(c, "Xoá thất bại", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(c, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }

    // Cập nhật originalList khi dữ liệu thay đổi
    public void setOriginalList(List<game> originalList) {
        this.originalList = originalList;
        notifyDataSetChanged();
    }
}

package com.example.pro1121_nhom3.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.pro1121_nhom3.model.game;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;
import java.util.HashMap;
import java.util.Map;

public class AdminGameAdapter extends FirebaseRecyclerAdapter<game, AdminGameAdapter.admingameViewHolder>{


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AdminGameAdapter(@NonNull FirebaseRecyclerOptions<game> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull admingameViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull game model) {
        String imgGame = model.getImg();
        if (imgGame != null && !imgGame.isEmpty()) {
            Picasso.get().load(imgGame).into(holder.imgGame);
        } else {

        }

        holder.tvAdminTengame.setText(model.getTengame());
        holder.tvAdminLoaigame.setText(model.getLoaigame().getTenloai());
        holder.tvAdminNph.setText(model.getNph());
        holder.tvAdminGiaban.setText(model.getGiaban() + "đ");

        holder.ivXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.tvAdminTengame.getContext());
                builder.setTitle("Are you sure?");
                builder.setMessage("Deleted Game can't be UNDO");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
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

                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.imgGame.getContext())
                        .setContentHolder(new ViewHolder(R.layout.dialog_admin_sua_game))
                        .setExpanded(true, 650)
                        .setCancelable(false)
                        .create();
                View v = dialogPlus.getHolderView();

                TextView txtTieuDe = v.findViewById(R.id.txtTieuDe);
                EditText edtTenGame = v.findViewById(R.id.edtTenGame);
                EditText edtMaLoai = v.findViewById(R.id.edtMaLoai);
                EditText edtNPH = v.findViewById(R.id.edtNPH);
                EditText edtGia = v.findViewById(R.id.edtGia);
                Button btnUpdate = v.findViewById(R.id.btnAdd);
                Button btnCancel = v.findViewById(R.id.btnCancel);

                txtTieuDe.setText("UPDATE A GAME");
                btnUpdate.setText("Update");

                edtTenGame.setText(model.getTengame());
                edtMaLoai.setText(model.getLoaigame().getTenloai());
                edtNPH.setText(model.getNph());
                edtGia.setText(String.valueOf(model.getGiaban()));

                dialogPlus.show();

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String,Object> map = new HashMap<>();
                        map.put("tengame",edtTenGame.getText().toString());
//                        map.put("loaigame",edtMaLoai.getText().toString());
                        map.put("nph",Float.parseFloat(edtGia.getText().toString()));
                        map.put("giaban",edtGia.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("game")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.tvAdminTengame.getContext(), "Game Updated Successfully!", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.tvAdminTengame.getContext(), "Error while Updating", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogPlus.dismiss();
                    }
                });


            }
        });
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

//    private void showDialogSua(game trochoi){
//        AlertDialog.Builder builder = new AlertDialog.Builder(c);
//        LayoutInflater layoutInflater = ((Activity)c).getLayoutInflater();
//        View view = layoutInflater.inflate(R.layout.dialog_adminthemgame, null);
//        builder.setView(view);
//
//        AlertDialog alertDialog = builder.create();
//        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        alertDialog.setCancelable(false);
//        alertDialog.show();
//
//        TextView txtTieuDe = view.findViewById(R.id.txtTieuDe);
//        EditText edtTenGame = view.findViewById(R.id.edtTenGame);
//        EditText edtMaLoai = view.findViewById(R.id.edtMaLoai);
//        EditText edtNPH = view.findViewById(R.id.edtNPH);
//        EditText edtGia = view.findViewById(R.id.edtGia);
//        Button btnUpdate = view.findViewById(R.id.btnAdd);
//        Button btnCancel = view.findViewById(R.id.btnCancel);
//
//        txtTieuDe.setText("UPDATE A GAME");
//        btnUpdate.setText("Update");
//
//        edtTenGame.setText(trochoi.getTengame());
//        edtMaLoai.setText(trochoi.getLoaigame().getTenloai());
//        edtNPH.setText(trochoi.getNph());
//        edtGia.setText(trochoi.getGiaban()+"đ");
//
//        btnUpdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Map<String,Object> map = new HashMap<>();
//                map.put("tengame",edtTenGame.getText().toString());
//                map.put("tenloai",edtMaLoai.getText().toString());
//                map.put("nph",edtTenGame.getText().toString());
//                map.put("giaban",edtTenGame.getText().toString());
//
//                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//                DatabaseReference databaseReference = firebaseDatabase.getReference("game");
//
//
//            }
//        });
//
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                alertDialog.dismiss();
//            }
//        });
//
//    }


}

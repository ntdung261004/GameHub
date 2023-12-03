package com.example.pro1121_nhom3.adapter;

import android.app.Activity;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class gameCartAdapter extends RecyclerView.Adapter<gameCartAdapter.gameViewHolder> {
    private List<game> listGame;
    private Context context;

    public gameCartAdapter(List<game> listGame, Context context)
    {
        this.listGame = listGame;
        this.context = context;

    }

    @NonNull
    @Override
    public gameCartAdapter.gameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.itemcart, parent, false);
        return new gameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull gameCartAdapter.gameViewHolder holder, int position) {
        game gameindex = listGame.get(position);
        if(gameindex == null)
        {
            return;
        }
        Glide.with(context).load(gameindex.getImg()).into(holder.imgCart);
        holder.tvtengame.setText(gameindex.getTengame());

        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        holder.tvgiaban.setText(format.format(gameindex.getGiaban()));

        holder.icdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if(currentUser != null)
                {
                    String userEmail = currentUser.getEmail();

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("nguoidung");
                    Query query = databaseReference.orderByChild("email").equalTo(userEmail);

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                    DatabaseReference ref = userSnapshot.child("cart").getRef();
                                    ref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for(DataSnapshot gamesnap1 : snapshot.getChildren())
                                            {
                                                if(gameindex.getMagame().equals(gamesnap1.getKey()))
                                                {
                                                    DatabaseReference deleteRef = userSnapshot.child("cart").child(gamesnap1.getKey()).getRef();
                                                    deleteRef.removeValue();
                                                }

                                            }

                                            notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Xử lý lỗi nếu có
                        }
                    });
                }
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

    public class gameViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgCart, icdelete;
        private TextView tvtengame, tvgiaban;

        public gameViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCart = itemView.findViewById(R.id.imggamecart);
            icdelete = itemView.findViewById(R.id.icdeletecart);
            tvtengame = itemView.findViewById(R.id.tvtengamecart);
            tvgiaban = itemView.findViewById(R.id.tvgiabancart);

        }
    }

    public void getCartList(ArrayList<game> listGame)
    {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null)
        {
            String userEmail = currentUser.getEmail();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("nguoidung");
            Query query = databaseReference.orderByChild("email").equalTo(userEmail);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            DatabaseReference ref = userSnapshot.child("cart").getRef();
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    listGame.clear();
                                    for(DataSnapshot gamesnap1 : snapshot.getChildren())
                                    {
                                        game game1 = gamesnap1.getValue(game.class);
                                        game1.setMagame(gamesnap1.getKey());

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
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Xử lý lỗi nếu có
                }
            });
        }

    }


}

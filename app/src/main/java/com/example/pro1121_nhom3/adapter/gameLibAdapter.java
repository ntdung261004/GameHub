package com.example.pro1121_nhom3.adapter;

import android.animation.ObjectAnimator;
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

import java.util.ArrayList;
import java.util.List;

public class gameLibAdapter extends RecyclerView.Adapter<gameLibAdapter.gameViewHolder> {
    private List<game> listGame;
    private Context context;

    public gameLibAdapter(List<game> listGame, Context context)
    {
        this.listGame = listGame;
        this.context = context;

    }

    @NonNull
    @Override
    public gameLibAdapter.gameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.itemlibrary, parent, false);
        return new gameLibAdapter.gameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull gameLibAdapter.gameViewHolder holder, int position) {
        game gameindex = listGame.get(position);

        Glide.with(context).load(gameindex.getImg()).into(holder.banner);
        holder.tvtengame.setText(gameindex.getTengame());
        holder.tvnph.setText(gameindex.getNph());

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

        private ImageView banner;
        private TextView tvtengame, tvnph;

        public gameViewHolder(@NonNull View itemView) {
            super(itemView);
            banner = itemView.findViewById(R.id.bannergamelib);
            tvtengame = itemView.findViewById(R.id.tvtengamelib);
            tvnph = itemView.findViewById(R.id.tvnphlib);

        }
    }

    public void getLibList(ArrayList<game> listGame)
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
                            DatabaseReference ref = userSnapshot.child("game").getRef();
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

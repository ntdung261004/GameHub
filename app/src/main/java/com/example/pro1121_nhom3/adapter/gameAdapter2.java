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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class gameAdapter2 extends RecyclerView.Adapter<gameAdapter2.gameViewHolder>{
    private ArrayList<game> listGame;
    private Context context;

    public gameAdapter2(ArrayList<game> listGame, Context context)
    {
        this.listGame = listGame;
        this.context = context;
    }

    @NonNull
    @Override
    public gameAdapter2.gameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.itemgame2, parent, false);
        return new gameAdapter2.gameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull gameAdapter2.gameViewHolder holder, int position) {
        game gameindex = listGame.get(position);
        if(gameindex == null)
        {
            return;
        }
        holder.loaigame.setText(gameindex.getLoaigame().getTenloai());
        holder.nhaph.setText(gameindex.getNph());
        holder.tvten.setText(gameindex.getTengame());
        Glide.with(context).load(gameindex.getImg()).into(holder.banner);
        if(gameindex.getGiaban() !=0) holder.giaban.setText((int)gameindex.getGiaban()+" vnd");
        else holder.giaban.setText("Free to Play");

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

        private TextView tvten;
        private ImageView banner;
        private TextView giaban;
        private TextView nhaph;
        private TextView loaigame;

        public gameViewHolder(@NonNull View itemView) {
            super(itemView);
            tvten = itemView.findViewById(R.id.tvtengame2);
            banner = itemView.findViewById(R.id.ivgame2);
            giaban = itemView.findViewById(R.id.tvgiaban2);
            nhaph = itemView.findViewById(R.id.tvnph2);
            loaigame = itemView.findViewById(R.id.tvloaigame2);
        }
    }

    public void getAllGame(ArrayList<game> listGame)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("game");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listGame.clear();

                for(DataSnapshot data : snapshot.getChildren())
                {
                    game game1 = data.getValue(game.class);
                    listGame.add(game1);
                }

                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void getFreeGame(ArrayList<game> listGame)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("game");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listGame.clear();

                for(DataSnapshot data : snapshot.getChildren())
                {
                    game game1 = data.getValue(game.class);
                    if(game1.getGiaban()== 0) listGame.add(game1);
                }

                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void getTop5BestSellersGame(ArrayList<game> listGame)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("game");
        Query top5seller = myRef.orderByChild("sellcount").limitToFirst(5);

        top5seller.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listGame.clear();

                for(DataSnapshot data : snapshot.getChildren())
                {
                    game game1 = data.getValue(game.class);
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

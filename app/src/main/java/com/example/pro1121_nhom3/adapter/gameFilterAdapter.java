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
import com.example.pro1121_nhom3.pagegameActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class gameFilterAdapter extends RecyclerView.Adapter<gameFilterAdapter.gameViewHolder> {
    private List<game> listGame;
    private Context context;

    public gameFilterAdapter(List<game> listGame, Context context)
    {
        this.listGame = listGame;
        this.context = context;

    }

    @NonNull
    @Override
    public gameFilterAdapter.gameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.itemgamefilter, parent, false);
        return new gameFilterAdapter.gameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull gameFilterAdapter.gameViewHolder holder, int position) {
        game gameindex = listGame.get(position);

        Glide.with(context).load(gameindex.getImg()).into(holder.banner);
        holder.tvtengame.setText(gameindex.getTengame());
        holder.tvloaigame.setText(gameindex.getLoaigame().getTenloai());
        if(gameindex.getGiaban()==0)
        {
            holder.tvgiaban.setText("Free to Play");
        }else{
            holder.tvgiaban.setText((int)gameindex.getGiaban()+" VND");
        }

        holder.cvitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = context.getSharedPreferences("infogame", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("magame", gameindex.getMagame());
                editor.apply();
                context.startActivity(new Intent(context, pagegameActivity.class));
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

        private ImageView banner;
        private TextView tvtengame, tvloaigame, tvgiaban;
        private CardView cvitem;

        public gameViewHolder(@NonNull View itemView) {
            super(itemView);
            banner = itemView.findViewById(R.id.bannergamefilter);
            tvtengame = itemView.findViewById(R.id.tvtengamefilter);
            tvloaigame = itemView.findViewById(R.id.tvloaigamefilter);
            tvgiaban = itemView.findViewById(R.id.tvgiabanfilter);
            cvitem = itemView.findViewById(R.id.cvitemfitler);
        }
    }

    public void getCategoryGame(String abc, String price, ArrayList<game> dsgame, int filter)
    {
        DatabaseReference gameRef = FirebaseDatabase.getInstance().getReference("game");
        Query bestseller = gameRef.orderByChild("sellcount");
        Query mostliked = gameRef.orderByChild("likecount");
        Query pricedown = gameRef.orderByChild("giaban");
        switch (filter)
        {
            case 0 : dothequery(abc, price, dsgame, gameRef); break;
            case 1 : dothequery(abc, price, dsgame, bestseller); break;
            case 2 : dothequery(abc, price, dsgame, mostliked); break;
            case 3 : dothequery(abc, price, dsgame, pricedown); break;
        }

    }

    public void dothequery(String abc, String price, ArrayList<game> dsgame, Query query)
    {
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dsgame.clear();
                for(DataSnapshot gamesnap : snapshot.getChildren())
                {
                    game game1 = gamesnap.getValue(game.class);
                    game1.setMagame(gamesnap.getKey());

                    if(abc.equals("All") && price.equals("all"))
                    {
                        dsgame.add(game1);
                    }else{
                        if(!abc.equals("All")){
                            if(price.equals("all")){
                                if(game1.getLoaigame().getTenloai().equals(abc)) dsgame.add(game1);
                            }
                            if(price.equals("free")){
                                if(game1.getLoaigame().getTenloai().equals(abc) && game1.getGiaban()==0) dsgame.add(game1);
                            }
                            if(price.equals("paid")){
                                if(game1.getLoaigame().getTenloai().equals(abc) && game1.getGiaban()>0) dsgame.add(game1);
                            }
                        }else{
                            if(price.equals("free")) {
                                if(game1.getGiaban() == 0) dsgame.add(game1);
                            }
                            if(price.equals("paid")) {
                                if(game1.getGiaban() > 0) dsgame.add(game1);
                            }
                        }
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



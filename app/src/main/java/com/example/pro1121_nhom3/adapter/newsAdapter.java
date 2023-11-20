package com.example.pro1121_nhom3.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.pro1121_nhom3.R;
import com.example.pro1121_nhom3.model.game;
import com.example.pro1121_nhom3.model.news;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class newsAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<news> list;

    public newsAdapter(Context context, ArrayList<news> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View v = inflater.inflate(R.layout.item_news, container, false);
        ImageView imgNews  = v.findViewById(R.id.imgnews);

        news news1 = list.get(position);
        if(news1 != null)
        {
            Glide.with(context).load(news1.getImg_url()).into(imgNews);
        }

        //add view vào viewgroup
        container.addView(v);
        return v;
    }

    @Override
    public int getCount() {
        if(list != null){
            return list.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public void GetNewsList(ArrayList<news> listNews)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("news_img");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listNews.clear();
                for(DataSnapshot data : snapshot.getChildren())
                {
                    news news1 = new news(data.getValue().toString());
                    listNews.add(news1);
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

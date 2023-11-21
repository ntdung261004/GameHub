package com.example.pro1121_nhom3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.pro1121_nhom3.adapter.searchAdapter;
import com.example.pro1121_nhom3.databinding.ActivityMainBinding;
import com.example.pro1121_nhom3.fragment.cart_Fragment;
import com.example.pro1121_nhom3.fragment.newsfeed_Fragment;
import com.example.pro1121_nhom3.fragment.profile_Fragment;
import com.example.pro1121_nhom3.fragment.wishlist_Fragment;
import com.example.pro1121_nhom3.model.search;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new newsfeed_Fragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            if(item.getItemId() == R.id.nf) replaceFragment(new newsfeed_Fragment());
            else if(item.getItemId() == R.id.wl) replaceFragment(new wishlist_Fragment());
            else if(item.getItemId() == R.id.c) replaceFragment(new cart_Fragment());
            else if(item.getItemId() == R.id.pf) replaceFragment(new profile_Fragment());



            return true;
        });


    }

    private void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.commit();
    }
}
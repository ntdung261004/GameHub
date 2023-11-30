package com.example.pro1121_nhom3.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pro1121_nhom3.LibraryActivity;
import com.example.pro1121_nhom3.R;
import com.example.pro1121_nhom3.adapter.gameLibAdapter;
import com.example.pro1121_nhom3.model.game;

import java.util.ArrayList;

public class wishlist_Fragment extends Fragment {

    RecyclerView rcvLib;
    ArrayList<game> listgame;

    public wishlist_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist_, container, false);
        rcvLib = view.findViewById(R.id.rcvwishlist);
        listgame = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        gameLibAdapter libAdapter = new gameLibAdapter(listgame, getActivity());
        libAdapter.getLibList(listgame);
        rcvLib.setAdapter(libAdapter);
        rcvLib.setLayoutManager(gridLayoutManager);




        return view;
    }
}
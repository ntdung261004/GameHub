package com.example.pro1121_nhom3.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pro1121_nhom3.R;
import com.example.pro1121_nhom3.adapter.gameAdapter;
import com.example.pro1121_nhom3.dao.gameDAO;
import com.example.pro1121_nhom3.model.game;

import java.util.ArrayList;


public class newsfeed_Fragment extends Fragment {



    public newsfeed_Fragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    RecyclerView rcvFreeGame, rcvBestSellers;
    ArrayList<game> listGame1, listGame2;
    gameDAO GameDAO;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newsfeed_, container, false);

        GameDAO = new gameDAO(getActivity());
        rcvFreeGame = view.findViewById(R.id.rcvFreeGame);
        rcvBestSellers = view.findViewById(R.id.rcvBestSellers);
        listGame1 = new ArrayList<>();
        listGame2 = new ArrayList<>();

        InitUI();
        return view;
    }

    private void InitUI()
    {

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        gameAdapter gameAdapter1 = new gameAdapter(listGame1, getActivity());
        gameAdapter1.getFreeGame(listGame1);
        rcvFreeGame.setAdapter(gameAdapter1);
        rcvFreeGame.setLayoutManager(linearLayoutManager1);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        gameAdapter gameAdapter2 = new gameAdapter(listGame2, getActivity());
        gameAdapter2.getTop5BestSellersGame(listGame2);
        rcvBestSellers.setAdapter(gameAdapter2);
        rcvBestSellers.setLayoutManager(linearLayoutManager2);


    }


}
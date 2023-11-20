package com.example.pro1121_nhom3.fragment;

import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.pro1121_nhom3.R;
import com.example.pro1121_nhom3.adapter.gameAdapter;
import com.example.pro1121_nhom3.adapter.newsAdapter;
import com.example.pro1121_nhom3.dao.gameDAO;
import com.example.pro1121_nhom3.model.game;
import com.example.pro1121_nhom3.model.news;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;


public class newsfeed_Fragment extends Fragment {



    public newsfeed_Fragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    RecyclerView rcvFreeGame, rcvBestSellers;
    ArrayList<game> listGame1, listGame2;
    ArrayList<news> newsList;
    gameDAO GameDAO;
    ViewPager newsSlideShow;
    CircleIndicator circleIndicator;
    newsAdapter newsAdapter;
    private Timer timer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newsfeed_, container, false);


        newsSlideShow = view.findViewById(R.id.viewPagerNews);
        circleIndicator = view.findViewById(R.id.circleIndicator);
        GameDAO = new gameDAO(getActivity());
        rcvFreeGame = view.findViewById(R.id.rcvFreeGame);
        rcvBestSellers = view.findViewById(R.id.rcvBestSellers);
        newsList = new ArrayList<>();
        listGame1 = new ArrayList<>();
        listGame2 = new ArrayList<>();


        newsAdapter = new newsAdapter(getActivity(), newsList);
        newsSlideShow.setAdapter(newsAdapter);

        circleIndicator.setViewPager(newsSlideShow);
        newsAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

        newsAdapter.GetNewsList(newsList);

        TabGame();
        autoSliderShow();
        return view;
    }


    private void TabGame()
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

        rcvBestSellers.setFocusable(false);
        //set cho rcv khong the roll rieng duoc
        rcvBestSellers.setNestedScrollingEnabled(false);


    }

    private void autoSliderShow()
    {

       // if(newsList == null || newsList.isEmpty() || newsSlideShow == null) return;
        if(timer == null) timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = newsSlideShow.getCurrentItem();
                        int totalItem = newsList.size()-1;



                        if(currentItem < totalItem) {
                            currentItem++;
                            newsSlideShow.setCurrentItem(currentItem);
                        }else{
                            newsSlideShow.setCurrentItem(0);
                        }

                    }
                });
            }
        }, 0, 5000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(timer != null)
        {
            timer.cancel();
        }
    }
}
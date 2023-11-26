package com.example.pro1121_nhom3.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.pro1121_nhom3.R;
import com.example.pro1121_nhom3.adapter.gameAdapter;
import com.example.pro1121_nhom3.adapter.gameAdapter2;
import com.example.pro1121_nhom3.adapter.newsAdapter;
import com.example.pro1121_nhom3.adapter.searchAdapter;
import com.example.pro1121_nhom3.dao.gameDAO;
import com.example.pro1121_nhom3.model.game;
import com.example.pro1121_nhom3.model.news;
import com.example.pro1121_nhom3.model.search;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class newsfeed_Fragment extends Fragment {

    private RecyclerView rcvFreeGame, rcvBestSellers, rcvAllGame, rcvSearch;
    private ArrayList<game> listGame1, listGame2, listGame3, listGame4;
    private ArrayList<news> newsList;
    private ArrayList<search> searchList;
    private SearchView searchView;
    private gameDAO GameDAO;
    private ViewPager newsSlideShow;
    private CircleIndicator circleIndicator;
    private newsAdapter newsAdapter;
    private searchAdapter searchAdapter;
    private Timer timer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newsfeed_, container, false);

        newsSlideShow = view.findViewById(R.id.viewPagerNews);
        circleIndicator = view.findViewById(R.id.circleIndicator);
        GameDAO = new gameDAO(getActivity());
        rcvFreeGame = view.findViewById(R.id.rcvFreeGame);
        rcvBestSellers = view.findViewById(R.id.rcvBestSellers);
        rcvAllGame = view.findViewById(R.id.rcvAllGame);
        rcvSearch = view.findViewById(R.id.rcvSearch);
        searchView = view.findViewById(R.id.searchView);
        newsList = new ArrayList<>();
        listGame1 = new ArrayList<>();
        listGame2 = new ArrayList<>();
        listGame3 = new ArrayList<>();
        listGame4 = new ArrayList<>();
        searchList = new ArrayList<>();

        newsAdapter = new newsAdapter(getActivity(), newsList);
        newsSlideShow.setAdapter(newsAdapter);
        circleIndicator.setViewPager(newsSlideShow);
        newsAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
        newsAdapter.GetNewsList(newsList);

        searchAdapter = new searchAdapter(searchList, getActivity());
        rcvSearch.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rcvSearch.setAdapter(searchAdapter);
        rcvSearch.setVisibility(View.GONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.isEmpty()) {
                    rcvSearch.setVisibility(View.GONE);
                } else {
                    rcvSearch.setVisibility(View.VISIBLE);

                    filterSearchResults(newText);
                }
                return true;
            }
        });
        TabGame();
        autoSliderShow();
        loadSearchDataFromFirebase();
        return view;
    }

    private void TabGame() {
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

        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        gameAdapter2 gameAdapter3 = new gameAdapter2(listGame3, getActivity());
        gameAdapter3.getAllGame(listGame3);
        rcvAllGame.setAdapter(gameAdapter3);
        rcvAllGame.setLayoutManager(linearLayoutManager3);
        rcvAllGame.setFocusable(false);
        rcvAllGame.setNestedScrollingEnabled(false);
    }

    private void autoSliderShow() {
        if (timer == null) timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(() -> {
                    int currentItem = newsSlideShow.getCurrentItem();
                    int totalItem = newsList.size() - 1;

                    if (currentItem < totalItem) {
                        currentItem++;
                        newsSlideShow.setCurrentItem(currentItem);
                    } else {
                        newsSlideShow.setCurrentItem(0);
                    }
                });
            }
        }, 0, 5000);
    }

    private void loadSearchDataFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("game");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                searchList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DataSnapshot loaiGameSnapshot = snapshot.child("loaigame");
                    String tengame = snapshot.child("tengame").getValue(String.class);
                    String img = snapshot.child("img").getValue(String.class);

                    if (tengame != null && img != null) {
                        searchList.add(new search(tengame, img));
                    }
                }
                searchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void startNewActivity(Class<?> activityClass) {
        Intent intent = new Intent(getActivity(), activityClass);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
    private void filterSearchResults(String query) {

        List<search> filteredList = new ArrayList<>();

        for (search game : searchList) {

            if (game.getTengame().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(game);
            }
        }


        searchAdapter.setFilter(filteredList);
    }

}

package com.example.pro1121_nhom3.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.pro1121_nhom3.BuyGameActivity;
import com.example.pro1121_nhom3.R;
import com.example.pro1121_nhom3.adapter.gameAdapter;
import com.example.pro1121_nhom3.adapter.gameAdapter2;
import com.example.pro1121_nhom3.adapter.newsAdapter;
import com.example.pro1121_nhom3.adapter.searchAdapter;
import com.example.pro1121_nhom3.dao.gameDAO;
import com.example.pro1121_nhom3.model.game;
import com.example.pro1121_nhom3.model.news;
import com.example.pro1121_nhom3.model.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class newsfeed_Fragment extends Fragment {

    private RecyclerView rcvFreeGame, rcvBestSellers, rcvAllGame, rcvSearch;
    private ArrayList<game> listGame1, listGame2, listGame3;
    private ArrayList<news> newsList;
    private gameDAO GameDAO;
    private ViewPager newsSlideShow;
    private CircleIndicator circleIndicator;
    private newsAdapter newsAdapter;
    private Timer timer;
    private searchAdapter searchAdapter;

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

        newsList = new ArrayList<>();
        listGame1 = new ArrayList<>();
        listGame2 = new ArrayList<>();
        listGame3 = new ArrayList<>();

        newsAdapter = new newsAdapter(getActivity(), newsList);
        newsSlideShow.setAdapter(newsAdapter);

        circleIndicator.setViewPager(newsSlideShow);
        newsAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

        newsAdapter.GetNewsList(newsList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvSearch.setLayoutManager(linearLayoutManager);

        searchAdapter = new searchAdapter(getListSearch());
        rcvSearch.setAdapter(searchAdapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rcvSearch.addItemDecoration(itemDecoration);

        rcvSearch.setVisibility(View.GONE); // Set initial visibility to GONE

        searchAdapter.setOnItemClickListener(new searchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(search item) {
                switch (item.getItemId()) {
                    case 1:
                        break;
                    case 2:
                        startNewActivity(BuyGameActivity.class);
                        // Thêm hoạt động tương ứng ở đây
                        break;
                    case 3:
                        break;

                }
            }
        });

        SearchView searchView = view.findViewById(R.id.searchView);

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                rcvSearch.setVisibility(View.GONE);
                return false;
            }
        });
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
                    searchAdapter.filterByName(newText);
                }
                return true;
            }
        });

        TabGame();
        autoSliderShow();
        return view;
    }

    private List<search> getListSearch() {
        List<search> list = new ArrayList<>();
        list.add(new search("adofai", R.drawable.adofai, 1));
        list.add(new search("cs2", R.mipmap.cs2, 2));
        list.add(new search("apex", R.mipmap.apex, 3));
        list.add(new search("naraka", R.mipmap.naraka, 4));
        return list;
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
}

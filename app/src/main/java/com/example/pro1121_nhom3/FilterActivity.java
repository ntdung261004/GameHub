package com.example.pro1121_nhom3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pro1121_nhom3.adapter.gameFilterAdapter;
import com.example.pro1121_nhom3.adapter.gameLibAdapter;
import com.example.pro1121_nhom3.adapter.spinnerAdapter;
import com.example.pro1121_nhom3.model.game;
import com.example.pro1121_nhom3.model.loaigame;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FilterActivity extends AppCompatActivity {

    Spinner spinnerFilter1, spinnerFilter2, spinnerFilter3;
    RecyclerView rcvFilter;
    TextView title;
    ArrayList<loaigame> listLoaiGame;
    ArrayList<game> listGame;

    public String getPrice = "all", getCategory = "All";
    public int filterID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        title = findViewById(R.id.tvtitlefilter);
        spinnerFilter1 = findViewById(R.id.spinnerFilter1);
        spinnerFilter2 = findViewById(R.id.spinnerFilter2);
        spinnerFilter3 = findViewById(R.id.spinnerFilter3);
        rcvFilter = findViewById(R.id.rcvFilter);
        Bundle bundle = getIntent().getExtras();
        listGame = new ArrayList<>();
        listLoaiGame = new ArrayList<>();

        spinnerAdapter spinnerAdapter = new spinnerAdapter(this, R.layout.itemspinner_selected, listLoaiGame);
        spinnerFilter1.setAdapter(spinnerAdapter);
        spinnerAdapter.getCategory(listLoaiGame);

        spinnerAdapter spinnerAdapter2 = new spinnerAdapter(this, R.layout.itemspinner_selected, getListSpinner2());
        spinnerFilter2.setAdapter(spinnerAdapter2);

        spinnerAdapter spinnerAdapter3 = new spinnerAdapter(this, R.layout.itemspinner_selected, getListSpinner3());
        spinnerFilter3.setAdapter(spinnerAdapter3);


        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rcvFilter.setLayoutManager(layoutManager);
        gameFilterAdapter rcvAdapter = new gameFilterAdapter(listGame, this);
        rcvFilter.setAdapter(rcvAdapter);

        int cateindex = bundle.getInt("cateindex", -1);
        if(cateindex != -1) spinnerFilter1.setSelection(cateindex);
        int priceindex = bundle.getInt("priceindex", -1);
        if(priceindex != -1) spinnerFilter2.setSelection(priceindex);
        int filterindex = bundle.getInt("filterindex", 0);
        if(filterindex != 0) spinnerFilter3.setSelection(filterindex);

        spinnerFilter1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getCategory = spinnerAdapter.getItem(position).getTenloai();
                rcvAdapter.getCategoryGame(spinnerAdapter.getItem(position).getTenloai(), getPrice,listGame, filterID);
                title.setText(spinnerAdapter.getItem(position).getTenloai());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerFilter2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0 : getPrice = "all"; break;
                    case 1 : getPrice = "free"; break;
                    case 2 : getPrice = "paid"; break;
                }
                rcvAdapter.getCategoryGame(getCategory, getPrice, listGame, filterID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerFilter3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterID = position;
                rcvAdapter.getCategoryGame(getCategory, getPrice, listGame, filterID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private List<loaigame> getListSpinner2(){
        List ds = new ArrayList();
        ds.add(new loaigame("vidu0", "All"));
        ds.add(new loaigame("vidu1", "Free"));
        ds.add(new loaigame("vidu2", "Paid"));

        return ds;
    }

    private List<loaigame> getListSpinner3(){
        List ds = new ArrayList();
        ds.add(new loaigame("vidu0", "None Filter"));
        ds.add(new loaigame("vidu1", "Best Seller"));
        ds.add(new loaigame("vidu3", "Most Liked"));
        ds.add(new loaigame("vidu2", "Price Down"));
        ds.add(new loaigame("vidu3", "Price Up"));

        return ds;
    }

}
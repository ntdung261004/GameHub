package com.example.pro1121_nhom3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pro1121_nhom3.adapter.spinnerAdapterAddgame;
import com.example.pro1121_nhom3.model.loaigame;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UpdateGameActivity extends AppCompatActivity {

    EditText eturl, etten, etnph, etgiaban, etmota, ettenloaimoi, etngayph;
    ImageView bannerpreview;
    Spinner spinnerloaigame;
    Button btpreview, btupdate, btcancel, btaddloaimoi, btgetngayph;
    LinearLayout linearaddloai;
    ArrayList<loaigame> loaigameList;
    public String maloai, tenloai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_game);

        eturl = findViewById(R.id.eturlbannerupdategame);
        etten = findViewById(R.id.ettengameupdategame);
        etnph = findViewById(R.id.etnphupdategame);
        etmota = findViewById(R.id.etmotaupdategame);
        etgiaban = findViewById(R.id.etgiabanupdategame);
        etngayph = findViewById(R.id.etngayphupdategame);
        ettenloaimoi = findViewById(R.id.etnewtenloaiupdategame);
        bannerpreview = findViewById(R.id.bannerpreupdategame);
        spinnerloaigame = findViewById(R.id.spinnerloaigameupdategame);
        linearaddloai = findViewById(R.id.linearaddloaiupdategame);
        btaddloaimoi = findViewById(R.id.btaddloaiupdategame);
        btgetngayph = findViewById(R.id.btgetngayph);
        btupdate = findViewById(R.id.btupdategame);
        btcancel = findViewById(R.id.btcancelupdategame);
        btpreview = findViewById(R.id.btpreviewupdategame);
        loaigameList = new ArrayList<>();

        linearaddloai.setVisibility(View.GONE);

        spinnerAdapterAddgame spinAdapter = new spinnerAdapterAddgame(this, R.layout.itemspinner, loaigameList);
        spinnerloaigame.setAdapter(spinAdapter);
        spinAdapter.getCategory(loaigameList);


//        // Nhận Intent từ activity gửi
//        Intent intent = getIntent();
//
//        // Kiểm tra xem có dữ liệu được đính kèm hay không
//        if (intent != null) {
//            // Lấy dữ liệu từ Intent
//            String hinhgame = intent.getStringExtra("img");
//            String tengame = intent.getStringExtra("tengame");
////            String loaigame = intent.getStringExtra("");
//            String ngayph = intent.getStringExtra("ngayph");
//            String nph = intent.getStringExtra("nph");
//            String mota = intent.getStringExtra("mota");
//            float gia = intent.getFloatExtra("giaban", 0);


            btpreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Glide.with(UpdateGameActivity.this).load(eturl.getText().toString()).into(bannerpreview);
                }
            });

            spinnerloaigame.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (spinAdapter.getItem(position).getMaloai().equals("mm1")) {
                        linearaddloai.setVisibility(View.VISIBLE);
                    } else {
                        maloai = spinAdapter.getItem(position).getMaloai();
                        tenloai = spinAdapter.getItem(position).getTenloai();
                        linearaddloai.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            btaddloaimoi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference loaigameRef = FirebaseDatabase.getInstance().getReference("loaigame");
                    loaigameRef.push().child("tenloai").setValue(ettenloaimoi.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            for (int i = 0; i < spinAdapter.getCount(); i++) {
                                if (spinAdapter.getItem(i).getTenloai().equals(ettenloaimoi.getText().toString())) {
                                    spinnerloaigame.setSelection(i);
                                    linearaddloai.setVisibility(View.GONE);
                                    ettenloaimoi.setText("");
                                    Toast.makeText(UpdateGameActivity.this, "Đã thêm thể loại mới", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                        }
                    });
                }
            });

            btcancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

        }
    }

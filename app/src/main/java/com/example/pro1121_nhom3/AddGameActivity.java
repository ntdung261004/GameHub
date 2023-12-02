package com.example.pro1121_nhom3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
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
import com.example.pro1121_nhom3.model.game;
import com.example.pro1121_nhom3.model.loaigame;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddGameActivity extends AppCompatActivity {

    EditText eturl, etten, etnph, etgiaban, etmota, ettenloaimoi, etngayph;
    ImageView bannerpreview;
    Spinner spinnerloaigame;
    Button btpreview, btadd, btcancel, btaddloaimoi, btgetngayph;
    LinearLayout linearaddloai;
    ArrayList<loaigame> loaigameList;
    public String maloai, tenloai;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);
        eturl = findViewById(R.id.eturlbanneraddgame);
        etten = findViewById(R.id.ettengameaddgame);
        etnph = findViewById(R.id.etnphaddgame);
        etmota = findViewById(R.id.etmotaaddgame);
        etgiaban = findViewById(R.id.etgiabanaddgame);
        etngayph = findViewById(R.id.etngayphaddgame);
        ettenloaimoi = findViewById(R.id.etnewtenloaiaddgame);
        bannerpreview = findViewById(R.id.bannerpreaddgame);
        spinnerloaigame = findViewById(R.id.spinnerloaigameaddgame);
        linearaddloai = findViewById(R.id.linearaddloaiaddgame);
        btaddloaimoi = findViewById(R.id.btaddloaiaddgame);
        btgetngayph = findViewById(R.id.btgetngayph);
        btadd = findViewById(R.id.btaddaddgame);
        btcancel = findViewById(R.id.btcanceladdgame);
        btpreview = findViewById(R.id.btpreviewaddgame);
        loaigameList = new ArrayList<>();

        linearaddloai.setVisibility(View.GONE);

        spinnerAdapterAddgame spinAdapter = new spinnerAdapterAddgame(this, R.layout.itemspinner, loaigameList);
        spinnerloaigame.setAdapter(spinAdapter);
        spinAdapter.getCategory(loaigameList);

        btpreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(AddGameActivity.this).load(eturl.getText().toString()).into(bannerpreview);
            }
        });

        spinnerloaigame.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinAdapter.getItem(position).getMaloai().equals("mm1"))
                {
                    linearaddloai.setVisibility(View.VISIBLE);
                }else{
                    maloai = spinAdapter.getItem(position).getMaloai();
                    tenloai = spinAdapter.getItem(position).getTenloai();
                    linearaddloai.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btgetngayph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String homnay = df.format(c);
                etngayph.setText(homnay);
            }
        });
        btaddloaimoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference loaigameRef = FirebaseDatabase.getInstance().getReference("loaigame");
                loaigameRef.push().child("tenloai").setValue(ettenloaimoi.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        for(int i = 0; i < spinAdapter.getCount(); i++)
                        {
                            if(spinAdapter.getItem(i).getTenloai().equals(ettenloaimoi.getText().toString()))
                            {
                                spinnerloaigame.setSelection(i);
                                linearaddloai.setVisibility(View.GONE);
                                ettenloaimoi.setText("");
                                Toast.makeText(AddGameActivity.this, "Đã thêm thể loại mới", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }
                });
            }
        });

        btadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference gameRef = FirebaseDatabase.getInstance().getReference("game");


                String tengame = etten.getText().toString();
                String nph = etnph.getText().toString();
                String ngayph = etngayph.getText().toString();
                float gia = Float.parseFloat(etgiaban.getText().toString());
                String mota = etmota.getText().toString();
                String img = eturl.getText().toString();
                loaigame loaigame1 = new loaigame(maloai, tenloai);

                game newgame = new game(tengame, nph, gia, img, ngayph, loaigame1, mota);
                gameRef.push().setValue(newgame).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AddGameActivity.this, "Đã thêm thành công game mới!", Toast.LENGTH_SHORT).show();
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
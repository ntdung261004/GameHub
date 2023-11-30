package com.example.pro1121_nhom3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pro1121_nhom3.model.loaigame;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ThemGameActivity extends AppCompatActivity {

    private ArrayList<loaigame> listLoaiGame;

    private ImageView ivSanPhamMoi;
    private Spinner SpnMaLoai;
    private EditText edtTenGame, edtNPH, edtGia, edtNgayPH, edtLikeCount, edtSellCount, edtMoTa;
    private Button btnAdd, btnCancel, btnThemHinhAnhSP;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_game);

        ImageView ivSanPhamMoi = findViewById(R.id.ivSanPhamMoi);
        Button btnThemHinhAnhSP = findViewById(R.id.btnThemHinhAnhSP);
        EditText edtTenGame = findViewById(R.id.edtTenGame);
        Spinner SpnMaLoai = findViewById(R.id.SpnMaLoai);
        EditText edtNPH = findViewById(R.id.edtNPH);
        EditText edtGia = findViewById(R.id.edtGia);
        EditText edtNgayPH = findViewById(R.id.edtNgayPH);
        EditText edtMoTa = findViewById(R.id.edtMoTa);
        EditText edtLikeCount = findViewById(R.id.edtLikeCount);
        EditText edtSellCount = findViewById(R.id.edtSellCount);

        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnCancel = findViewById(R.id.btnCancel);


//        databaseReference = FirebaseDatabase.getInstance().getReference();
//        fetchLoaiGameFromFirebase();

//        ArrayList<String> categoryNames = new ArrayList<>();
//        for (loaigame category : listLoaiGame) {
//            categoryNames.add(category.getTenloai());
//        }
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        SpnMaLoai.setAdapter(adapter);
//
//        SpnMaLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                loaigame selectedCategory = listLoaiGame.get(position);
//                Toast.makeText(ThemGameActivity.this, "Selected category: " + selectedCategory.getTenloai(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // Do nothing here
//            }
//        });


        // Firebase reference to your "loaigame" node
        databaseReference = FirebaseDatabase.getInstance().getReference("loaigame");

        // Fetch data from Firebase
        fetchDataFromFirebase();

        // Set up a listener for Spinner item selection
        SpnMaLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected item here
                loaigame selectedCategory = listLoaiGame.get(position);
                Toast.makeText(ThemGameActivity.this, "Selected category: " + selectedCategory.getTenloai(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });



        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void fetchDataFromFirebase() {
        listLoaiGame = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listLoaiGame.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    loaigame category = snapshot.getValue(loaigame.class);
                    listLoaiGame.add(category);
                }

                // Populate Spinner with categories
                populateSpinner();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors here
                Toast.makeText(ThemGameActivity.this, "Failed to fetch data from Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateSpinner() {
        ArrayList<String> categoryNames = new ArrayList<>();
        for (loaigame category : listLoaiGame) {
            categoryNames.add(category.getTenloai());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpnMaLoai.setAdapter(adapter);
    }

}

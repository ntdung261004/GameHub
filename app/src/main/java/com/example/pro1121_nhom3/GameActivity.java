package com.example.pro1121_nhom3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.pro1121_nhom3.adapter.AdminGameAdapter;
import com.example.pro1121_nhom3.model.game;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GameActivity extends AppCompatActivity {
    ImageView ivMenuBack;
    FloatingActionButton floatAdd;
    private RecyclerView recyclerviewgame;
    private AdminGameAdapter adminGameAdapter;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        floatAdd = findViewById(R.id.floatAdd);
        ivMenuBack = findViewById(R.id.ivMenuBack);


        recyclerviewgame = findViewById(R.id.recyclerviewgame);
        recyclerviewgame.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<game> options = new FirebaseRecyclerOptions.Builder<game>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("game"), game.class)
                .build();

        adminGameAdapter = new AdminGameAdapter(options, this);
        recyclerviewgame.setAdapter(adminGameAdapter);




        ivMenuBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        floatAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GameActivity.this, AddGameActivity.class);
                startActivity(i);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        adminGameAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adminGameAdapter.stopListening();
    }

//    private void showDialogThem() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = getLayoutInflater();
//        View view = inflater.inflate(R.layout.dialog_admin_them_game, null);
//        builder.setView(view);
//
//        AlertDialog alertDialog = builder.create();
//        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        alertDialog.setCancelable(false);
//
//
//        ImageView ivSanPhamMoi = view.findViewById(R.id.ivSanPhamMoi);
//        Button btnThemHinhAnhSP = view.findViewById(R.id.btnThemHinhAnhSP);
//        EditText edtTenGame = view.findViewById(R.id.edtTenGame);
//        Spinner SpnMaLoai = view.findViewById(R.id.SpnMaLoai);
//        EditText edtNPH = view.findViewById(R.id.edtNPH);
//        EditText edtGia = view.findViewById(R.id.edtGia);
//        EditText edtNgayPH = view.findViewById(R.id.edtNgayPH);
//        EditText edtMoTa = view.findViewById(R.id.edtMoTa);
//        EditText edtLikeCount = view.findViewById(R.id.edtLikeCount);
//        EditText edtSellCount = view.findViewById(R.id.edtSellCount);
//
//        Button btnAdd = view.findViewById(R.id.btnAdd);
//        Button btnCancel = view.findViewById(R.id.btnCancel);
//
//        ArrayList<String> categoryNames = new ArrayList<>();
//        for (loaigame category : listLoaiGame) {
//            categoryNames.add(category.getTenloai());
//        }
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        SpnMaLoai.setAdapter(adapter);
//        SpnMaLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                loaigame selectedCategory = listLoaiGame.get(position);
//                Toast.makeText(GameActivity.this, "Selected category: " + selectedCategory.getTenloai(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // Do nothing here
//            }
//        });
//        alertDialog.show();
//
//
//
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                alertDialog.dismiss();
//            }
//        });
//
//
//
//        DatabaseReference root = FirebaseDatabase.getInstance().getReference("game");
//        StorageReference sReference = FirebaseStorage.getInstance().getReference();
//
//
//
//
//        btnAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Map<String,Object> map = new HashMap<>();
//                map.put("tengame",edtTenGame.getText().toString());
//                map.put("loaigame",SpnMaLoai.getSelectedItem().toString());
//                map.put("nph", edtNPH.getText().toString());
//                map.put("giaban", edtGia.getText().toString());
//                map.put("ngayph", edtNgayPH.getText().toString());
//                map.put("mota", edtMoTa.getText().toString());
//                map.put("likecount", edtLikeCount.getText().toString());
//                map.put("sellcount", edtSellCount.getText().toString());
//
//                FirebaseDatabase.getInstance().getReference().child("game").push()
//                        .setValue(map)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                Toast.makeText(GameActivity.this, "Game Added Successfully!", Toast.LENGTH_SHORT).show();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(GameActivity.this, "Error while Adding", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//            }
//        });
//
//    }


}


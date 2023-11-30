package com.example.pro1121_nhom3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pro1121_nhom3.model.loaigame;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ThemGameActivity extends AppCompatActivity {

    private ArrayList<loaigame> listLoaiGame;

    private ImageView ivSanPhamMoi;
    private Spinner SpnMaLoai;
    private EditText edtTenGame, edtNPH, edtGia, edtNgayPH, edtLikeCount, edtSellCount, edtMoTa;
    private Button btnAdd, btnCancel, btnThemHinhAnhSP;
    private DatabaseReference databaseReference;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("game");
    private StorageReference sReference = FirebaseStorage.getInstance().getReference();

    private Uri imageURI;
    String img = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_game);

        ivSanPhamMoi = findViewById(R.id.ivSanPhamMoi);
        btnThemHinhAnhSP = findViewById(R.id.btnThemHinhAnhSP);
        edtTenGame = findViewById(R.id.edtTenGame);
        SpnMaLoai = findViewById(R.id.SpnMaLoaiActivity);
        edtNPH = findViewById(R.id.edtNPH);
        edtGia = findViewById(R.id.edtGia);
        edtNgayPH = findViewById(R.id.edtNgayPH);
        edtMoTa = findViewById(R.id.edtMoTa);
        edtLikeCount = findViewById(R.id.edtLikeCount);
        edtSellCount = findViewById(R.id.edtSellCount);

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


        btnThemHinhAnhSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> map = new HashMap<>();

                map.put("tengame",edtTenGame.getText().toString());
                map.put("loaigame",SpnMaLoai.getSelectedItem().toString());
                map.put("nph", edtNPH.getText().toString());
                map.put("giaban", edtGia.getText().toString());
                map.put("ngayph", edtNgayPH.getText().toString());
                map.put("mota", edtMoTa.getText().toString());
                map.put("likecount", edtLikeCount.getText().toString());
                map.put("sellcount", edtSellCount.getText().toString());

                FirebaseDatabase.getInstance().getReference().child("game").push()
                        .setValue(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(ThemGameActivity.this, "Game Added Successfully!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ThemGameActivity.this, "Error while Adding", Toast.LENGTH_SHORT).show();
                            }
                        });
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
        try {
            SpnMaLoai.setAdapter(adapter);
        }
        catch (Exception e){
            //log de biet bi loi gi
            Log.d("MYLOG","Loi: "+ e);
        }
    }

    private void choosePicture() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // inten tich hop 2 intenr tren 1  hop
        Intent tuychon = Intent.createChooser(i, "chọn 1 mục");
        tuychon.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{camera});
        startActivityForResult(tuychon,999);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==999 && resultCode==RESULT_OK){
            if (data.getExtras() != null) {// lay tu camera
                Bundle b = data.getExtras();
                Bitmap bm = (Bitmap) b.get("data");
                ivSanPhamMoi.setImageBitmap(bm);
                uploadBitmap(bm);
            }else {
                imageURI = data.getData();
                ivSanPhamMoi.setImageURI(imageURI);
                uploadPicture();
            }
        }
    }

    private void uploadBitmap(Bitmap bm) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Đang tải...");
        dialog.show();
        final long id = System.currentTimeMillis();
        StorageReference river = sReference.child("image/"+id);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        river.putBytes(byteArray)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        river.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                dialog.dismiss();
                                img = uri+"";
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Tải không thành công.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        dialog.setMessage("Đang tải... ");
                    }
                });
    }

    private void uploadPicture() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Đang tải...");
        dialog.show();
        final String randomKey = UUID.randomUUID().toString();
        StorageReference river = sReference.child("image/*"+randomKey);

        river.putFile(imageURI)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        dialog.dismiss();
                        river.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                img = uri+"";
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Tải không thành công.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        dialog.setMessage("Đang tải... ");
                    }
                });
    }

    private void clearAll(){
        edtTenGame.setText("");
        edtNPH.setText("");
        edtMoTa.setText("");
        edtGia.setText("");
        edtNgayPH.setText("");
        edtSellCount.setText("");
        edtLikeCount.setText("");
        ivSanPhamMoi.setImageResource(R.mipmap.auto);
    }

}

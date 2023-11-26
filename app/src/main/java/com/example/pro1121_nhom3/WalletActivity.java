package com.example.pro1121_nhom3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class WalletActivity extends AppCompatActivity {

    private TextView sodu;
    private EditText nhaptien;
    private CardView momo_cardview;
    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        sodu = findViewById(R.id.sodu);
        nhaptien = findViewById(R.id.nhaptien);
        momo_cardview = findViewById(R.id.momo_cardview);
        btnBack=findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Gọi hàm để lấy và hiển thị giá trị ví của người dùng
        fetchUserWallet();

        momo_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCustomAmount(); // Xử lý số tiền nhập tùy ý
            }
        });
    }

    private void fetchUserWallet() {
        // Lấy thông tin người dùng hiện tại
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("nguoidung");
            Query query = databaseReference.orderByChild("email").equalTo(userEmail);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            int userWallet = userSnapshot.child("wallet").getValue(Integer.class);
                            updateWalletUI(userWallet);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Xử lý lỗi nếu có
                }
            });
        }
    }

    private void updateWalletUI(int userWallet) {
        // Cập nhật giao diện người dùng với số tiền ví
        sodu.setText(String.valueOf(userWallet));
    }

    private void handleCustomAmount() {
        // Lấy số tiền từ EditText
        String inputAmount = nhaptien.getText().toString();

        if (!inputAmount.isEmpty()) {
            double enteredAmount = Double.parseDouble(inputAmount);

            // Lấy số dư hiện tại từ TextView
            String currentBalanceString = sodu.getText().toString();
            double currentBalance = Double.parseDouble(currentBalanceString);

            // Cộng thêm số tiền nhập vào số dư
            double updatedBalance = currentBalance + enteredAmount;

            // Cập nhật TextView và giá trị ví trên Firebase Realtime Database
            sodu.setText(String.valueOf(updatedBalance));
            updateWalletInFirebase(updatedBalance);
        }
    }

    private void updateWalletInFirebase(double enteredAmount) {
        // Lấy thông tin người dùng hiện tại
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("nguoidung");
            Query query = databaseReference.orderByChild("email").equalTo(userEmail);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            // Cập nhật giá trị ví trong Firebase
                            userSnapshot.getRef().child("wallet").setValue(enteredAmount);

                            // Sau khi cập nhật, đọc lại giá trị từ Firebase
                            userSnapshot.getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    // Lấy giá trị ví mới từ Firebase
                                    int updatedWallet = dataSnapshot.child("wallet").getValue(Integer.class);

                                    // Gửi kết quả trở lại cho Fragment
                                    Intent resultIntent = new Intent();
                                    resultIntent.putExtra("UPDATED_WALLET", updatedWallet);
                                    setResult(RESULT_OK, resultIntent);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Xử lý lỗi nếu có
                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Xử lý lỗi nếu có
                }
            });
        }
    }


}

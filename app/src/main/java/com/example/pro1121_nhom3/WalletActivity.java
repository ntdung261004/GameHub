package com.example.pro1121_nhom3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

import java.text.NumberFormat;
import java.util.Locale;

public class WalletActivity extends AppCompatActivity {

    private TextView sodu;
    private EditText nhaptien;
    private CardView momo_cardview, atm_cardview, visa_cardview;
    ImageView btnBack;
    String walletInVND;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        sodu = findViewById(R.id.sodu);
        nhaptien = findViewById(R.id.nhaptien);
        momo_cardview = findViewById(R.id.momo_cardview);
        btnBack = findViewById(R.id.btnBack);
        atm_cardview = findViewById(R.id.atm_cardview);
        visa_cardview = findViewById(R.id.visa_cardview);

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
                handleCustomAmount();
            }
        });
        visa_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCustomAmount(); // Xử lý số tiền nhập tùy ý
            }
        });
        atm_cardview.setOnClickListener(new View.OnClickListener() {
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
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String walletInVND = format.format(userWallet);
        sodu.setText(walletInVND);
    }

    private void handleCustomAmount() {
        // Lấy số tiền từ EditText
        String inputAmount = nhaptien.getText().toString();

        if (!inputAmount.isEmpty() && inputAmount.matches("[0-9,]+")) {
            double enteredAmount = Double.parseDouble(inputAmount.replace(",", ""));

            // Tạo AlertDialog để xác nhận nạp tiền
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Xác nhận nạp tiền");
            builder.setMessage("Bạn có chắc muốn nạp số tiền này không?");

            // Nút "Có"
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Lấy số dư hiện tại từ TextView
                    String currentBalanceString = sodu.getText().toString();
                    currentBalanceString = currentBalanceString.replaceAll("[^0-9]", "");
                    double currentBalance = Double.parseDouble(currentBalanceString);

                    // Cộng thêm số tiền nhập vào số dư
                    double updatedBalance = currentBalance + enteredAmount;

                    // Định dạng số dư ví thành định dạng tiền tệ VND
                    NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                    String walletInVND = format.format(updatedBalance);

                    // Cập nhật TextView và giá trị ví trên Firebase Realtime Database
                    sodu.setText(walletInVND);
                    updateWalletInFirebase(updatedBalance);

                    // Hiển thị thông báo nạp tiền thành công
                    Toast.makeText(WalletActivity.this, "Nạp tiền thành công", Toast.LENGTH_SHORT).show();
                }
            });

            // Nút "Không"
            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Đóng hộp thoại và không thay đổi gì
                    dialog.dismiss();
                }
            });

            // Hiển thị AlertDialog
            builder.create().show();
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
                                    double updatedWallet = dataSnapshot.child("wallet").getValue(Double.class);
                                    NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                                    String walletInVND = format.format(updatedWallet);
                                    // Gửi kết quả trở lại cho Fragment
                                    Intent resultIntent = new Intent();
                                    resultIntent.putExtra("UPDATED_WALLET", walletInVND);
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

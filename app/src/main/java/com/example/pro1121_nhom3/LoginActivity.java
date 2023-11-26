package com.example.pro1121_nhom3;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pro1121_nhom3.MainActivity;
import com.example.pro1121_nhom3.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {

    private EditText etuser, etpass;
    private Button btlogin, btregister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ánh xạ các thành phần giao diện
        etuser = findViewById(R.id.edtUsername);
        etpass = findViewById(R.id.edtPassword);
        btlogin = findViewById(R.id.btlogin);
        btregister = findViewById(R.id.btregister);
        mAuth = FirebaseAuth.getInstance();

        // Xử lý sự kiện khi nhấn nút đăng nhập
        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        // Xử lý sự kiện khi nhấn nút đăng ký
        btregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    // Phương thức xử lý đăng nhập
    private void login() {
        String user, pass;
        user = etuser.getText().toString();
        pass = etpass.getText().toString();
        if (TextUtils.isEmpty(user)) {
            Toast.makeText(this, "Vui lòng nhập Username", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Vui lòng nhập Password", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    // Thêm code để lấy dữ liệu từ Realtime Database và chuyển sang MainActivity
                    fetchUserData(user);
                } else {
                    Toast.makeText(LoginActivity.this, "Đăng nhập không thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Phương thức để lấy dữ liệu người dùng từ Realtime Database
    private void fetchUserData(String userEmail) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("nguoidung");
        Query query = databaseReference.orderByChild("email").equalTo(userEmail);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        // Lấy thông tin người dùng từ snapshot
                        String userName = userSnapshot.child("tennd").getValue(String.class);
                        String userEmail = userSnapshot.child("email").getValue(String.class);
                        int userWallet = userSnapshot.child("wallet").getValue(Integer.class);
                        String userPass = userSnapshot.child("matkhau").getValue(String.class);

                        // Chuyển đến MainActivity và truyền dữ liệu
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("userName", userName);
                        intent.putExtra("userWallet", userWallet);
                        intent.putExtra("userEmail", userEmail);
                        intent.putExtra("userPassword", userPass);
                        startActivity(intent);
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

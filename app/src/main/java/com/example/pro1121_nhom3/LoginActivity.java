package com.example.pro1121_nhom3;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
    private Button btlogin;
    private TextView txtregister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ánh xạ các thành phần giao diện
        etuser = findViewById(R.id.edtUsername);
        etpass = findViewById(R.id.edtPassword);
        btlogin = findViewById(R.id.btlogin);
        txtregister = findViewById(R.id.txtregister);
        mAuth = FirebaseAuth.getInstance();

        // Xử lý sự kiện khi nhấn nút đăng nhập
        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        // Xử lý sự kiện khi nhấn nút đăng ký
        txtregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    // Phương thức xử lý đăng nhập
    private void login() {
        String input = etuser.getText().toString();
        String pass = etpass.getText().toString();

        if (TextUtils.isEmpty(input) || TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Vui lòng nhập Email/Key và Password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra xem input có phải là email hay key
        if (isValidEmail(input)) {
            // Đăng nhập bằng email
            mAuth.signInWithEmailAndPassword(input, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        // Thêm code để lấy dữ liệu từ Realtime Database và chuyển sang MainActivity
                        fetchUserData(input); // Sử dụng email để lấy dữ liệu
                    } else {
                        Toast.makeText(LoginActivity.this, "Đăng nhập không thành công", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            // Đăng nhập bằng key
            loginWithKey(input, pass);
        }
    }

    // Phương thức kiểm tra xem chuỗi có phải là email hay không
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Phương thức đăng nhập bằng key
    private void loginWithKey(String key, String password) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("nguoidung");

        // Kiểm tra xem key có tồn tại trong cơ sở dữ liệu hay không
        databaseReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Lấy email được liên kết với key
                    String userEmail = snapshot.child("email").getValue(String.class);

                    // Thực hiện xác thực Firebase với email và mật khẩu
                    mAuth.signInWithEmailAndPassword(userEmail, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                        // Lấy thông tin người dùng sau khi đăng nhập thành công
                                        fetchUserData(userEmail);
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Đăng nhập không thành công", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(LoginActivity.this, "Key không tồn tại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi từ cơ sở dữ liệu
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

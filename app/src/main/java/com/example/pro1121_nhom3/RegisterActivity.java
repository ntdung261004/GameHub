package com.example.pro1121_nhom3;

import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtUsername, edttennd, edtGmail, edtPassword, edtenterpassword;
    private Button btSignIn;
    private TextView tvLogin;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Khởi tạo Firebase Auth và Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("nguoidung");

        edtUsername = findViewById(R.id.edtUsername);
        edttennd = findViewById(R.id.edttennd);
        edtGmail = findViewById(R.id.edtGmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtenterpassword = findViewById(R.id.edtenterpassword);
        btSignIn = findViewById(R.id.btSignIn);
        tvLogin=findViewById(R.id.tvLogin);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Gọi hàm đăng ký khi người dùng click nút đăng ký
                registerUser();
            }
        });
    }

    private void registerUser() {
        final String username = edtUsername.getText().toString().trim();
        final String tennd = edttennd.getText().toString().trim();
        final String email = edtGmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtenterpassword.getText().toString().trim();

        if (username.isEmpty() || tennd.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        } else {
            // Thực hiện đăng ký người dùng trên Firebase
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Lưu thông tin người dùng vào Database với username làm khóa chính
                                DatabaseReference currentUserDb = mDatabase.child(username);
                                currentUserDb.child("email").setValue(email);
                                currentUserDb.child("tennd").setValue(tennd);
                                currentUserDb.child("role").setValue(1);
                                currentUserDb.child("wallet").setValue(0);
                                currentUserDb.child("matkhau").setValue(password);

                                // Hiển thị thông báo đăng ký thành công
                                Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                            } else {
                                // Hiển thị thông báo lỗi nếu đăng ký không thành công
                                if (task.getException() != null) {
                                    Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
        }
    }
}

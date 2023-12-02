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

public class LoginActivity extends AppCompatActivity {

    private EditText etuser, etpass;
    private Button btlogin;
    private FirebaseAuth mAuth;
    private TextView txtregister,txtForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Map UI elements
        etuser = findViewById(R.id.edtUsername);
        etpass = findViewById(R.id.edtPassword);
        btlogin = findViewById(R.id.btlogin);
        txtregister = findViewById(R.id.txtregister);
        txtForgotPassword=findViewById(R.id.txtForgotPassword);
        // Set OnClickListener for the login button
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
        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });
    }
    // Method to handle login
    private void login() {
        String input = etuser.getText().toString().trim();
        String pass = etpass.getText().toString().trim();

        if (TextUtils.isEmpty(input) || TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Vui lòng nhập Email/Key và Password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the input is a valid email
        if (isValidEmail(input)) {
            // Login with email
            mAuth.signInWithEmailAndPassword(input, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        fetchUserData(input);
                    } else {
                        Toast.makeText(LoginActivity.this, "Đăng nhập không thành công", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            // Login with key
            loginWithKey(input, pass);
        }
    }
    // Method to check if the input is a valid email
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Method to login with key
    private void loginWithKey(String key, String password) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("nguoidung");

        databaseReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String userEmail = snapshot.child("email").getValue(String.class);

                    mAuth.signInWithEmailAndPassword(userEmail, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                        fetchUserData(userEmail);
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Đăng nhập không thành công", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }

    // Method to fetch user data from Realtime Database
    // Trong phương thức fetchUserData
    private void fetchUserData(String userEmail) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("nguoidung");
        Query query = databaseReference.orderByChild("email").equalTo(userEmail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        Intent intent;
                        if (userSnapshot.child("role").getValue(Integer.class) == 2) {
                            // Redirect to AdminActivity
                            intent = new Intent(LoginActivity.this, AdminActivity.class);
                            startActivity(intent);
                            finish();
                            return;
                        } else {
                            intent = new Intent(LoginActivity.this, MainActivity.class);
                            // Truyền dữ liệu cần thiết nếu có
                            intent.putExtra("userName", userSnapshot.child("tennd").getValue(String.class));
                            intent.putExtra("userWallet", userSnapshot.child("wallet").getValue(Integer.class));
                        }
                        intent.putExtra("userEmail", userEmail);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }
}

package com.example.pro1121_nhom3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText etuser;
    private Button btlogin;
    private CheckBox checkboxrem;
    private FirebaseAuth mAuth;
    private TextView txtregister, txtForgotPassword;
    private TextInputLayout edtPasswordLayout, edtPasswordLayout1;
    private TextInputEditText etpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        edtPasswordLayout = findViewById(R.id.edtPasswordLayout);
        edtPasswordLayout1 = findViewById(R.id.edtPasswordLayout1);
        checkboxrem = findViewById(R.id.checkboxrem);
        etuser = findViewById(R.id.edtUsername);
        etpass = findViewById(R.id.edtPassword);

        // Khôi phục thông tin đăng nhập đã lưu
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("remember", false)) {
            String savedEmail = sharedPreferences.getString("email", "");
            String savedKey = sharedPreferences.getString("key", "");
            String savedPassword = sharedPreferences.getString("password", "");
            boolean isLoginWithEmail = sharedPreferences.getBoolean("isLoginWithEmail", false);

            etuser.setText(isLoginWithEmail ? savedEmail : savedKey);
            etpass.setText(savedPassword);
            checkboxrem.setChecked(true);
            login();
        }

        // Ánh xạ các phần tử giao diện
        btlogin = findViewById(R.id.btlogin);
        txtregister = findViewById(R.id.txtregister);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);

        // Đặt OnClickListener cho nút đăng nhập
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

    // Phương thức xử lý đăng nhập
    private void login() {
        String input = etuser.getText().toString().trim();
        String pass = etpass.getText().toString().trim();

        if (TextUtils.isEmpty(input) || TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Vui lòng nhập Email/Key và Password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra xem input có phải là một email hợp lệ không
        if (isValidEmail(input)) {
            // Kiểm tra xem email có tồn tại không
            mAuth.fetchSignInMethodsForEmail(input).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                @Override
                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                    if (task.isSuccessful()) {
                        SignInMethodQueryResult result = task.getResult();
                        if (result.getSignInMethods().size() > 0) {
                            // Email tồn tại, thử đăng nhập
                            mAuth.signInWithEmailAndPassword(input, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                        // Lưu thông tin đăng nhập nếu checkbox được chọn
                                        if (checkboxrem.isChecked()) {
                                            // Lưu với loại đăng nhập là email
                                            saveCredentials(input, "", pass, true);
                                        }
                                        fetchUserData(input);
                                    } else {
                                        // Xử lý mật khẩu sai
                                        Toast.makeText(LoginActivity.this, "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            // Email không tồn tại
                            Toast.makeText(LoginActivity.this, "Email không tồn tại", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Xử lý lỗi
                        Toast.makeText(LoginActivity.this, "Đăng nhập không thành công", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            // Đăng nhập bằng key
            loginWithKey(input, pass);
        }
    }


    // Phương thức kiểm tra xem input có phải là một email hợp lệ không
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Phương thức đăng nhập bằng key
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
                                        // Lưu thông tin đăng nhập nếu checkbox được chọn
                                        if (checkboxrem.isChecked()) {
                                            // Lưu với loại đăng nhập là key
                                            saveCredentials(userEmail, key, password, false);
                                        }
                                        fetchUserData(userEmail);
                                    } else {
                                        // Xử lý mật khẩu sai
                                        Toast.makeText(LoginActivity.this, "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    // Xử lý trường hợp key không tồn tại
                    Toast.makeText(LoginActivity.this, "Tên đăng nhập không tồn tại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi database
            }
        });
    }


    // Phương thức lấy dữ liệu người dùng từ Realtime Database
    private void fetchUserData(String userEmail) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("nguoidung");
        Query query = databaseReference.orderByChild("email").equalTo(userEmail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        Intent intent;
                        if (userSnapshot.child("role").getValue(Integer.class) > 1) {
                            // Chuyển hướng đến AdminActivity
                            if(userSnapshot.child("role").getValue(Integer.class) == 2){
                                intent = new Intent(LoginActivity.this, AdminActivity.class);
                                startActivity(intent);
                                finish();
                                return;
                            }else{
                                intent = new Intent(LoginActivity.this, AdminActivity.class);
                                intent.putExtra("nv",true);
                                startActivity(intent);
                                finish();
                                return;
                            }

                        } else {
                            intent = new Intent(LoginActivity.this, MainActivity.class);
                            // Truyền dữ liệu cần thiết nếu có
                            intent.putExtra("userName", userSnapshot.child("tennd").getValue(String.class));
                            intent.putExtra("userWallet", userSnapshot.child("wallet").getValue(Long.class)); // Sửa ở đây
                        }
                        intent.putExtra("userEmail", userEmail);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi database
            }
        });
    }

    // Phương thức lưu thông tin đăng nhập và loại đăng nhập
    private void saveCredentials(String email, String key, String password, boolean isLoginWithEmail) {
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("remember", true);
        editor.putBoolean("isLoginWithEmail", isLoginWithEmail);
        editor.putString("email", email);
        editor.putString("key", key);
        editor.putString("password", password);
        editor.apply();
    }
}

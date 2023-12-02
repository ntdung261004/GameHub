package com.example.pro1121_nhom3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pro1121_nhom3.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class changespassword extends AppCompatActivity {

    private EditText etOldPassword, etNewPassword,etConfirmPassword;
    private Button changePasswordButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changespassword);

        mAuth = FirebaseAuth.getInstance();

        etOldPassword = findViewById(R.id.etOldPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        changePasswordButton = findViewById(R.id.changePasswordButton);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });
    }

    private void changePassword() {
        String oldPassword = etOldPassword.getText().toString().trim();
        final String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword)) {
            Toast.makeText(this, "Vui lòng nhập cả mật khẩu cũ và mới", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Vui lòng nhập cả mật khẩu cũ, mới và nhập lại mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu mới và nhập lại mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), oldPassword);
            currentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        currentUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    showSuccessDialog();
                                } else {
                                    Toast.makeText(changespassword.this, "Không thể đổi mật khẩu. Kiểm tra lại mật khẩu cũ.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(changespassword.this, "Xác minh mật khẩu cũ không thành công. Vui lòng kiểm tra lại.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thành công")
                .setMessage("Bạn đã thay đổi mật khẩu thành công. Vui lòng đăng nhập lại.")
                .setPositiveButton("Đăng nhập lại", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // TODO: Add code to navigate back to the login screen
                        // For example, you can use Intent to start LoginActivity
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Log để kiểm tra xem khối mã này có được thực thi không
                        Log.d("ChangesPasswordActivity", "Người dùng chọn 'Không'");

                        // Do nothing or add any additional action
                        finish(); // Đóng activity nếu "Không" được chọn
                    }
                })
                .setCancelable(false) // Ngăn chặn việc đóng dialog khi chạm bên ngoài
                .show();
    }

}

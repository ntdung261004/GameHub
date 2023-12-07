package com.example.pro1121_nhom3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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

public class AdminActivity extends AppCompatActivity {

    LinearLayout luser, lbill, lgame, lpass, lempl, llogout,linerDoiMK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        luser = findViewById(R.id.linerUser);
        lbill = findViewById(R.id.linerbill);
        lgame = findViewById(R.id.linerGAME);
        lpass = findViewById(R.id.linerDoiMK);
        lempl = findViewById(R.id.linearAddNV);
        llogout = findViewById(R.id.linerLogout);
        linerDoiMK=findViewById(R.id.linerDoiMK);
        linerDoiMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, changespassword.class);
                startActivity(intent);
            }
        });
        llogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSavedCredentials();

                // Navigate back to LoginActivity
                Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
               AdminActivity.this.finish();
            }
        });
        Intent intent = getIntent();
        if(intent.getBooleanExtra("nv", false)) lempl.setVisibility(View.GONE);


        luser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminActivity.this, UserActivity.class);
                if(intent.getBooleanExtra("nv", false)){
                    i.putExtra("nva", false);
                }
                startActivity(i);
            }
        });

        lbill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminActivity.this, hoadonActivity.class);
                startActivity(i);
            }
        });

        lgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminActivity.this, GameActivity.class);
                startActivity(i);
            }
        });

        lempl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(AdminActivity.this);
                dialog.setContentView(R.layout.dialogaddnv);

                Window window = dialog.getWindow();
                if(window == null){
                    return;
                }

                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                EditText ethoten = dialog.findViewById(R.id.ethotennv);
                EditText etuser = dialog.findViewById(R.id.etusernamenv);
                EditText etpass = dialog.findViewById(R.id.etpassnv);
                EditText etmail = dialog.findViewById(R.id.etmailnv);
                Button btreg = dialog.findViewById(R.id.btaddnv);

                btreg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String hoten = ethoten.getText().toString();
                        String user = etuser.getText().toString();
                        String pass = etpass.getText().toString();
                        String mail = etmail.getText().toString();
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("nguoidung");
                        Query query = userRef.orderByKey().equalTo(user);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    return;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        mAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(AdminActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Lưu thông tin người dùng vào Database với username làm khóa chính
                                    DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference("nguoidung").child(user);
                                    currentUserDb.child("avatar").setValue("https://firebasestorage.googleapis.com/v0/b/pro1121-nhom3.appspot.com/o/nv.png?alt=media&token=0a2724e5-f4ca-4719-80b6-6a69233ca479");
                                    currentUserDb.child("email").setValue(mail);
                                    currentUserDb.child("tennd").setValue(hoten);
                                    currentUserDb.child("role").setValue(3);
                                    currentUserDb.child("wallet").setValue(0);
                                    currentUserDb.child("matkhau").setValue(pass);

                                    // Hiển thị thông báo đăng ký thành công
                                    Toast.makeText(AdminActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Hiển thị thông báo lỗi nếu đăng ký không thành công
                                    if (task.getException() != null) {
                                        Toast.makeText(AdminActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AdminActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                    }
                });

                dialog.show();
            }
        });
    }
    private void clearSavedCredentials() {
        SharedPreferences sharedPreferences = AdminActivity.this.getSharedPreferences("loginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();  // Clear all saved preferences
        editor.apply();
    }
}
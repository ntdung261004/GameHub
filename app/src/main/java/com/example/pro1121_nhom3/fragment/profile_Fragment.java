package com.example.pro1121_nhom3.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.pro1121_nhom3.LoginActivity;
import com.example.pro1121_nhom3.MainActivity;
import com.example.pro1121_nhom3.R;
import com.example.pro1121_nhom3.WalletActivity;
import com.example.pro1121_nhom3.model.nguoidung;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class profile_Fragment extends Fragment {

    private TextView tvtenuser, edtemail, wallet;
    private EditText edttennguoidung;
    private TextInputEditText edtpassword;
    private Button btnUpdate;
    private ImageButton btnWallet;
    private ImageView avatar;

    public profile_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_, container, false);

        tvtenuser = view.findViewById(R.id.tvtenuser);
        wallet = view.findViewById(R.id.wallet);
        edtemail = view.findViewById(R.id.edtemail);
        edttennguoidung = view.findViewById(R.id.edttennguoidung);
        edtpassword = view.findViewById(R.id.edtpasswaord);
        edtpassword.setTransformationMethod(new PasswordTransformationMethod());
        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnWallet = view.findViewById(R.id.btnWallet);
        avatar = view.findViewById(R.id.avtuser);

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
                            nguoidung nguoidung1 = userSnapshot.getValue(nguoidung.class);
                            String avatarUrl = nguoidung1.getAvatar();
                            Glide.with(getActivity()).load(avatarUrl).into(avatar);
                            tvtenuser.setText(nguoidung1.getTennd());
                            wallet.setText(String.valueOf(nguoidung1.getWallet()));
                            edtemail.setText(nguoidung1.getEmail());
                            edttennguoidung.setText(nguoidung1.getTennd());
                            edtpassword.setText(nguoidung1.getMatkhau());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = edttennguoidung.getText().toString().trim();
                String newPassword = edtpassword.getText().toString().trim();

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("nguoidung");
                    Query query = databaseReference.orderByChild("email").equalTo(currentUser.getEmail());

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                boolean isUsernameChanged = false;
                                boolean isPasswordChanged = false;

                                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                    if (!newUsername.isEmpty() && !newUsername.equals(userSnapshot.child("tennd").getValue(String.class))) {
                                        userSnapshot.getRef().child("tennd").setValue(newUsername);
                                        tvtenuser.setText(newUsername);
                                        isUsernameChanged = true;
                                    }

                                    if (!newPassword.isEmpty() && !newPassword.equals(userSnapshot.child("matkhau").getValue(String.class))) {
                                        isPasswordChanged = true;
                                    }
                                }

                                if (isUsernameChanged || isPasswordChanged) {
                                    boolean finalIsUsernameChanged = isUsernameChanged;
                                    boolean finalIsPasswordChanged = isPasswordChanged;
                                    new AlertDialog.Builder(getActivity())
                                            .setTitle("Xác nhận thay đổi")
                                            .setMessage("Bạn có chắc chắn muốn thay đổi?")
                                            .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Update is confirmed, proceed with the changes
                                                    if (finalIsUsernameChanged) {
                                                        Snackbar.make(getView(), "Bạn đã thay đổi tên người dùng thành công.", Snackbar.LENGTH_SHORT).show();
                                                    }
                                                    if (finalIsPasswordChanged) {
                                                        FirebaseAuth.getInstance().signOut();
                                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                                        startActivity(intent);
                                                        getActivity().finish();
                                                    }
                                                }
                                            })
                                            .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // User clicked "Không," do not save changes
                                                    // You can choose to dismiss the dialog or leave it open
                                                    dialog.dismiss();
                                                }
                                            })
                                            .show();
                                } else {
                                    Snackbar.make(getView(), "Không có thay đổi nào được thực hiện.", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
        });
        edtpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        edtpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtpassword.setTransformationMethod(null);
            }
        });

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Thay đổi Avatar");
                builder.setMessage("Bạn có muốn thay đổi avatar không?");

                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final EditText input = new EditText(getActivity());
                        input.setInputType(InputType.TYPE_CLASS_TEXT);
                        input.setHint("Nhập URL hình ảnh mới");
                        builder.setView(input);

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String newAvatarUrl = input.getText().toString().trim();
                                if (!newAvatarUrl.isEmpty()) {
                                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                    if (currentUser != null) {
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("nguoidung");
                                        Query query = databaseReference.orderByChild("email").equalTo(currentUser.getEmail());

                                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                                        userSnapshot.getRef().child("avatar").setValue(newAvatarUrl);
                                                    }

                                                    // Hiển thị Snackbar khi thay đổi avatar thành công
                                                    Snackbar.make(getView(), "Thay đổi avatar thành công", Snackbar.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                            }
                                        });
                                    }

                                    // Load hình ảnh mới vào ImageView sử dụng Glide
                                    Glide.with(getActivity()).load(newAvatarUrl).into(avatar);
                                } else {
                                    Snackbar.make(getView(), "Vui lòng nhập URL hình ảnh", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });

                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.show();
                    }
                });

                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
        btnWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WalletActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == AppCompatActivity.RESULT_OK) {
            if (data != null) {
                int updatedWallet = data.getIntExtra("UPDATED_WALLET", 0);
                wallet.setText(String.valueOf(updatedWallet));
            }
        }
    }
}

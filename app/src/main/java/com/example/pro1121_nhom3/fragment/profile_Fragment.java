    package com.example.pro1121_nhom3.fragment;

    import android.content.DialogInterface;
    import android.content.Intent;
    import android.os.Bundle;
    import android.text.InputType;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageButton;
    import android.widget.ImageView;
    import android.widget.TextView;
    import android.widget.Toast;
    import android.text.method.PasswordTransformationMethod;
    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AlertDialog;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.fragment.app.Fragment;

    import com.bumptech.glide.Glide;
    import com.example.pro1121_nhom3.LoginActivity;
    import com.example.pro1121_nhom3.R;
    import com.example.pro1121_nhom3.WalletActivity;
    import com.example.pro1121_nhom3.changespassword;
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

        private TextView tvtenuser, edtemail, wallet, edtpassword;
        private EditText edttennguoidung;
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
            edtpassword = view.findViewById(R.id.edtpasswaord); // Corrected typo in the ID
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
                                Glide.with(requireActivity()).load(avatarUrl).into(avatar);
                                tvtenuser.setText(nguoidung1.getTennd());
                                wallet.setText(String.valueOf(nguoidung1.getWallet()));
                                edtemail.setText(nguoidung1.getEmail());
                                edttennguoidung.setText(nguoidung1.getTennd());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle the error
                        Toast.makeText(requireContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            edtpassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showChangePasswordConfirmationDialog();
                }
            });

            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newUsername = edttennguoidung.getText().toString().trim();
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (currentUser != null) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("nguoidung");
                        Query query = databaseReference.orderByChild("email").equalTo(currentUser.getEmail());

                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    boolean isUsernameChanged = false;

                                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                        if (!newUsername.isEmpty() && !newUsername.equals(userSnapshot.child("tennd").getValue(String.class))) {
                                            userSnapshot.getRef().child("tennd").setValue(newUsername);
                                            tvtenuser.setText(newUsername);
                                            isUsernameChanged = true;
                                        }
                                    }
                                    if (isUsernameChanged) {
                                        // Username is changed successfully
                                        showSnackbar("Bạn đã thay đổi tên người dùng thành công.");

                                        // Optionally, you can add code here to update other information in Firebase
                                        // For example, update other fields if needed
                                    } else {
                                        // No changes
                                        showSnackbar("Không có thay đổi nào được thực hiện.");
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle the error
                                Toast.makeText(requireContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });

            avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAvatarChangeDialog();
                }
            });

            btnWallet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(requireActivity(), WalletActivity.class);
                    startActivityForResult(intent, 1);
                }
            });

            return view;
        }

        private void showChangePasswordConfirmationDialog() {
            new AlertDialog.Builder(requireActivity())
                    .setTitle("Xác nhận thay đổi mật khẩu")
                    .setMessage("Bạn có muốn thay đổi mật khẩu không?")
                    .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Người dùng xác nhận, chuyển hướng đến ActivityChangesPassword
                            Intent intent = new Intent(requireActivity(), changespassword.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Người dùng chọn "Không," không làm gì cả
                            dialog.dismiss();
                        }
                    })
                    .show();
        }

        private void showSnackbar(String message) {
            Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show();
        }

        private void showAvatarChangeDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle("Thay đổi Avatar");
            builder.setMessage("Bạn có muốn thay đổi avatar không?");

            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final EditText input = new EditText(requireActivity());
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
                                                showSnackbar("Thay đổi avatar thành công");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            // Handle the error
                                            Toast.makeText(requireContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                // Load hình ảnh mới vào ImageView sử dụng Glide
                                Glide.with(requireActivity()).load(newAvatarUrl).into(avatar);
                            } else {
                                showSnackbar("Vui lòng nhập URL hình ảnh");
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

package com.example.pro1121_nhom3.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
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
import com.example.pro1121_nhom3.likedListActivity;
import com.example.pro1121_nhom3.model.nguoidung;
import com.google.android.material.snackbar.Snackbar;
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

public class profile_Fragment extends Fragment {

    private TextView tvtenuser, edtemail, wallet, edtpassword, tvlikelist;
    private EditText edttennguoidung;
    private Button btnUpdate;
    private ImageButton btnWallet,btnout;
    private ImageView avatar;

    public profile_Fragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_, container, false);

        tvlikelist = view.findViewById(R.id.tvlikelistprf);
        tvtenuser = view.findViewById(R.id.tvtenuserbill);
        wallet = view.findViewById(R.id.wallet);
        edtemail = view.findViewById(R.id.edtemail);
        edttennguoidung = view.findViewById(R.id.edttennguoidung);
        edtpassword = view.findViewById(R.id.edtpasswaord); // Corrected typo in the ID
        edtpassword.setTransformationMethod(new PasswordTransformationMethod());
        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnWallet = view.findViewById(R.id.btnWallet);
        avatar = view.findViewById(R.id.avtuser);
        btnout=view.findViewById(R.id.btnout);
        btnout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear saved credentials
                clearSavedCredentials();

                // Navigate back to LoginActivity
                Intent intent = new Intent(requireActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                requireActivity().finish();
            }
        });
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("nguoidung");
            Query query = databaseReference.orderByChild("email").equalTo(userEmail);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Kiểm tra xem fragment có đính kèm với hoạt động không
                    if (isAdded()) {
                        if (snapshot.exists()) {
                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                nguoidung nguoidung1 = userSnapshot.getValue(nguoidung.class);
                                if (nguoidung1 != null) {
                                    String avatarUrl = nguoidung1.getAvatar();
                                    Glide.with(requireActivity()).load(avatarUrl).into(avatar);
                                    tvtenuser.setText(nguoidung1.getTennd());

                                    // Định dạng số dư ví thành định dạng tiền tệ VND
                                    int userWallet = (int) nguoidung1.getWallet();
                                    NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                                    String walletInVND = format.format(userWallet);
                                    wallet.setText(walletInVND);

                                    edtemail.setText(nguoidung1.getEmail());
                                    edttennguoidung.setText(nguoidung1.getTennd());
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Xử lý lỗi
                    // Kiểm tra xem fragment có đính kèm với hoạt động không trước khi hiển thị thông báo
                    if (isAdded()) {
                        Toast.makeText(requireContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
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

        SpannableString spannableString = new SpannableString(tvlikelist.getText());
        UnderlineSpan underlineSpan = new UnderlineSpan();
        spannableString.setSpan(underlineSpan,0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvlikelist.setText(spannableString);

        tvlikelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), likedListActivity.class));
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
        CharSequence[] options = {"Chụp ảnh", "Chọn từ thư viện", "Nhập URL"};

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Thay đổi Avatar");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        // Chụp ảnh - Xử lý chụp ảnh
                        captureImage();
                        break;
                    case 1:
                        // Chọn từ thư viện - Xử lý chọn từ thư viện
                        pickImageFromGallery();
                        break;
                    case 2:
                        // Nhập URL - Hiển thị hộp thoại nhập URL
                        showEnterUrlDialog();
                        break;
                }
            }
        });

        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }


    private void showEnterUrlDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Nhập URL");

        final EditText input = new EditText(requireActivity());
        input.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newAvatarUrl = input.getText().toString().trim();
                if (!newAvatarUrl.isEmpty()) {
                    handleSelectedImage(Uri.parse(newAvatarUrl));
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
    private void clearSavedCredentials() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();  // Clear all saved preferences
        editor.apply();
    }
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_REQUEST);
    }

    private static final int IMAGE_PICK_REQUEST = 2;
    private static final int CAMERA_CAPTURE_REQUEST = 3;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICK_REQUEST && resultCode == AppCompatActivity.RESULT_OK) {
            if (data != null) {
                Uri selectedImageUri = data.getData();
                handleSelectedImage(selectedImageUri);
            }
        } else if (requestCode == CAMERA_CAPTURE_REQUEST && resultCode == AppCompatActivity.RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                // Lưu ảnh vào storage hoặc xử lý theo nhu cầu
            }
        } else if (requestCode == 1 && resultCode == AppCompatActivity.RESULT_OK) {
            if (data != null) {
                String updatedWallet = data.getStringExtra("UPDATED_WALLET");
                wallet.setText(updatedWallet);
            }
        }
    }


    private void handleSelectedImage(Uri selectedImageUri) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("nguoidung");
            Query query = databaseReference.orderByChild("email").equalTo(currentUser.getEmail());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            userSnapshot.getRef().child("avatar").setValue(selectedImageUri.toString());
                        }

                        Glide.with(requireActivity()).load(selectedImageUri).into(avatar);
                        showSnackbar("Thay đổi avatar thành công");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(requireContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void captureImage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAMERA_CAPTURE_REQUEST);
        } else {
            Toast.makeText(requireContext(), "Không thể mở ứng dụng Camera", Toast.LENGTH_SHORT).show();
        }
    }

}

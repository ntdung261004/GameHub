package com.example.pro1121_nhom3.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.pro1121_nhom3.MainActivity;
import com.example.pro1121_nhom3.R;
import com.example.pro1121_nhom3.WalletActivity;
import com.example.pro1121_nhom3.model.game;
import com.example.pro1121_nhom3.model.nguoidung;
import com.example.pro1121_nhom3.pagegameActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class profile_Fragment extends Fragment {

    private TextView tvtenuser,edtemail;;
    private TextView wallet;
    private EditText edttennguoidung;
    private EditText edtpasswaord;
    private Button btnUpdate;
    private ImageButton btnWallet;
    private ImageView avatar;


    public profile_Fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_, container, false);

        tvtenuser = view.findViewById(R.id.tvtenuser);
        wallet = view.findViewById(R.id.wallet);
        edtemail = view.findViewById(R.id.edtemail);
        edttennguoidung = view.findViewById(R.id.edttennguoidung);
        edtpasswaord = view.findViewById(R.id.edtpasswaord);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnWallet = view.findViewById(R.id.btnWallet);
        avatar = view.findViewById(R.id.avtuser);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null)
        {
            String userEmail = currentUser.getEmail();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("nguoidung");
            Query query = databaseReference.orderByChild("email").equalTo(userEmail);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            nguoidung nguoidung1 = userSnapshot.getValue(nguoidung.class);
                            Glide.with(getActivity()).load(nguoidung1.getAvatar()).into(avatar);
                            tvtenuser.setText(nguoidung1.getTennd());
                            wallet.setText(nguoidung1.getWallet()+"");

                            edtemail.setText(nguoidung1.getEmail());
                            edttennguoidung.setText(nguoidung1.getTennd());
                            edtpasswaord.setText(nguoidung1.getMatkhau());

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Xử lý lỗi nếu có
                }
            });
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý sự kiện khi nút "Cập nhật" được nhấn
                // Lấy dữ liệu từ EditText và thực hiện cập nhật
            }
        });

        btnWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở WalletActivity khi nút "Ví" được nhấn
                Intent intent = new Intent(getActivity(), WalletActivity.class);
                startActivityForResult(intent, 1); // Sử dụng startActivityForResult để nhận kết quả từ WalletActivity
            }
        });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == AppCompatActivity.RESULT_OK) {
            // Lấy dữ liệu từ Intent trả về và cập nhật giao diện
            if (data != null) {
                int updatedWallet = data.getIntExtra("UPDATED_WALLET", 0);
                wallet.setText(String.valueOf(updatedWallet));
            }
        }
    }
}

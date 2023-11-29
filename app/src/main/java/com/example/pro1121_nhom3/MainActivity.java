package com.example.pro1121_nhom3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Toast;

import com.example.pro1121_nhom3.databinding.ActivityMainBinding;
import com.example.pro1121_nhom3.fragment.cart_Fragment;
import com.example.pro1121_nhom3.fragment.newsfeed_Fragment;
import com.example.pro1121_nhom3.fragment.profile_Fragment;
import com.example.pro1121_nhom3.fragment.wishlist_Fragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        if(getIntent().getIntExtra("okok",0)==1){
            replaceFragment(new cart_Fragment());
            binding.bottomNavigationView.setSelectedItemId(R.id.c);
        }else{
            replaceFragment(new newsfeed_Fragment());
        }


        // Xử lý sự kiện khi chọn mục trên BottomNavigationView
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            // Kiểm tra mục được chọn và thay thế fragment tương ứng
            if (item.getItemId() == R.id.nf) replaceFragment(new newsfeed_Fragment());
            else if (item.getItemId() == R.id.wl) replaceFragment(new wishlist_Fragment());
            else if (item.getItemId() == R.id.c) replaceFragment(new cart_Fragment());
            else if (item.getItemId() == R.id.pf) replaceProfileFragment();

            return true; // Trả về true để báo hiệu rằng sự kiện đã được xử lý
        });
    }

    // Phương thức để thay thế fragment hiện tại
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.commit();
    }

    // Phương thức để thay thế fragment profile_Fragment với truyền tham số
    private void replaceProfileFragment() {
        // Lấy thông tin người dùng từ Intent
        String userName = getIntent().getStringExtra("userName");
        String userEmail = getIntent().getStringExtra("userEmail");
        String userPass = getIntent().getStringExtra("userPassword");
        int userWallet = getIntent().getIntExtra("userWallet", 0);

        // Thay thế fragment profile_Fragment và truyền tham số
        replaceFragment(profile_Fragment.newInstance(userName, userWallet, userEmail, userPass));
    }
}

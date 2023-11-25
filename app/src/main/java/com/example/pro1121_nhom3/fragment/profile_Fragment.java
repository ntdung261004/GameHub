package com.example.pro1121_nhom3.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.pro1121_nhom3.R;

public class profile_Fragment extends Fragment {

    private TextView tvtenuser;
    private TextView wallet;
    private EditText edtemail;
    private EditText edttennguoidung;
    private EditText edtpasswaord;
    private Button btnUpdate;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private String mParam1,mParam3,mParam4;
    private int mParam2;

    public profile_Fragment() {
    }

    public static profile_Fragment newInstance(String userName, int userWallet, String userEmail, String userPassword) {
        profile_Fragment fragment = new profile_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, userName);
        args.putString(ARG_PARAM3, userEmail);
        args.putInt(ARG_PARAM2, userWallet);
        args.putString(ARG_PARAM4, userPassword);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
            mParam4 = getArguments().getString(ARG_PARAM4);
        }
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

        if (getArguments() != null) {
            String userName = getArguments().getString(ARG_PARAM1);
            int userWallet = getArguments().getInt(ARG_PARAM2);
            String userEmail = getArguments().getString(ARG_PARAM3);
            String userPassword = getArguments().getString(ARG_PARAM4);
            updateUI(userName, userWallet, userEmail, userPassword);
        }


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEmail = edtemail.getText().toString();
                String newUserName = edttennguoidung.getText().toString();
                String newPassword = edtpasswaord.getText().toString();
            }
        });

        return view;
    }



    public void updateUI(String userName, int userWallet, String userEmail, String userPassword) {
        tvtenuser.setText(userName);
        wallet.setText(String.valueOf(userWallet));
        edtemail.setText(userEmail);
        edttennguoidung.setText(userName);
        edtpasswaord.setText(userPassword);
    }
}

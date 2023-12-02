package com.example.pro1121_nhom3.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pro1121_nhom3.R;
import com.example.pro1121_nhom3.adapter.gameLibAdapter;
import com.example.pro1121_nhom3.model.game;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class library_Fragment extends Fragment {

    RecyclerView rcvLib;
    ArrayList<game> listgame;
    gameLibAdapter libAdapter;

    public library_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library_, container, false);

        rcvLib = view.findViewById(R.id.rcvwishlist);
        listgame = new ArrayList<>();
        libAdapter = new gameLibAdapter(listgame, getActivity());
        rcvLib.setAdapter(libAdapter);

        // Set up RecyclerView with GridLayoutManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        rcvLib.setLayoutManager(gridLayoutManager);

        // Set up SearchView
        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Do nothing on submit
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the list when the user types in the search box
                libAdapter.filter(newText);
                return true;
            }
        });

        // Load game data
        checkIfUserHas();

        return view;
    }

    private void checkIfUserHas() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("nguoidung");
            Query query = databaseReference.orderByChild("email").equalTo(userEmail);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            userSnapshot.child("game").getRef().addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    listgame.clear();
                                    for (DataSnapshot game : snapshot.getChildren()) {
                                        listgame.add(game.getValue(game.class));
                                    }
                                    // Update the original list in the adapter
                                    libAdapter.setOriginalList(listgame);
                                    // Notify the adapter about the data set changes
                                    libAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    // Handle onCancelled
                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Handle onCancelled
                }
            });
        }
    }
}

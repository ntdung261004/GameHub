package com.example.pro1121_nhom3.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pro1121_nhom3.model.hoadon;
import com.example.pro1121_nhom3.repo.FirebaseRepo;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class FirebaseViewModel extends ViewModel implements FirebaseRepo.OnRealTimeDbTaskComplete {

    private MutableLiveData<ArrayList<hoadon>> BillMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<DatabaseError> databaseErrorMutableLiveData = new MutableLiveData<>();
    private FirebaseRepo firebaseRepo;

    public MutableLiveData<ArrayList<hoadon>> getBillMutableLiveData() {
        return BillMutableLiveData;
    }

    public MutableLiveData<DatabaseError> getDatabaseErrorMutableLiveData() {
        return databaseErrorMutableLiveData;
    }

    public FirebaseViewModel(){
        firebaseRepo = new FirebaseRepo(this);
    }

    public void getAllData(){
        firebaseRepo.getAllData();
    }


    @Override
    public void onSuccess(ArrayList<hoadon> listBill) {
        BillMutableLiveData.setValue(listBill);
    }

    @Override
    public void onFailure(DatabaseError error) {
        databaseErrorMutableLiveData.setValue(error);
    }
}

package com.example.pro1121_nhom3.model;

import java.util.ArrayList;
import java.util.List;

public class hoadon {
    private String ngaymua;
    private String nguoidung_tendangnhap;
    private float thanhtien;

    private ArrayList<game> game;

    public hoadon() {
    }

    public hoadon(String ngaymua, String nguoidung_tendangnhap, float thanhtien, ArrayList<game> listGame) {
        this.ngaymua = ngaymua;
        this.nguoidung_tendangnhap = nguoidung_tendangnhap;
        this.thanhtien = thanhtien;
        this.game = listGame;
    }

    public String getNgaymua() {
        return ngaymua;
    }

    public void setNgaymua(String ngaymua) {
        this.ngaymua = ngaymua;
    }



    public float getThanhtien() {
        return thanhtien;
    }

    public void setThanhtien(float thanhtien) {
        this.thanhtien = thanhtien;
    }

    public String getNguoidung_tendangnhap() {
        return nguoidung_tendangnhap;
    }

    public void setNguoidung_tendangnhap(String nguoidung_tendangnhap) {
        this.nguoidung_tendangnhap = nguoidung_tendangnhap;
    }

    public ArrayList<game> getListGame() {
        return game;
    }

    public void setListGame(ArrayList<game> listGame) {
        this.game = listGame;
    }
}

package com.example.pro1121_nhom3.model;

import java.util.ArrayList;
import java.util.List;

public class hoadon {
    private game game;
    private nguoidung nguoidung;
    private String ngaymua;
    private String mabill;

    public hoadon() {
    }

    public hoadon(com.example.pro1121_nhom3.model.game game, com.example.pro1121_nhom3.model.nguoidung nguoidung, String ngaymua) {
        this.game = game;
        this.nguoidung = nguoidung;
        this.ngaymua = ngaymua;
    }

    public String getMabill() {
        return mabill;
    }

    public void setMabill(String mabill) {
        this.mabill = mabill;
    }

    public com.example.pro1121_nhom3.model.game getGame() {
        return game;
    }

    public void setGame(com.example.pro1121_nhom3.model.game game) {
        this.game = game;
    }

    public com.example.pro1121_nhom3.model.nguoidung getNguoidung() {
        return nguoidung;
    }

    public void setNguoidung(com.example.pro1121_nhom3.model.nguoidung nguoidung) {
        this.nguoidung = nguoidung;
    }

    public String getNgaymua() {
        return ngaymua;
    }

    public void setNgaymua(String ngaymua) {
        this.ngaymua = ngaymua;
    }
}

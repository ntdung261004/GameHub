package com.example.pro1121_nhom3.model;

public class nguoidung {
    private String email;
    private game userGame;
    private String matkhau;
    private String tendangnhap;
    private int role;
    private String tennd;
    private double wallet;

    public nguoidung() {
    }

    public nguoidung(String email, game userGame, String matkhau, String tendangnhap, int role, String tennd, double wallet) {
        this.email = email;
        this.userGame = userGame;
        this.matkhau = matkhau;
        this.tendangnhap = tendangnhap;
        this.role = role;
        this.tennd = tennd;
        this.wallet = wallet;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public game getUserGame() {
        return userGame;
    }

    public void setUserGame(game userGame) {
        this.userGame = userGame;
    }

    public String getMatkhau() {
        return matkhau;
    }

    public void setMatkhau(String matkhau) {
        this.matkhau = matkhau;
    }

    public String getTendangnhap() {
        return tendangnhap;
    }

    public void setTendangnhap(String tendangnhap) {
        this.tendangnhap = tendangnhap;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getTennd() {
        return tennd;
    }

    public void setTennd(String tennd) {
        this.tennd = tennd;
    }

    public double getWallet() {
        return wallet;
    }

    public void setWallet(double wallet) {
        this.wallet = wallet;
    }
}

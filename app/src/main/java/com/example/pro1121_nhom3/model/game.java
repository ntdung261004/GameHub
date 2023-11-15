package com.example.pro1121_nhom3.model;

public class game {

    private int magame;
    private String tengame;
    private String nph;
    private float giaban;
    private int image;
    private loaigame loaigame;
    private String mota;
    private int likecount;

    public game() {
    }

    public game(int magame, String tengame, String nph, float giaban, int image, com.example.pro1121_nhom3.model.loaigame loaigame, String mota, int likecount) {
        this.magame = magame;
        this.tengame = tengame;
        this.nph = nph;
        this.giaban = giaban;
        this.image = image;
        this.loaigame = loaigame;
        this.mota = mota;
        this.likecount = likecount;
    }

    public int getMagame() {
        return magame;
    }

    public void setMagame(int magame) {
        this.magame = magame;
    }

    public String getTengame() {
        return tengame;
    }

    public void setTengame(String tengame) {
        this.tengame = tengame;
    }

    public String getNph() {
        return nph;
    }

    public void setNph(String nph) {
        this.nph = nph;
    }

    public float getGiaban() {
        return giaban;
    }

    public void setGiaban(float giaban) {
        this.giaban = giaban;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public com.example.pro1121_nhom3.model.loaigame getLoaigame() {
        return loaigame;
    }

    public void setLoaigame(com.example.pro1121_nhom3.model.loaigame loaigame) {
        this.loaigame = loaigame;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public int getLikecount() {
        return likecount;
    }

    public void setLikecount(int likecount) {
        this.likecount = likecount;
    }
}
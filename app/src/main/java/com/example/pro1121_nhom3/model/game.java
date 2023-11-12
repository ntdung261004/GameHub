package com.example.pro1121_nhom3.model;

public class game {

    private int magame;
    private String tengame;
    private String nph;
    private float giaban;
    private int image;
    private loaigame loaigame;

    public game() {
    }

    public game(int magame, String tengame, String nph, float giaban, int image, loaigame loaigame) {
        this.magame = magame;
        this.tengame = tengame;
        this.nph = nph;
        this.giaban = giaban;
        this.image = image;
        this.loaigame = loaigame;
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

    public loaigame getLoaigame() {
        return loaigame;
    }

    public void setLoaigame(loaigame loaigame) {
        this.loaigame = loaigame;
    }

    @Override
    public String toString() {
        return "game{" +
                "magame='" + magame + '\'' +
                ", tengame='" + tengame + '\'' +
                ", nph='" + nph + '\'' +
                ", giaban=" + giaban +
                ", image=" + image +
                ", loaigame=" + loaigame +
                '}';
    }
}

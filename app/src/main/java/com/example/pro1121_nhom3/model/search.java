package com.example.pro1121_nhom3.model;

public class search {
    private String tengame;
    private String img;

    private String magame;

    public String getTengame() {
        return tengame;
    }

    public void setTengame(String tengame) {
        this.tengame = tengame;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getMagame() {
        return magame;
    }

    public void setMagame(String magame) {
        this.magame = magame;
    }

    public search(String tengame, String img, String magame) {
        this.tengame = tengame;
        this.img = img;
        this.magame = magame;
    }

    public search() {
    }
}



package com.example.pro1121_nhom3.model;

public class search {
    private String tengame;
    private String img;

    public search() {
    }

    public search(String tengame, String img) {
        this.tengame = tengame;
        this.img = img;
    }

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
}

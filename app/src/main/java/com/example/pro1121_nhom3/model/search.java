package com.example.pro1121_nhom3.model;

public class search {
    private String name;
    private int imageResource;
    private int itemId;

    public search(String name, int imageResource, int itemId) {
        this.name = name;
        this.imageResource = imageResource;
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public int getItemId() {
        return itemId;
    }
}



package com.example.admin_server.Model;

public class Category {
    String Image;
    String Name;

    public Category(String image, String name) {
        Image = image;
        this.Name = name;
    }
    public Category(){}

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }
}
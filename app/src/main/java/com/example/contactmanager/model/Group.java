package com.example.contactmanager.model;
public class Group {
    private long id;
    private int image;
    private String name;
    private String type;
    public Group(long id, int image, String name) {
        this.id = id;
        this.image = image;
        this.name = name;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public int getImage() {
        return image;
    }
    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

package com.example.mycloudnotes;

public class User {
    private String title, desc,id;

    public User() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User(String title, String desc) {
        this.title = title;
        this.desc = desc;
    }
}

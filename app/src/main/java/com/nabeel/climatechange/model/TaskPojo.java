package com.nabeel.climatechange.model;

public class TaskPojo {
    private Long id;
    private String title;
    private String description;
    private String created_at;
    private String exp_date;
    private String img;

    public TaskPojo() {}

    public TaskPojo(Long id, String title, String description, String created_at, String exp_date, String img) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.created_at = created_at;
        this.exp_date = exp_date;
        this.img = img;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getExp_date() {
        return exp_date;
    }

    public void setExp_date(String exp_date) {
        this.exp_date = exp_date;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}

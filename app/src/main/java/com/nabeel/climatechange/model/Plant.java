package com.nabeel.climatechange.model;

public class Plant {
    private String plant_name;
    private String state_name;
    private String image;

    public Plant() {
    }

    public Plant(String plant_name, String state_name, String image) {
        this.plant_name = plant_name;
        this.state_name = state_name;
        this.image = image;
    }

    public String getPlant_name() {
        return plant_name;
    }

    public void setPlant_name(String plant_name) {
        this.plant_name = plant_name;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

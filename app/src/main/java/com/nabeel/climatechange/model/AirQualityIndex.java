package com.nabeel.climatechange.model;

import java.util.ArrayList;

public class AirQualityIndex {
    private String city_name;
    private String lon;
    private String timezone;
    private String lat;
    private String country_code;
    private String state_code;
    private ArrayList<AQIModelClass> data;

    public AirQualityIndex(String city_name, String lon, String timezone, String lat, String country_code, String state_code, ArrayList<AQIModelClass> data) {
        this.city_name = city_name;
        this.lon = lon;
        this.timezone = timezone;
        this.lat = lat;
        this.country_code = country_code;
        this.state_code = state_code;
        this.data = data;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getState_code() {
        return state_code;
    }

    public void setState_code(String state_code) {
        this.state_code = state_code;
    }

    public ArrayList<AQIModelClass> getData() {
        return data;
    }

    public void setData(ArrayList<AQIModelClass> data) {
        this.data = data;
    }
}

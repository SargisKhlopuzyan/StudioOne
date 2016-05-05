package com.example.sargis_kh.model;

import android.graphics.drawable.Drawable;

/**
 * Created by Sargis_Kh on 5/2/2016.
 */
public class Company {

//    - Identification number (id)
//    - Company Name (name)
//    - GPS Latitude (lat)
//    - GPS Longitude (lon)
//    - Company Logo URL (logo)

    private long id;
    private String name;
    private double lat;
    private double lon;
    private String logo;

    public Company(long id, String name, double lat, double lon, String logo) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.logo = logo;
    }

    public long getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return lat;
    }

    public double getLongitude() {
        return lon;
    }

    public String getLogo() {
        return logo;
    }


    public void setID(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLatitude(double lat) {
        this.lat = lat;
    }

    public void setLongitude(double lon) {
        this.lon = lon;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}

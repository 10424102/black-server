package com.example.models;

import javax.persistence.Embeddable;

@Embeddable
public class Location {
    private double longitude;
    private double latitude;

    //<editor-fold desc="=== Getters & Setters ===">

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    //</editor-fold>
}

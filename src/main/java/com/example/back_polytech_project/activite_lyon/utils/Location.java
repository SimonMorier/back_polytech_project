package com.example.back_polytech_project.activite_lyon.utils;

public class Location {
    private Long id;
    private double lat;
    private double lon;

    
    public Location(Long id, double lat, double lon) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public double getLat() {
        return lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }
    public double getLon() {
        return lon;
    }
    public void setLon(double lon) {
        this.lon = lon;
    }

}

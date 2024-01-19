package com.example.back_polytech_project.activite_lyon.utils;

import java.util.Objects;

//Objet qui sert au transfert des données lorqu'on ne s'interesse qu'aux coord (ex map)

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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Location location = (Location) o;
        return id == location.id &&
                Double.compare(location.lon, lon) == 0 &&
                Double.compare(location.lat, lat) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lon, lat);
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", lon=" + lon +
                ", lat=" + lat +
                '}';
    }

}

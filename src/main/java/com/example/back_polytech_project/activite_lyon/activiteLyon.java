package com.example.back_polytech_project.activite_lyon;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class activiteLyon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String theme;
    private String type;
    private String address;
    private String descrcourtfr;
    private String descrdetailfr;
    private String contact;
    private String ouvertureenclair;
    private String tarifsenclair;
    private Double tarifmin;
    private Double tarifmax;
    private String modepaiemt;
    private String illustrations;
    private Double lon;
    private Double lat;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getTheme() {
        return theme;
    }
    public void setTheme(String theme) {
        this.theme = theme;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getDescrcourtfr() {
        return descrcourtfr;
    }
    public void setDescrcourtfr(String descrcourtfr) {
        this.descrcourtfr = descrcourtfr;
    }
    public String getDescrdetailfr() {
        return descrdetailfr;
    }
    public void setDescrdetailfr(String descrdetailfr) {
        this.descrdetailfr = descrdetailfr;
    }
    public String getContact() {
        return contact;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }
    public String getOuvertureenclair() {
        return ouvertureenclair;
    }
    public void setOuvertureenclair(String ouvertureenclair) {
        this.ouvertureenclair = ouvertureenclair;
    }
    public String getTarifsenclair() {
        return tarifsenclair;
    }
    public void setTarifsenclair(String tarifsenclair) {
        this.tarifsenclair = tarifsenclair;
    }
    public Double getTarifmin() {
        return tarifmin;
    }
    public void setTarifmin(Double tarifmin) {
        this.tarifmin = tarifmin;
    }
    public Double getTarifmax() {
        return tarifmax;
    }
    public void setTarifmax(Double tarifmax) {
        this.tarifmax = tarifmax;
    }
    public String getModepaiemt() {
        return modepaiemt;
    }
    public void setModepaiemt(String modepaiemt) {
        this.modepaiemt = modepaiemt;
    }
    public String getIllustrations() {
        return illustrations;
    }
    public void setIllustrations(String illustrations) {
        this.illustrations = illustrations;
    }
    public Double getLon() {
        return lon;
    }
    public void setLon(Double lon) {
        this.lon = lon;
    }
    public Double getLat() {
        return lat;
    }
    public void setLat(Double lat) {
        this.lat = lat;
    }
    
}

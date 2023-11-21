package com.example.back_polytech_project.activite_lyon.utils;

public class Utils {
    // Méthode pour calculer la distance haversine entre deux points géographiques
    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        // Rayon de la Terre en kilomètres
        final double R = 6371.0;

        // Conversion des coordonnées en radians
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        // Formule haversine
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Distance haversine
        return R * c;
    }
}

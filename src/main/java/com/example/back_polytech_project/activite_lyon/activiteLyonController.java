package com.example.back_polytech_project.activite_lyon;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.back_polytech_project.activite_lyon.utils.ApiResponse;
import com.example.back_polytech_project.activite_lyon.utils.Location;

@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")

// Ce fichier repertorie les differents endpoint qui sont utilisable sur notre
// base de donnée.
@RestController
@RequestMapping(path = "api/v1/activite")
public class activiteLyonController {

    private activiteLyonService activiteLyonService;

    public activiteLyonController(activiteLyonService activiteLyonService) {
        this.activiteLyonService = activiteLyonService;
    }

    // Retourne toutes les activité en DB
    @GetMapping
    public ApiResponse getAllActivite(@RequestParam(required = false) Optional<Boolean> withTarifs) {
        return new ApiResponse(activiteLyonService.getActiviteLyon(withTarifs));

    }

    // Retourne l'activité avec un id donnée en DB
    @GetMapping(path = "{activiteId}")
    public ApiResponse getActiviteById(
            @PathVariable Long activiteId) {
        activiteLyon activiteLyonRequested = activiteLyonService.getActiviteLyonById(activiteId);
        return new ApiResponse(activiteLyonRequested);
    }

    // Retourne un tableau de l'ensemble des coordonnées des activités en DB
    @GetMapping(path = "coords")
    public ApiResponse getCoordsArray() {
        List<Location> locationArray = activiteLyonService.getCoordsArray();
        return new ApiResponse(locationArray);
    }

    // Retourne les differents types d'activité present dans notre DB
    @GetMapping(path = "theme")
    public ApiResponse getTheme() {
        return new ApiResponse(activiteLyonService.getThemeArray());
    }

    // Retourne les coordonnées des activité filtré dans un rayon en Km par rapport
    // à un point
    @GetMapping(path = "coords/filtred/InRadius")
    public ApiResponse getCooFiltredInRadius(
            @RequestParam(required = true) Double lat,
            @RequestParam(required = true) Double lon,
            @RequestParam(required = true) Double radius) {
        return new ApiResponse(activiteLyonService.getActiviteLyonFiltredByRadius(lat, lon, radius));
    }

    // Retourne les coordonnées des axctivité filtré dans une fourchette de prix
    @GetMapping(path = "coords/filtred/InPriceRange")
    public ApiResponse getCooFiltredInPriceRange(
            @RequestParam(required = true) Double max,
            @RequestParam(required = true) Double min,
            @RequestParam(required = true) Boolean isPriceRequired) {
        return new ApiResponse(activiteLyonService.getCooFiltredInPriceRange(max, min, isPriceRequired));
    }

    // Retourne les coordonnées des activités filtrés par type d'activité donnée
    @GetMapping(path = "coords/filtred/ByActivityTheme")
    public ApiResponse getCooFiltredByActivityTheme(
            @RequestParam(required = true) List<String> activityTheme) {
        return new ApiResponse(activiteLyonService.getCooFiltredByActivityTheme(activityTheme));
    }

    // Retourne les ACTIVITES filtrées par type d'activité donnée
    @GetMapping(path = "activite/filtred/ByActivityTheme")
    public ApiResponse getActiviteFiltredByActivityTheme(
            @RequestParam(required = true) List<String> activityTheme) {
        return new ApiResponse(activiteLyonService.getActiviteFiltredByActivityTheme(activityTheme));
    }

    // Retourne les coordonnées des activités filtrés par rayon, fourchette de prix,
    // et type d'activité
    @GetMapping(path = "coords/filtred")
    public ApiResponse getCooFiltred(
            @RequestParam(required = false) Optional<Double> lat,
            @RequestParam(required = false) Optional<Double> lon,
            @RequestParam(required = false) Optional<Double> radius,
            @RequestParam(required = false) Optional<List<String>> activityTheme,
            @RequestParam(required = false) Optional<Double> max,
            @RequestParam(required = false) Optional<Double> min,
            @RequestParam(required = false) Optional<Boolean> isPriceRequired) {
        return new ApiResponse(
                activiteLyonService.getCooFiltred(lat, lon, radius, activityTheme, max, min, isPriceRequired));
    }

    // Retourne les activité de type donné dont le nom comprend le string Keyword
    @GetMapping("/search")
    public ApiResponse searchLocations(
            @RequestParam String keyword,
            @RequestParam(required = true) List<String> activityTheme) {
        List<activiteLyon> searchResult = activiteLyonService.searchActiviteLyonWithCategories(keyword, activityTheme);
        return new ApiResponse(searchResult);
    }

}
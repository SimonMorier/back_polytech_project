package com.example.back_polytech_project.activite_lyon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.server.ResponseStatusException;

import com.example.back_polytech_project.activite_lyon.utils.Location;
import com.example.back_polytech_project.activite_lyon.utils.Utils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.micrometer.common.util.StringUtils;

@CrossOrigin(origins ="http://localhost:4200", allowedHeaders = "*")
@Service
public class activiteLyonService {
    private final activiteLyonRepository activiteLyonRepository;
    @Autowired
    public activiteLyonService(activiteLyonRepository activiteLyonRepository){
        this.activiteLyonRepository = activiteLyonRepository;
    }

    public List<activiteLyon> getActiviteLyon(Optional<Boolean> filterTarifs){
        List<activiteLyon> list = new ArrayList<>();
        if(filterTarifs.isPresent() && filterTarifs.get()){
            return activiteLyonRepository.findActivteLyonWithPricesNotNull();
        }else{
            list = activiteLyonRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
            return list;
    }
        
    }

    public activiteLyon getActiviteLyonById(Long id){
        return activiteLyonRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "ActiviteLyon id: " + id + " does not exist"));
    }

    public List<Location> getCoordsArray() {
        List<activiteLyon> list = activiteLyonRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

        List<Location> result = list.stream()
            .map(activite -> new Location(activite.getId(), activite.getLat(), activite.getLon()))
            .collect(Collectors.toList());

        return result;
}

    public List<Location> getActiviteLyonFiltredByRadius(Double lat, Double lon, Double radius) {
        List<Location> ListeCoordonneesEnDB = this.getCoordsArray();

         // Filtrer les locations dans le rayon en utilisant la distance haversine
         List<Location> result = ListeCoordonneesEnDB.stream()
                    .filter(location -> Utils.haversine(lat, lon, location.getLat(), location.getLon()) <= radius)
                    .collect(Collectors.toList());

        return result;
    }

    public List<Location> getCooFiltredInPriceRange(Double max, Double min, Boolean isPricerequired) {
        Optional<Boolean> avecTarifs = Optional.of(isPricerequired);
        List<activiteLyon> activiteLyonsInDb = this.getActiviteLyon(avecTarifs);
       
        
        List<activiteLyon> filtredList = activiteLyonsInDb.stream()
                .filter(activiteLyon ->  {
                    // Inclure les activités avec des champs min et max null si isPriceRequired est à false
                    if (!isPricerequired) {
                        return true;
                    }
                    // Sinon, appliquer le filtrage normal
                    return activiteLyon.getTarifmax() != null && activiteLyon.getTarifmin() <= max
                            && activiteLyon.getTarifmin() != null && activiteLyon.getTarifmin() > min;
                })
                .collect(Collectors.toList());
    
        List<Location> result = this.getCoordsArray().stream()
                .filter(location -> filtredList.stream().anyMatch(activiteLyon -> activiteLyon.getId() == location.getId()))
                .collect(Collectors.toList());
    
        return result;
    }

    public List<Location> getCooFiltredByActivityTheme(List<String> activityThemes) {
       // Remplacer " et " par "&" dans chaque élément de la liste
        List<String> modifiedActivityThemes = activityThemes.stream()
            .map(theme -> theme.replace(" et ", "&"))
            .collect(Collectors.toList());
        List<activiteLyon> activiteLyonsInDb = this.getActiviteLyon(Optional.empty());
        List<activiteLyon> filtredList = activiteLyonsInDb.stream()
                .filter(activiteLyon -> {
                    try {
                        String[]  themeArray = activiteLyon.getTheme().replaceAll("[\\[\\]']", "").split(",\\s*");
                        List<String> themeList = Arrays.asList(themeArray);
                        return themeList.stream().anyMatch(modifiedActivityThemes::contains);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                })
                .collect(Collectors.toList());
    
        List<Location> result = this.getCoordsArray().stream()
                .filter(location -> filtredList.stream().anyMatch(activiteLyon -> activiteLyon.getId() == location.getId()))
                .collect(Collectors.toList());

        return result;
    }

    //Recevoir array d'activite
    //Ne pas avoir de filtrage si array vide.
    

    public List<Location> getCooFiltred(Optional<Double> lat,
                                Optional<Double> lon, 
                                Optional<Double> radius,
                                Optional<List<String>>activityTheme,
                                Optional<Double> max,
                                Optional<Double> min,
                                Optional<Boolean> isPriceRequired) {

        List<Location> list = this.getCoordsArray();
        System.out.println(list.size());
        if(activityTheme.isPresent()){
            list = Utils.filterIntersection(list,this.getCooFiltredByActivityTheme(activityTheme.get()));
        }
        if(max.isPresent() && min.isPresent()){
            list = Utils.filterIntersection(list,this.getCooFiltredInPriceRange(max.get(), min.get(), isPriceRequired.get()));
        }
        if(lat.isPresent() && lon.isPresent() && radius.isPresent()){
            list = Utils.filterIntersection(list,this.getActiviteLyonFiltredByRadius(lat.get(), lon.get(), radius.get()));
        }                            
        return list;
    }

    public List<String> getThemeArray() {       
        List<String> themeList = activiteLyonRepository.findDistinctThemes();
        return themeList.stream()
        // Supprimer les caractères non nécessaires et diviser les thèmes en une liste
        .map(theme -> theme.replaceAll("[\\[\\]']", "").split(",\\s*"))
        // Aplatir la liste de listes de thèmes
        .flatMap(array -> Arrays.stream(array))
        // Supprimer les espaces supplémentaires
        .map(String::trim)
        // Remplacer '@' par " et "
        .map(theme -> theme.replace("&", " et "))
        // Filtrer les thèmes vides
        .filter(theme -> !theme.isEmpty())
        // Collecter les thèmes distincts
        .distinct()
        // Collecter les résultats dans une liste
        .collect(Collectors.toList());
    }

    public List<activiteLyon> getActiviteFiltredByActivityTheme(List<String> activityThemes) {
       // Remplacer " et " par "&" dans chaque élément de la liste
        List<String> modifiedActivityThemes = activityThemes.stream()
            .map(theme -> theme.replace(" et ", "&"))
            .collect(Collectors.toList());
        List<activiteLyon> activiteLyonsInDb = this.getActiviteLyon(Optional.empty());
        List<activiteLyon> filtredList = activiteLyonsInDb.stream()
                .filter(activiteLyon -> {
                    try {
                        String[]  themeArray = activiteLyon.getTheme().replaceAll("[\\[\\]']", "").split(",\\s*");
                        List<String> themeList = Arrays.asList(themeArray);
                        return themeList.stream().anyMatch(modifiedActivityThemes::contains);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                })
                .collect(Collectors.toList());
        
        if(!modifiedActivityThemes.isEmpty()){
            return filtredList;
        }
        else {
            return activiteLyonsInDb;
        }
        
    }
    


    public List<activiteLyon> searchActiviteLyons(String keyword) {
        // Ajoutez une vérification pour les chaînes vides ou nulles
        if (StringUtils.isBlank(keyword)) {
            // Vous pouvez choisir de retourner toutes les activités ou une liste vide
            return activiteLyonRepository.findAll();
        }

        // Implémentez la logique de recherche en utilisant le repository
        return activiteLyonRepository.findByNomContainingIgnoreCase(keyword);
    }


    public List<activiteLyon> searchActiviteLyonWithCategories(String keyword,List<String> activityThemes){
        List<activiteLyon> ListActByTheme = this.getActiviteFiltredByActivityTheme(activityThemes);
        List<activiteLyon> ListWithKeyWord = this.searchActiviteLyons(keyword);

        return Utils.filterIntersectionAct(ListActByTheme, ListWithKeyWord);
    
    }
}


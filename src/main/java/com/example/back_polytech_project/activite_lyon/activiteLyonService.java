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
        if(filterTarifs.isPresent()){
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

    public List<Location> getCooFiltredInPriceRange(Double max, Double min) {
        Optional<Boolean> tarifs = Optional.of(true);
        List<activiteLyon> activiteLyonsInDb = this.getActiviteLyon(tarifs);
    
        List<activiteLyon> filtredList = activiteLyonsInDb.stream()
                .filter(activiteLyon -> activiteLyon.getTarifmax() <= max && activiteLyon.getTarifmin() > min)
                .collect(Collectors.toList());
    
        List<Location> result = this.getCoordsArray().stream()
                .filter(location -> filtredList.stream().anyMatch(activiteLyon -> activiteLyon.getId() == location.getId()))
                .collect(Collectors.toList());
    
        return result;
    }

    public List<Location> getCooFiltredByActivityTheme(String activityType) {
        List<activiteLyon> activiteLyonsInDb = this.getActiviteLyon(Optional.empty());
        List<activiteLyon> filtredList = activiteLyonsInDb.stream()
                .filter(activiteLyon -> {
                    try {
                        String[]  themeArray = activiteLyon.getTheme().replaceAll("[\\[\\]']", "").split(",\\s*");
                        List<String> themeList = Arrays.asList(themeArray);
                        return themeList.contains(activityType);
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

    
}

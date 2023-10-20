package com.example.back_polytech_project.activite_lyon;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.server.ResponseStatusException;

import com.example.back_polytech_project.activite_lyon.utils.Location;

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
}

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


@CrossOrigin(origins ="http://localhost:4200", allowedHeaders = "*")

@RestController
@RequestMapping(path="api/v1/activite")
public class activiteLyonController{

    private activiteLyonService activiteLyonService;


    public activiteLyonController(activiteLyonService activiteLyonService){
        this.activiteLyonService = activiteLyonService;
    }


    @GetMapping
    public ApiResponse getAllActivite(@RequestParam(required = false) Optional<Boolean> withTarifs){
        return new ApiResponse(activiteLyonService.getActiviteLyon(withTarifs));

    }

    @GetMapping(path="{activiteId}")
    public ApiResponse getActiviteById(
        @PathVariable Long activiteId
        ){
            activiteLyon activiteLyonRequested = activiteLyonService.getActiviteLyonById(activiteId);
            return new ApiResponse(activiteLyonRequested);
    }

    @GetMapping(path="coords")
    public ApiResponse getCoordsArray(){
        List<Location> locationArray = activiteLyonService.getCoordsArray();
        return new ApiResponse(locationArray);
    }
    

    
    @GetMapping(path="coords/filtred/InRadius")
    public ApiResponse getCooFiltredInRadius(
        @RequestParam(required = true) Double lat,
        @RequestParam(required = true) Double lon,
        @RequestParam(required = true) Double radius
    )
    {
        return new ApiResponse(activiteLyonService.getActiviteLyonFiltredByRadius(lat, lon, radius));
    }


    @GetMapping(path="coords/filtred/InPriceRange")
        public ApiResponse getCooFiltredInPriceRange(
            @RequestParam(required = true) Double max,
            @RequestParam(required = true) Double min
        ){
            return new ApiResponse(activiteLyonService.getCooFiltredInPriceRange(max,min));
        }

    @GetMapping(path="coords/filtred/ByActivityTheme")
        public ApiResponse getCooFiltredByActivityTheme(
            @RequestParam(required = true) String activityTheme
        ){
            return new ApiResponse(activiteLyonService.getCooFiltredByActivityTheme(activityTheme));
        }

    //GET not empty prices 
    //GET liste activites with prices in interval
}
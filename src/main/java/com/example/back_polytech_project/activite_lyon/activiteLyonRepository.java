package com.example.back_polytech_project.activite_lyon;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



public interface activiteLyonRepository extends JpaRepository<activiteLyon,Long> {
    @Query("SELECT a FROM activiteLyon a WHERE a.tarifmin IS NOT NULL AND a.tarifmax IS NOT NULL")
    List<activiteLyon> findActivteLyonWithPricesNotNull();

    @Query("SELECT DISTINCT a.theme FROM activiteLyon a")
    List<String> findDistinctThemes();

    List<activiteLyon> findByNomContainingIgnoreCase(String keyword);
}

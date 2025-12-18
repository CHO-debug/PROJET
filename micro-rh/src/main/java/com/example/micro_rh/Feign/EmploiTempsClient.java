package com.example.micro_rh.Feign;
import com.example.micro_rh.dto.EmploiTempsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "micro-emploi", url = "http://localhost:8085")
public interface EmploiTempsClient {

    // Lister tous les emplois créés par le RH connecté
    @GetMapping("/emplois/rh/me")
    List<EmploiTempsDTO> getEmploisByRh(@RequestHeader("X-User-Id") String rhId);

    // Créer un emploi pour un employé
    @PostMapping("/emplois/rh/create")
   EmploiTempsDTO createEmploi(
            @RequestHeader("X-User-Id") String rhId,
            @RequestBody EmploiTempsDTO emploiTemps
    );

    @DeleteMapping("/rh/delete")
    boolean deleteEmploiByTache(
            @RequestHeader("X-User-Id") String rhId,
            @RequestParam String tache
    );

    // Lister les emplois d’un employé spécifique (DTO simplifié)
    @GetMapping("/emplois/me")
    List<EmploiTempsDTO> getEmploiByEmploye(@RequestHeader("X-User-Id") String employeId);
}
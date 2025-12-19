package com.example.micro_rh.Feign;

import com.example.micro_rh.dto.AbsenceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "absence-service", url = "http://localhost:8084")
public interface AbsenceFeignClient {

    /**
     * Récupère les absences créées par le RH via son ID passé dans l'URL.
     */
    @GetMapping("/absences/created-by-me/{rhId}")
    List<AbsenceDTO> getAbsencesByRh(@PathVariable("rhId") String rhId);

    /**
     * Crée une absence pour un RH, avec l'ID du RH dans l'URL.
     */
    @PostMapping("/absences/create-by-rh/{rhId}")
    AbsenceDTO createAbsenceByRh(
            @PathVariable("rhId") String rhId,
            @RequestBody AbsenceDTO dto
    );
}

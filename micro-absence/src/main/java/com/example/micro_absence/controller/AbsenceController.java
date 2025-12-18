package com.example.micro_absence.controller;

import com.example.micro_absence.Dto.AbsenceDto;
import com.example.micro_absence.Dto.AbsenceResponseDto;
import com.example.micro_absence.Service.AbsenceService;
import com.example.micro_absence.Dto.AbsenceDto;
import com.example.micro_absence.entities.Absence;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/absences")
@RequiredArgsConstructor
public class AbsenceController {

    private final AbsenceService service;

    // Vérifier si le microservice fonctionne
    @GetMapping
    public String serviceStatus() {
        return "Microservice Absence is running";
    }

    // Créer une absence
    @PostMapping
    public Absence createAbsence(@RequestBody Absence absence) {
        return service.createAbsence(absence);
    }

    // Modifier une absence
    @PutMapping("/{id}")
    public Absence updateAbsence(@PathVariable String id, @RequestBody Absence updatedAbsence) {
        return service.updateAbsence(id, updatedAbsence);
    }

    // Supprimer une absence
    @DeleteMapping("/{id}")
    public void deleteAbsence(@PathVariable String id) {
        service.deleteAbsence(id);
    }

    // Liste complète des absences (RH)
    @GetMapping("/all")
    public List<Absence> getAllAbsences() {
        return service.getAllAbsences();
    }

    @GetMapping("/me/{id}")
    public ResponseEntity<List<AbsenceResponseDto>> getMyAbsences(@PathVariable String id) {

        return ResponseEntity.ok(
                service.getAbsencesByEmployeeForFrontend(id)
        );
    }

    // -------------------------------------------------
    // DATA ANALYZER : Retourne JUSTIFIE / NON_JUSTIFIE
    // -------------------------------------------------

}
package com.example.micro_emploi.Controller;

import com.example.micro_emploi.dto.CreateEmploiDTO;
import com.example.micro_emploi.dto.EmploiTempsSimpleDTO;
import com.example.micro_emploi.dto.RHEmploiDTO;
import com.example.micro_emploi.entities.EmploiTemps;
import com.example.micro_emploi.services.EmploiTempsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Gestion des emplois du temps", description = "Endpoints pour gérer les emplois du temps")
@RestController
@RequestMapping("/emplois")
public class EmploiTempsController {

    @Autowired
    private EmploiTempsService emploiTempsService;

    @Operation(summary = "Lister les plannings de l'employé connecté")
    @GetMapping("/me/{id}") public ResponseEntity<List<EmploiTempsSimpleDTO>> getMyEmploi( @PathVariable String id) { List<EmploiTempsSimpleDTO> emplois = emploiTempsService.getEmploiByEmployeAsSimpleDTO(id);
        return ResponseEntity.ok(emplois); }
    @Operation(summary = "Lister les plannings des employés supervisés par le RH")
    @GetMapping("/rh/{rhId}")
    public ResponseEntity<List<RHEmploiDTO>> getEmploisByRh(
            @PathVariable String rhId) {

        List<EmploiTemps> emplois = emploiTempsService.getEmploisByRh(rhId);

        if (emplois.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<RHEmploiDTO> dtoList = emplois.stream()
                .map(e -> new RHEmploiDTO(
                        e.getId(),
                        e.getDate() != null ? e.getDate().toString() : "",
                        e.getHeure(),
                        e.getTache(),
                        e.getTaches(),
                        e.getEmployeId()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

    @PostMapping("/rh/{rhId}/employe/{employeId}/emploi")
    public ResponseEntity<RHEmploiDTO> createEmploiByRh(
            @PathVariable String rhId,
            @PathVariable String employeId,
            @RequestBody CreateEmploiDTO dto) {

        EmploiTemps emploi = new EmploiTemps();
        emploi.setDate(dto.getDate());
        emploi.setHeure(dto.getHeure());
        emploi.setTache(dto.getTache());
        emploi.setEmployeId(employeId);

        EmploiTemps created = emploiTempsService.createEmploi(rhId, emploi);

        RHEmploiDTO response = new RHEmploiDTO(

                created.getDate() != null ? created.getDate().toString() : "",
                created.getHeure(),
                created.getTache(),
                created.getTaches(),
                created.getEmployeId()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



    @Operation(summary = "Supprimer un emploi d'un employé")
    @DeleteMapping("/{emploiId}")
    public ResponseEntity<Void> deleteEmploi(

            @PathVariable String emploiId) {

        boolean deleted = emploiTempsService.deleteEmploi(emploiId);

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}



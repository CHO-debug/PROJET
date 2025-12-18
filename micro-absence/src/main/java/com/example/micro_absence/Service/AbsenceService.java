package com.example.micro_absence.Service;

import com.example.micro_absence.Dto.AbsenceDto;
import com.example.micro_absence.Dto.AbsenceDto;
import com.example.micro_absence.Dto.AbsenceResponseDto;
import com.example.micro_absence.entities.Absence;
import com.example.micro_absence.entities.TypeAbsence;
import com.example.micro_absence.repositories.AbsenceRepository;
import com.example.micro_absence.repositories.TypeAbsenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AbsenceService {

    private final AbsenceRepository repo;
    private final TypeAbsenceRepository typeRepo; // Pour le Data Analyzer

    // -------------------------------------------------
    // CREATE
    // -------------------------------------------------
    public Absence createAbsence(Absence absence) {
        return repo.save(absence);
    }

    // -------------------------------------------------
    // READ : liste complète
    // -------------------------------------------------
    public List<Absence> getAllAbsences() {
        return repo.findAll();
    }

    // -------------------------------------------------
    // READ : 1 absence par ID
    // -------------------------------------------------
    public Absence getAbsenceById(String id) {
        return repo.findById(id).orElse(null);
    }

    // -------------------------------------------------
    // READ : absences par employé
    // -------------------------------------------------
    public List<Absence> getAbsencesByEmployee(String employeId) {
        return repo.findByEmployeId(employeId);
    }

    // -------------------------------------------------
    // UPDATE
    // -------------------------------------------------
    public Absence updateAbsence(String id, Absence updated) {
        Absence existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Absence non trouvée"));

        existing.setDate(updated.getDate());
        existing.setTypeId(updated.getTypeId());
        existing.setEmployeId(updated.getEmployeId());

        return repo.save(existing);
    }

    // -------------------------------------------------
    // DELETE
    // -------------------------------------------------
    public void deleteAbsence(String id) {
        repo.deleteById(id);
    }

    // -------------------------------------------------
    // DATA ANALYZER : JUSTIFIE / NON_JUSTIFIE
    // -------------------------------------------------

    // Récupérer les absences pour le frontend (intervalle + libelle + motif + durée)
    public List<AbsenceResponseDto> getAbsencesByEmployeeForFrontend(String employeId) {
        List<Absence> absences = repo.findByEmployeId(employeId);
        List<AbsenceResponseDto> result = new ArrayList<>();

        for (Absence absence : absences) {
            // Récupération du type d'absence
            TypeAbsence type = typeRepo.findById(absence.getTypeId())
                    .orElseThrow(() -> new RuntimeException("TypeAbsence introuvable pour id " + absence.getTypeId()));

            AbsenceResponseDto dto = new AbsenceResponseDto();

            LocalDate start = absence.getDate(); // LocalDate direct

            int duree = absence.getDuree();
            if (duree > 1) {
                LocalDate end = start.plusDays(duree - 1);
                dto.setDateInterval(start + " à " + end);
            } else {
                dto.setDateInterval(start.toString());
            }

            dto.setLibelle(type.getLibelle());
            dto.setMotif(type.isJustificationRequise() ? absence.getMotif() : "NONE");
            dto.setDuree(duree);

            result.add(dto);
        }

        return result;
    }



}
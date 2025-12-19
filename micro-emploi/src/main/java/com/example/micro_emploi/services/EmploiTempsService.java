package com.example.micro_emploi.services;

import com.example.micro_emploi.dto.EmploiTempsSimpleDTO;
import com.example.micro_emploi.entities.EmploiTemps;
import com.example.micro_emploi.repositories.EmploiTempsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmploiTempsService {

    @Autowired
    private EmploiTempsRepository emploiTempsRepository;

    // =========================
    // Récupérer les emplois par RH
    // =========================
    public List<EmploiTemps> getEmploisByRh(String rhId) {
        List<EmploiTemps> byIdRh = emploiTempsRepository.findByIdRh(rhId);
        return byIdRh;
    }

    // =========================
    // Créer un emploi
    // =========================
    public EmploiTemps createEmploi(String rhId, EmploiTemps emploiTemps) {
        emploiTemps.setIdRh(rhId); // Associer l'emploi au RH connecté
        return emploiTempsRepository.save(emploiTemps);
    }

    // =========================
    // Supprimer les emplois d’un RH pour une tâche donnée
    // =========================
    public boolean deleteEmploiByTache(String rhId, String tache) {
        // Récupérer tous les emplois de ce RH avec cette tâche
        List<EmploiTemps> emplois = emploiTempsRepository.findByIdRhAndTache(rhId, tache);

        if (emplois.isEmpty()) {
            // Aucun emploi trouvé pour ce RH et cette tâche
            return false;
        }

        // Supprimer tous les emplois trouvés
        emploiTempsRepository.deleteAll(emplois);
        return true;
    }

    // =========================
    // Récupérer les emplois d’un employé sous forme DTO simplifié
    // =========================
    public List<EmploiTempsSimpleDTO> getEmploiByEmployeAsSimpleDTO(String employeId) {
        return emploiTempsRepository.findByEmployeId(employeId)
                .stream()
                .map(e -> {
                    EmploiTempsSimpleDTO dto = new EmploiTempsSimpleDTO();
                    dto.setDate(e.getDate());   // LocalDate → LocalDate, ok
                    dto.setHeure(e.getHeure()); // String
                    dto.setTache(e.getTache()); // String
                    return dto;
                })
                .collect(Collectors.toList());
    }
    public boolean deleteEmploi( String emploiId) {
        // Récupérer l'emploi par son id
        Optional<EmploiTemps> emploiOpt = emploiTempsRepository.findById(emploiId);
        List<EmploiTemps> emploes = emploiTempsRepository.findAll();

        if (emploiOpt.isPresent()) {
            emploiTempsRepository.delete(emploiOpt.get());

            return true;
        } else {
            System.out.println("Emploi non trouvé : " + emploiId);
            return false;
        }
    }

}
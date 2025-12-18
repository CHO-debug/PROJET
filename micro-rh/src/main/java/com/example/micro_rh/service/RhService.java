package com.example.micro_rh.service;

import com.example.micro_rh.Feign.AbsenceFeignClient;
import com.example.micro_rh.Feign.DemandeCongeClient;
import com.example.micro_rh.Feign.EmploiTempsClient;
import com.example.micro_rh.dto.AbsenceDTO;
import com.example.micro_rh.dto.DemandeCongeRHDto;
import com.example.micro_rh.dto.EmploiTempsDTO;
import com.example.micro_rh.entities.RH;
import com.example.micro_rh.repository.RHRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RhService {

    private final RHRepository repository;
    private final PasswordEncoder passwordEncoder; // injecté par Spring
    private final AbsenceFeignClient absenceFeignClient;
    private final DemandeCongeClient demandeCongeClient;
    private final EmploiTempsClient emploiTempsClient;
    // ----------------------------
    // Enregistrement d'un RH
    // ----------------------------
    public RH register(RH rh) {
        if (repository.existsByEmail(rh.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }
        // Encoder le mot de passe avant de sauvegarder
        rh.setPassword(passwordEncoder.encode(rh.getPassword()));
        return repository.save(rh);
    }

    // ----------------------------
    // Authentification RH
    // ----------------------------
    public Optional<RH> login(String email, String password) {
        return repository.findByEmail(email)
                .filter(rh -> passwordEncoder.matches(password, rh.getPassword()));
    }



    public List<AbsenceDTO> getAbsencesCreatedByRh(String rhId) {
        // ✅ Appel via Feign Client sur le microservice Absence
        return absenceFeignClient.getAbsencesByRh(rhId);
    }

    // ------------------- CREER UNE ABSENCE -------------------
    public AbsenceDTO createAbsenceByRh(String rhId, AbsenceDTO dto) {
        return absenceFeignClient.createAbsenceByRh(rhId, dto);
    }

    public List<DemandeCongeRHDto> getDemandesEnAttentePourRH() {
        // Récupérer les demandes depuis micro_conge
        List<DemandeCongeRHDto> remoteList = demandeCongeClient.getDemandesEnAttente();

        // Mapper vers DTO local RH (4 champs)
        return remoteList.stream()
                .map(d -> new DemandeCongeRHDto(
                        d.getDateDebut(),  // LocalDate déjà correct
                        d.getDateFin(),
                        d.getMotif(),
                        d.getStatut()
                ))
                .collect(Collectors.toList());
    }


    // Accepter une demande
    public DemandeCongeRHDto accepterDemande(String demandeId) {
        return demandeCongeClient.accepterDemande(demandeId);
    }

    // Refuser une demande
    public DemandeCongeRHDto refuserDemande(String demandeId) {
        return demandeCongeClient.refuserDemande(demandeId);
    }
    // Lister tous les emplois créés par le RH connecté
    public List<EmploiTempsDTO> getEmploisByRh(String rhId) {
        return emploiTempsClient.getEmploisByRh(rhId);
    }

    // Créer un emploi pour un employé
    public EmploiTempsDTO createEmploi(String rhId, EmploiTempsDTO emploiTemps) {
        return emploiTempsClient.createEmploi(rhId, emploiTemps);
    }

    public boolean deleteEmploiByTache(String rhId, String tache) {
        System.out.println("Suppression demandé par RH : " + rhId + " pour tache : " + tache);
        return emploiTempsClient.deleteEmploiByTache(rhId, tache);
    }

    // Lister les emplois d’un employé
    public List<EmploiTempsDTO> getEmploiByEmploye(String employeId) {
        return emploiTempsClient.getEmploiByEmploye(employeId);
    }
}

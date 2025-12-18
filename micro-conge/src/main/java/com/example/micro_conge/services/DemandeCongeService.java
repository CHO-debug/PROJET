package com.example.micro_conge.services;

import com.example.micro_conge.dto.DemandeCongeDTO;
import com.example.micro_conge.dto.DemandeCongeRHDto;
import com.example.micro_conge.entities.DemandeConge;
import com.example.micro_conge.entities.StatutDemande;
import com.example.micro_conge.repositories.DemandeCongeRepository;
import com.example.micro_conge.repositories.StatutDemandeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DemandeCongeService {

    private final DemandeCongeRepository demandeCongeRepository;
    private final StatutDemandeRepository statutDemandeRepository;

    public DemandeCongeService(DemandeCongeRepository demandeCongeRepository,
                               StatutDemandeRepository statutDemandeRepository) {
        this.demandeCongeRepository = demandeCongeRepository;
        this.statutDemandeRepository = statutDemandeRepository;
    }

    // =====================================================
    // 👤 EMPLOYÉ → MES DEMANDES DE CONGÉ
    // =====================================================

    public List<DemandeCongeDTO> getDemandeCongeByEmployeId(String employeId) {

        return demandeCongeRepository.findByEmployeId(employeId)
                .stream()
                .map(demande -> {

                    DemandeCongeDTO dto = new DemandeCongeDTO();
                    dto.setDateDebut(demande.getDateDebut());
                    dto.setDateFin(demande.getDateFin());
                    dto.setMotif(demande.getMotif());

                    String libelleStatut = Optional.ofNullable(demande.getStatutId())
                            .flatMap(statutDemandeRepository::findById)
                            .map(StatutDemande::getLibelle)
                            .orElse("Inconnu");

                    dto.setStatut(libelleStatut);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // =====================================================
    // 🧑‍💼 RH → DEMANDES EN ATTENTE
    // =====================================================

    public List<DemandeCongeRHDto> getDemandesEnAttentePourRH() {

        StatutDemande statutAttente = statutDemandeRepository
                .findByLibelle("en attente")
                .orElse(null);

        if (statutAttente == null) {
            return List.of();
        }

        return demandeCongeRepository
                .findByStatutId(statutAttente.getIdStatut())
                .stream()
                .map(demande -> new DemandeCongeRHDto(
                        demande.getEmployeId(),
                        demande.getDateDebut().atStartOfDay(),
                        demande.getDateFin().atStartOfDay(),
                        demande.getMotif(),
                        statutAttente.getLibelle()
                ))
                .collect(Collectors.toList());
    }

    // =====================================================
    // 🧑‍💼 RH → ACCEPTER / REFUSER UNE DEMANDE
    // =====================================================

    @Transactional
    public DemandeCongeRHDto changerStatutPourRH(String demandeId, String nouveauStatut) {

        // 1️⃣ Récupérer la demande
        DemandeConge demande = demandeCongeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));

        // 2️⃣ Récupérer le statut
        StatutDemande statut = statutDemandeRepository.findByLibelle(nouveauStatut)
                .orElseThrow(() -> new RuntimeException("Statut introuvable"));

        // 3️⃣ Mise à jour
        demande.setStatutId(statut.getIdStatut());
        demandeCongeRepository.save(demande);

        // 4️⃣ Retour DTO RH (sans ID Mongo)
        return new DemandeCongeRHDto(
                demande.getEmployeId(),
                demande.getDateDebut().atStartOfDay(),
                demande.getDateFin().atStartOfDay(),
                demande.getMotif(),
                statut.getLibelle()
        );
    }
}

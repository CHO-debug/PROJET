package com.example.micro_conge.repositories;

import com.example.micro_conge.entities.DemandeConge;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandeCongeRepository extends MongoRepository<DemandeConge, String> {

    // Récupérer toutes les demandes d'un employé
    List<DemandeConge> findByEmployeId(String employeId);

    // Récupérer toutes les demandes ayant un certain statut
    List<DemandeConge> findByStatutId(String statutId);

    // Récupérer les demandes d'un employé avec un certain statut
    List<DemandeConge> findByEmployeIdAndStatutId(String employeId, String statutId);
}

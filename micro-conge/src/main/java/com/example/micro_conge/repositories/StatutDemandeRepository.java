package com.example.micro_conge.repositories;

import com.example.micro_conge.entities.StatutDemande;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface StatutDemandeRepository extends MongoRepository<StatutDemande, String> {
    Optional<StatutDemande> findByLibelle(String libelle);
}


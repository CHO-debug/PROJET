package com.example.micro_rh.repository;

import com.example.micro_rh.entities.RH;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RHRepository extends MongoRepository<RH, String> {

    // Recherche par email
    Optional<RH> findByEmail(String email);

    // Vérifier si un email existe déjà
    boolean existsByEmail(String email);

    // Vérifier si un département existe déjà (optionnel)
    boolean existsByDepartement(String departement);
}

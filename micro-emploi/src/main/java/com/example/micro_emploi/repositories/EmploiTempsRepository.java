package com.example.micro_emploi.repositories;

import com.example.micro_emploi.entities.EmploiTemps;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmploiTempsRepository extends MongoRepository<EmploiTemps, String> {

    List<EmploiTemps> findByEmployeId(String employeId);
    List<EmploiTemps> findByIdRh(String idRh);
    List<EmploiTemps> findByIdRhAndTache(String idRh, String tache);
}

package com.example.micro_absence.repositories;

import com.example.micro_absence.entities.Absence;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AbsenceRepository extends MongoRepository<Absence, String> {

    // Récupérer toutes les absences d’un employé
    List<Absence> findByEmployeId(String employeId);
    List<Absence> findByIdRh(String idRh);


}

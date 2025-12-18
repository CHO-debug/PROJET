package com.example.micro_absence.repositories;

import com.example.micro_absence.entities.TypeAbsence;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeAbsenceRepository extends MongoRepository<TypeAbsence, String> {
    List<TypeAbsence> findByJustificationRequise(boolean justificationRequise);

}

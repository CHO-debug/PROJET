package com.example.micro_employe.Repositories;
import com.example.micro_employe.entities.Employe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeRepository extends MongoRepository<Employe, String> {

    // Exemple de méthodes de recherche personnalisées
    Optional<Employe> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Employe> findByPoste(String poste);

    List<Employe> findBySoldeCongeGreaterThan(int solde);
}
package com.example.micro_employe.Service;

import com.example.micro_employe.Repositories.EmployeRepository;
import com.example.micro_employe.dto.AbsenceResponseDto;
import com.example.micro_employe.dto.DemandeCongeDTO;
import com.example.micro_employe.dto.EmploiTempsDto;
import com.example.micro_employe.entities.Employe;
import com.example.micro_employe.feign.AbsenceClient;
import com.example.micro_employe.feign.DemandeCongeFeignClient;
import com.example.micro_employe.feign.EmploiTempsFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeService {

    private final EmployeRepository repository;
    private final PasswordEncoder passwordEncoder; // injecté par Spring
    private final EmploiTempsFeignClient emploiTempsFeignClient;
    private final AbsenceClient absenceClient;
    private DemandeCongeFeignClient demandeCongeFeignClient;
    // ----------------------------
    // Enregistrement d'un employé
    // ----------------------------
    public Employe register(Employe employe) {
        if (repository.existsByEmail(employe.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }

        // 🔐 Crypter le mot de passe avant sauvegarde
        employe.setMotDePasse(passwordEncoder.encode(employe.getMotDePasse()));

        return repository.save(employe);
    }

    // ----------------------------
    // Authentification employé
    // ----------------------------
    public Optional<Employe> login(String email, String password) {
        return repository.findByEmail(email)
                .filter(emp -> passwordEncoder.matches(password, emp.getMotDePasse()));
    }

    // ----------------------------
    // Récupérer tous les employés
    // ----------------------------
    public List<Employe> getAll() {
        return repository.findAll();
    }

    // ----------------------------
    // Récupérer employé par ID
    // ----------------------------
    public Optional<Employe> getById(String id) {
        return repository.findById(id);
    }

    // ----------------------------
    // Mettre à jour un employé
    // ----------------------------
    public Employe update(Employe employe) {
        if (!repository.existsById(employe.getId())) {
            throw new RuntimeException("Employé non trouvé");
        }
        return repository.save(employe);
    }

    // ----------------------------
    // Supprimer un employé
    // ----------------------------
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Employé non trouvé");
        }
        repository.deleteById(id);
    }

    // Récupère les absences de l'employé via le microservice Absence
    public List<AbsenceResponseDto> getMyAbsences(String employeId) {
        System.out.println("Appel du microservice Absence pour l'employé : " + employeId);
        return absenceClient.getMyAbsences(employeId);
    }
    public List<EmploiTempsDto> getMyEmploi(String employeId) {
        // Appel via l'instance injectée
        return emploiTempsFeignClient.getMyEmploi(employeId);
    }
    public List<DemandeCongeDTO> getMesDemandesConge(String employeId) {
        return demandeCongeFeignClient.getMyDemandesConge(employeId);
    }

}


package com.example.auth_service.Service;
import com.example.auth_service.DTOS.EmployeDTO;
import com.example.auth_service.DTOS.LoginRequest;
import com.example.auth_service.DTOS.LoginResponse;
import com.example.auth_service.DTOS.RhDTO;
import com.example.auth_service.Util.JwtUtil;
import com.example.auth_service.feignClient.EmployeClient;
import com.example.auth_service.feignClient.RhClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final EmployeClient employeClient;
    private final RhClient rhClient;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil; // Utiliser le bean injecté

    // Récupérer un employé par email via Feign
    public EmployeDTO getEmployeByEmail(String email) {
        try {
            return employeClient.getByEmail(email);
        } catch (Exception e) {
            System.out.println("Erreur getEmployeByEmail: " + e.getMessage());
            return null;
        }
    }

    // Récupérer un RH par email via Feign
    public RhDTO getRhByEmail(String email) {
        try {
            return rhClient.getByEmail(email);
        } catch (Exception e) {
            System.out.println("Erreur getRhByEmail: " + e.getMessage());
            return null;
        }
    }

    // Méthode de Login
    public LoginResponse login(LoginRequest request) {
        System.out.println("Login attempt: " + request.getEmail());

        // Vérifier EMPLOYE
        EmployeDTO employe = getEmployeByEmail(request.getEmail());
        if (employe != null && passwordEncoder.matches(request.getMotDePasse(), employe.getMotDePasse())) {
            String token = jwtUtil.generateToken(employe.getId(), employe.getEmail(), "EMPLOYE");
            return new LoginResponse(employe.getId(), employe.getNom(), employe.getEmail(), "EMPLOYE", token);
        }

        // Vérifier RH
        RhDTO rh = getRhByEmail(request.getEmail());
        if (rh != null && passwordEncoder.matches(request.getMotDePasse(), rh.getPassword())) {
            String token = jwtUtil.generateToken(rh.getId(), rh.getEmail(), "RH");
            return new LoginResponse(rh.getId(), rh.getNom(), rh.getEmail(), "RH", token);
        }

        System.out.println("Échec de l'authentification pour email: " + request.getEmail());
        throw new RuntimeException("Identifiants incorrects");
    }
}

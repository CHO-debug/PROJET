package com.example.micro_rh.Feign;

import com.example.micro_rh.dto.DemandeCongeRHDto; // DTO du microservice conge
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@FeignClient(name = "micro-conge", url = "http://localhost:8088")
public interface DemandeCongeClient {

    @GetMapping("/demande_conge/rh/en-attente")
    List<DemandeCongeRHDto> getDemandesEnAttente();
    @PutMapping("/conges/accepter/{demandeId}")
    DemandeCongeRHDto accepterDemande(@PathVariable String demandeId);

    @PutMapping("/conges/refuser/{demandeId}")
    DemandeCongeRHDto refuserDemande(@PathVariable String demandeId);
}


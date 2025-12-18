package com.example.micro_employe.feign;

import com.example.micro_employe.dto.DemandeCongeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "demandeCongeClient", url = "http://localhost:8088")
public interface DemandeCongeFeignClient {

    @GetMapping("/demande_conge/me")
    List<DemandeCongeDTO> getMyDemandesConge(
            @RequestHeader("X-User-Id") String employeId
    );

}


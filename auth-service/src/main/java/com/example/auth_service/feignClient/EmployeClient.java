package com.example.auth_service.feignClient;

import com.example.auth_service.DTOS.EmployeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="employe-service", url="http://localhost:8087")
public interface EmployeClient {

    @GetMapping("/employes/by-email/{email}")
    EmployeDTO getByEmail(@PathVariable String email);
}
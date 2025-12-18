package com.example.auth_service.feignClient;

import com.example.auth_service.DTOS.RhDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="rh-service", url="http://localhost:8083")
public interface RhClient {

    @GetMapping("/rh/by-email/{email}")
    RhDTO getByEmail(@PathVariable String email);
}


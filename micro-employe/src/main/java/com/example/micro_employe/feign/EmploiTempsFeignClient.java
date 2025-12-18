package com.example.micro_employe.feign;

import com.example.micro_employe.dto.EmploiTempsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "emploiTempsClient", url = "http://localhost:8085")
public interface EmploiTempsFeignClient {

    @GetMapping("/emploitemps/me")
    List<EmploiTempsDto> getMyEmploi(@RequestHeader("X-User-Id") String employeId);

}

package com.example.micro_employe.feign;

import com.example.micro_employe.dto.AbsenceResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "absence-service", url = "http://localhost:8084")
public interface AbsenceClient {

    // ✅ La méthode doit être public
    @GetMapping("/absences/me")
    public List<AbsenceResponseDto> getMyAbsences(@RequestHeader("X-User-Id") String employeId);

}

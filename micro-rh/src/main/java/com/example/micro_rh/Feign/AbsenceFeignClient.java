package com.example.micro_rh.Feign;


import com.example.micro_rh.dto.AbsenceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "absence-service", url = "http://localhost:8084")
public interface AbsenceFeignClient {

    @GetMapping("/absences/created-by-me")
    List<AbsenceDTO> getAbsencesByRh(@RequestHeader("X-User-Id") String rhId);
    @PostMapping("/absences/create-by-rh")
    AbsenceDTO createAbsenceByRh(
            @RequestHeader("X-User-Id") String rhId,
            @RequestBody AbsenceDTO dto
    );
}

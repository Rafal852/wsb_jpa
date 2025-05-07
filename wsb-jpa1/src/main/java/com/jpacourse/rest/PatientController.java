package com.jpacourse.rest;

import com.jpacourse.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/{patientId}/visits")
    public ResponseEntity<Void> addVisitToPatient(@PathVariable Long patientId,
                                                  @RequestParam Long doctorId,
                                                  @RequestParam LocalDateTime visitTime,
                                                  @RequestParam String description) {
        patientService.addVisitToPatient(patientId, doctorId, visitTime, description);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
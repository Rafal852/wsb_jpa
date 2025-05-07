package com.jpacourse.service;

import com.jpacourse.dto.PatientTO;

import java.time.LocalDateTime;

public interface PatientService {
    PatientTO findById(Long id);
    void delete(final Long id);

    void addVisitToPatient(Long patientId, Long doctorId, LocalDateTime visitTime, String description);

}
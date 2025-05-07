package com.jpacourse.service;

import com.jpacourse.dto.PatientTO;
import com.jpacourse.persistance.entity.VisitEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface PatientService {
    PatientTO findById(Long id);
    void delete(final Long id);

    void addVisitToPatient(Long patientId, Long doctorId, LocalDateTime visitTime, String description);

    List<VisitEntity> findVisitsByPatientId(Long patientId);

}
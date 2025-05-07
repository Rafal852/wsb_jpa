package com.jpacourse.persistance.dao;

import com.jpacourse.persistance.entity.PatientEntity;
import java.time.LocalDateTime;
import java.util.List;

public interface PatientDao extends Dao<PatientEntity, Long> {
    void addVisitToPatient(Long patientId, Long doctorId, LocalDateTime visitTime, String description);
    List<PatientEntity> findByLastName(String lastName);
    List<PatientEntity> findPatientsWithMoreThanXVisits(Long visitCount);
    List<PatientEntity> findByInsured(boolean isInsured);
    void addCascadeVisitToPatient(Long patientId, Long doctorId, LocalDateTime visitTime, String description);
}


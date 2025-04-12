package com.jpacourse.persistance.dao.impl;

import com.jpacourse.persistance.dao.PatientDao;
import com.jpacourse.persistance.entity.DoctorEntity;
import com.jpacourse.persistance.entity.PatientEntity;
import com.jpacourse.persistance.entity.VisitEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PatientDaoImpl extends AbstractDao<PatientEntity, Long> implements PatientDao {
    @Override
    @Transactional
    public void addVisitToPatient(Long patientId, Long doctorId, LocalDateTime visitTime, String description) {

        PatientEntity patient = entityManager.find(PatientEntity.class, patientId);
        DoctorEntity doctor = entityManager.find(DoctorEntity.class, doctorId);

        if (patient == null || doctor == null) {
            throw new IllegalArgumentException("Patient lub doctor nie istnieje");
        }

        VisitEntity visit = new VisitEntity();
        visit.setTime(visitTime);
        visit.setDescription(description);
        visit.setPatient(patient);
        visit.setDoctor(doctor);

        patient.getVisits().add(visit);
    }

    @Override
    public List<PatientEntity> findByLastName(String lastName) {
        return List.of();
    }

    @Override
    public List<PatientEntity> findPatientsWithMoreThanXVisits(Long visitCount) {
        return List.of();
    }

    @Override
    public List<PatientEntity> findByInsured(boolean isInsured) {
        return List.of();
    }

    @Override
    public void delete(Long id) {
        PatientEntity entity = findOne(id);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }

}
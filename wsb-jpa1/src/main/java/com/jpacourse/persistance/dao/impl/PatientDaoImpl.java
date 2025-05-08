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
        if (patient == null) {
            throw new IllegalArgumentException("Patient not found with ID: " + patientId);
        }

        DoctorEntity doctor = entityManager.find(DoctorEntity.class, doctorId);
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor not found with ID: " + doctorId);
        }

        VisitEntity visit = new VisitEntity();
        visit.setPatient(patient);
        visit.setDoctor(doctor);
        visit.setTime(visitTime);
        visit.setDescription(description);

        patient.getVisits().add(visit);

        entityManager.merge(patient);
    }

    @Override
    public List<PatientEntity> findByLastName(String lastName) {
        String jpql = "SELECT p FROM PatientEntity p WHERE LOWER(p.lastName) = LOWER(:lastName)";
        return entityManager.createQuery(jpql, PatientEntity.class)
                .setParameter("lastName", lastName)
                .getResultList();
    }

    @Override
    public List<PatientEntity> findPatientsWithMoreThanXVisits(Long visitCount) {
        String jpql = "SELECT p FROM PatientEntity p WHERE SIZE(p.visits) > :visitCount";
        return entityManager.createQuery(jpql, PatientEntity.class)
                .setParameter("visitCount", visitCount.intValue())
                .getResultList();
    }

    @Override
    public List<PatientEntity> findByInsured(boolean isInsured) {
        String jpql = "SELECT p FROM PatientEntity p WHERE p.insured = :isInsured";
        return entityManager.createQuery(jpql, PatientEntity.class)
                .setParameter("isInsured", isInsured)
                .getResultList();
    }

    @Override
    public void delete(Long id) {
        PatientEntity patient = entityManager.find(PatientEntity.class, id);
        if (patient != null) {
            entityManager.remove(patient);
        }
    }

    @Override
    @Transactional
    public void addCascadeVisitToPatient(Long patientId, Long doctorId, LocalDateTime visitTime, String description) {

        PatientEntity patient = entityManager.find(PatientEntity.class, patientId);
        if (patient == null) {
            throw new IllegalArgumentException("Patient not found with ID: " + patientId);
        }

        DoctorEntity doctor = entityManager.find(DoctorEntity.class, doctorId);
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor not found with ID: " + doctorId);
        }

        VisitEntity visit = new VisitEntity();
        visit.setPatient(patient);
        visit.setDoctor(doctor);
        visit.setTime(visitTime);
        visit.setDescription(description);

        patient.getVisits().add(visit);

        entityManager.merge(patient);
    }

    @Override
    public List<PatientEntity> findByHeightGreaterThanAndLessThan(Double minHeight, Double maxHeight) {
        String jpql = "SELECT p FROM PatientEntity p WHERE 1=1";

        if (minHeight != null) {
            jpql += " AND p.height >= :minHeight";
        }
        if (maxHeight != null) {
            jpql += " AND p.height <= :maxHeight";
        }

        var query = entityManager.createQuery(jpql, PatientEntity.class);

        if (minHeight != null) {
            query.setParameter("minHeight", minHeight);
        }
        if (maxHeight != null) {
            query.setParameter("maxHeight", maxHeight);
        }

        return query.getResultList();
    }



}
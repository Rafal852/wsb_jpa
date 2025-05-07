package com.jpacourse.persistance.dao;

import com.jpacourse.persistance.entity.DoctorEntity;
import com.jpacourse.persistance.entity.PatientEntity;
import com.jpacourse.persistance.entity.VisitEntity;
import com.jpacourse.persistance.enums.Specialization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PatientDaoTest {

    @Autowired
    private PatientDao patientDao;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void clearDatabase() {

        entityManager.createQuery("DELETE FROM VisitEntity").executeUpdate();
        entityManager.createQuery("DELETE FROM PatientEntity").executeUpdate();
        entityManager.createQuery("DELETE FROM DoctorEntity").executeUpdate();
    }

    private PatientEntity createTestPatient() {
        PatientEntity patient = new PatientEntity();
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setTelephoneNumber("123-456-7890");
        patient.setPatientNumber(UUID.randomUUID().toString()); // Unikalny patientNumber
        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patient.setInsured(true);
        return patient;
    }

    @Test
    @Transactional
    public void testShouldAddCascadeVisitToPatient() {

        //given
        PatientEntity patient = createTestPatient();
        patient = patientDao.save(patient);

        DoctorEntity doctor = new DoctorEntity();
        doctor.setFirstName("Jane");
        doctor.setLastName("Smith");
        doctor.setTelephoneNumber("123-456-7890");
        doctor.setDoctorNumber("DOC001");
        doctor.setSpecialization(Specialization.OCULIST);
        entityManager.persist(doctor);
        entityManager.flush();

        LocalDateTime visitTime = LocalDateTime.now();
        String description = "Regular check-up";

        // when
        patientDao.addCascadeVisitToPatient(patient.getId(), doctor.getId(), visitTime, description);

        // then
        PatientEntity updatedPatient = patientDao.findOne(patient.getId());
        assertThat(updatedPatient).isNotNull();
        assertThat(updatedPatient.getVisits()).hasSize(1);

        VisitEntity visitEntity = updatedPatient.getVisits().get(0);
        assertThat(visitEntity.getDescription()).isEqualTo(description);
        assertThat(visitEntity.getTime()).isEqualTo(visitTime);
        assertThat(visitEntity.getDoctor().getTelephoneNumber()).isEqualTo("123-456-7890");
    }
}
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
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
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

        //when
        patientDao.addCascadeVisitToPatient(patient.getId(), doctor.getId(), visitTime, description);

        //then
        PatientEntity updatedPatient = patientDao.findOne(patient.getId());
        assertThat(updatedPatient).isNotNull();
        assertThat(updatedPatient.getVisits()).hasSize(1);

        VisitEntity visitEntity = updatedPatient.getVisits().get(0);
        assertThat(visitEntity.getDescription()).isEqualTo(description);
        assertThat(visitEntity.getTime()).isEqualTo(visitTime);
        assertThat(visitEntity.getDoctor().getTelephoneNumber()).isEqualTo("123-456-7890");
    }

    @Test
    public void shouldFindPatientsByLastName() {

        PatientEntity patient1 = new PatientEntity();
        patient1.setFirstName("Anna");
        patient1.setLastName("Nowak");
        patient1.setTelephoneNumber("123456789");
        patient1.setPatientNumber("PAT001");
        patient1.setDateOfBirth(java.time.LocalDate.of(1990, 5, 15));
        patient1.setInsured(true);
        entityManager.persist(patient1);

        PatientEntity patient2 = new PatientEntity();
        patient2.setFirstName("Jan");
        patient2.setLastName("Nowak");
        patient2.setTelephoneNumber("234567890");
        patient2.setPatientNumber("PAT002");
        patient2.setDateOfBirth(java.time.LocalDate.of(1985, 3, 10));
        patient2.setInsured(false);
        entityManager.persist(patient2);

        PatientEntity patient3 = new PatientEntity();
        patient3.setFirstName("Maciej");
        patient3.setLastName("Kowalski");
        patient3.setTelephoneNumber("345678901");
        patient3.setPatientNumber("PAT003");
        patient3.setDateOfBirth(java.time.LocalDate.of(1975, 1, 5));
        patient3.setInsured(true);
        entityManager.persist(patient3);

        entityManager.flush();

        List<PatientEntity> results = patientDao.findByLastName("Nowak");

        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(patient -> patient.getLastName().equals("Nowak")));
    }

    @Test
    public void shouldFindPatientsWithMoreThanXVisits() {

        //Given
        int minimumVisitCount = 2;

        PatientEntity patientWithThreeVisits = createTestPatient();
        entityManager.persist(patientWithThreeVisits);

        DoctorEntity doctor = new DoctorEntity();
        doctor.setFirstName("Jane");
        doctor.setLastName("Smith");
        doctor.setTelephoneNumber("123-456-7890");
        doctor.setDoctorNumber("DOC001");
        doctor.setSpecialization(Specialization.OCULIST);
        entityManager.persist(doctor);

        createAndPersistVisit(patientWithThreeVisits, doctor, "Visit 1");
        createAndPersistVisit(patientWithThreeVisits, doctor, "Visit 2");
        createAndPersistVisit(patientWithThreeVisits, doctor, "Visit 3");

        PatientEntity patientWithOneVisit = createTestPatient();
        patientWithOneVisit.setFirstName("SingleVisit");
        entityManager.persist(patientWithOneVisit);
        createAndPersistVisit(patientWithOneVisit, doctor, "Visit 1");

        entityManager.flush();

        //When
        List<PatientEntity> results = patientDao.findPatientsWithMoreThanXVisits((long) minimumVisitCount);

        // Then
        assertNotNull(results, "Results list should not be null.");
        assertEquals(1, results.size(), "Should find only one patient with more than " + minimumVisitCount + " visits.");
        assertEquals(patientWithThreeVisits.getId(), results.get(0).getId(), "Returned patient ID does not match.");
        assertTrue(results.get(0).getVisits().size() > minimumVisitCount, "Patient should have more visits than " + minimumVisitCount);
    }

    private void createAndPersistVisit(PatientEntity patient, DoctorEntity doctor, String description) {
        VisitEntity visit = new VisitEntity();
        visit.setDescription(description);
        visit.setTime(LocalDateTime.now());
        visit.setDoctor(doctor);
        visit.setPatient(patient);
        entityManager.persist(visit);

        patient.getVisits().add(visit);
    }

    @Test
    public void shouldFindPatientsTallerThanGivenValue() {
        //given
        PatientEntity shortPatient = createTestPatient();
        shortPatient.setHeight(160);
        entityManager.persist(shortPatient);

        PatientEntity mediumPatient = createTestPatient();
        mediumPatient.setHeight(175);
        entityManager.persist(mediumPatient);

        PatientEntity tallPatient = createTestPatient();
        tallPatient.setHeight(190);
        entityManager.persist(tallPatient);

        entityManager.flush();

        //when
        List<PatientEntity> results = patientDao.findByHeightGreaterThanAndLessThan(170.0, null);

        //then
        assertThat(results)
                .hasSize(2)
                .extracting(PatientEntity::getHeight)
                .containsExactlyInAnyOrder(175, 190);
    }

    @Test
    public void shouldFindPatientsShorterThanGivenValue() {
        //given
        PatientEntity shortPatient = createTestPatient();
        shortPatient.setHeight(160);
        entityManager.persist(shortPatient);

        PatientEntity mediumPatient = createTestPatient();
        mediumPatient.setHeight(175);
        entityManager.persist(mediumPatient);

        PatientEntity tallPatient = createTestPatient();
        tallPatient.setHeight(190);
        entityManager.persist(tallPatient);

        entityManager.flush();

        //when
        List<PatientEntity> results = patientDao.findByHeightGreaterThanAndLessThan(null, 180.0);

        //then
        assertThat(results)
                .hasSize(2)
                .extracting(PatientEntity::getHeight)
                .containsExactlyInAnyOrder(160, 175);
    }

    @Test
    public void shouldFetchPatientWithMultipleVisitsAndLogQueries() {
        //given
        PatientEntity patient = createTestPatient();
        patient.setHeight(180);
        entityManager.persist(patient);

        DoctorEntity doctor = new DoctorEntity();
        doctor.setFirstName("Test");
        doctor.setLastName("Doctor");
        doctor.setDoctorNumber("DOC_TEST_" + UUID.randomUUID().toString());
        doctor.setTelephoneNumber("123456789");
        doctor.setSpecialization(Specialization.GP);
        entityManager.persist(doctor);

        for (int i = 1; i <= 3; i++) {
            VisitEntity visit = new VisitEntity();
            visit.setPatient(patient);
            visit.setDoctor(doctor);
            visit.setTime(LocalDateTime.now().plusDays(i));
            visit.setDescription("Wizyta kontrolna " + i);
            entityManager.persist(visit);
        }

        entityManager.flush();
        entityManager.clear();

        System.out.println("\nTest FetchMode.SELECT");

        //when
        PatientEntity foundPatient = patientDao.findOne(patient.getId());

        //then
        assertThat(foundPatient.getVisits()).hasSize(3);
        assertThat(foundPatient.getHeight()).isEqualTo(180);
    }
}

//Wnioski z testu FetchMode.SELECT/JOIN:
//1. FetchMode.SELECT robi osobne zapytania: jedno dla pacjenta a drugie dla wszystkich jego wizyt,
//działa lepiej dla małej liczby wizyt.
//2. FetchMode.JOIN robi jedno połączone zapytanie, ładuje pacjenta i wszystkie wizyty od razu,
//działa lepiej dla większej liczby wizyt, ale może zwracać dużo danych.

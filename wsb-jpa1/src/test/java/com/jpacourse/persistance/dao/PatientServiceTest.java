package com.jpacourse.persistance.dao;

import com.jpacourse.dto.PatientTO;
import com.jpacourse.persistance.entity.DoctorEntity;
import com.jpacourse.persistance.entity.PatientEntity;
import com.jpacourse.persistance.entity.VisitEntity;
import com.jpacourse.service.impl.PatientServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.jpacourse.persistance.enums.Specialization;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
class PatientServiceTest {

    @Mock
    private PatientDao patientDao;

    @InjectMocks
    private PatientServiceImpl patientService;

    @Test
    void testDeletePatient_cascadeDeletesVisits_doesNotDeleteDoctors() {
        //Given
        Long patientId = 1L;
        PatientEntity patient = createMockPatientWithDoctor(patientId);

        when(patientDao.findOne(patientId)).thenReturn(patient);

        //When
        patientService.delete(patientId);

        //Then
        verify(patientDao, times(1)).delete(patient);

        assertNotNull(patient.getVisits(), "Visits list should not be null.");
        assertEquals(2, patient.getVisits().size(), "Patient should have 2 visits initially.");

        VisitEntity visit = patient.getVisits().get(0);
        assertNotNull(visit.getDoctor(), "Visit should have a doctor assigned.");
        assertEquals("John", visit.getDoctor().getFirstName(), "Doctor's first name should not be removed.");
        assertEquals("Smith", visit.getDoctor().getLastName(), "Doctor's last name should not be removed.");
    }


    @Test
    void testFindPatientById_returnsValidTOStructure() {

        //Given
        Long patientId = 1L;
        PatientEntity patient = createMockPatient(patientId);

        when(patientDao.findOne(anyLong())).thenReturn(patient);

        //When
        PatientTO result = patientService.findById(patientId);

        //Then
        assertNotNull(result, "Resulting PatientTO should not be null.");
        assertEquals(patientId, result.getId(), "Incorrect Patient ID.");
        assertEquals("Anna", result.getFirstName(), "First Name mismatch.");
        assertEquals("Nowak", result.getLastName(), "Last Name mismatch.");
        assertTrue(result.isInsured(), "isInsured flag mismatch.");

        assertNotNull(result.getVisits(), "Visits in TO should not be null.");
        assertEquals(2, result.getVisits().size(), "Visit count mismatch.");
        assertEquals("Visit 1", result.getVisits().get(0).getDescription(), "First visit description mismatch.");
        assertEquals("Visit 2", result.getVisits().get(1).getDescription(), "Second visit description mismatch.");
    }

    @Test
    void testFindVisitsByPatientId_returnsValidVisitList() {

        //Given
        Long patientId = 1L;
        PatientEntity patient = createMockPatient(patientId);

        when(patientDao.findOne(patientId)).thenReturn(patient);

        //When
        List<VisitEntity> visits = patientService.findVisitsByPatientId(patientId);

        //Then
        assertNotNull(visits, "Resulting visit list should not be null.");
        assertEquals(2, visits.size(), "Visit list size mismatch.");
        assertEquals("Visit 1", visits.get(0).getDescription(), "First visit description mismatch.");
        assertEquals("Visit 2", visits.get(1).getDescription(), "Second visit description mismatch.");

        verify(patientDao, times(1)).findOne(patientId);
    }

    private PatientEntity createMockPatient(Long patientId) {
        PatientEntity patient = new PatientEntity();
        patient.setId(patientId);
        patient.setFirstName("Anna");
        patient.setLastName("Nowak");
        patient.setPatientNumber("PAT001");
        patient.setDateOfBirth(LocalDate.of(1990, 5, 15));
        patient.setTelephoneNumber("987654321");
        patient.setEmail("anna.nowak@example.com");
        patient.setInsured(true); // Dodane pole
        patient.setVisits(createMockVisits());
        return patient;
    }


    private PatientEntity createMockPatientWithDoctor(Long patientId) {
        PatientEntity patient = createMockPatient(patientId);

        DoctorEntity doctor = new DoctorEntity();
        doctor.setId(501L);
        doctor.setFirstName("John");
        doctor.setLastName("Smith");
        doctor.setDoctorNumber("DOC123");
        doctor.setTelephoneNumber("123456789");
        doctor.setEmail("john.smith@example.com");
        doctor.setSpecialization(Specialization.DERMATOLOGIST);

        for (VisitEntity visit : patient.getVisits()) {
            visit.setDoctor(doctor);
        }

        return patient;
    }

    private List<VisitEntity> createMockVisits() {
        VisitEntity visit1 = new VisitEntity();
        visit1.setId(101L);
        visit1.setTime(LocalDateTime.of(2025, 3, 20, 10, 0));
        visit1.setDescription("Visit 1");

        VisitEntity visit2 = new VisitEntity();
        visit2.setId(102L);
        visit2.setTime(LocalDateTime.of(2025, 3, 21, 14, 0));
        visit2.setDescription("Visit 2");

        List<VisitEntity> visits = new ArrayList<>();
        visits.add(visit1);
        visits.add(visit2);

        return visits;
    }
}
package com.jpacourse.mapper;

import com.jpacourse.dto.PatientTO;
import com.jpacourse.dto.VisitTO;
import com.jpacourse.persistance.entity.PatientEntity;
import com.jpacourse.persistance.entity.VisitEntity;

import java.util.List;
import java.util.stream.Collectors;

public final class PatientMapper {

    public static PatientTO mapToTO(final PatientEntity patientEntity) {
        if (patientEntity == null) {
            return null;
        }

        PatientTO patientTO = new PatientTO();
        patientTO.setId(patientEntity.getId());
        patientTO.setFirstName(patientEntity.getFirstName());
        patientTO.setLastName(patientEntity.getLastName());
        patientTO.setTelephoneNumber(patientEntity.getTelephoneNumber());
        patientTO.setEmail(patientEntity.getEmail());
        patientTO.setPatientNumber(patientEntity.getPatientNumber());
        patientTO.setDateOfBirth(patientEntity.getDateOfBirth());
        patientTO.setInsured(patientEntity.isInsured());

        if (patientEntity.getVisits() != null) {
            List<VisitTO> visitTOs = patientEntity.getVisits().stream()
                    .map(PatientMapper::mapVisitToTO)
                    .collect(Collectors.toList());
            patientTO.setVisits(visitTOs);
        }

        return patientTO;
    }

    private static VisitTO mapVisitToTO(VisitEntity visitEntity) {
        VisitTO visitTO = new VisitTO();
        visitTO.setId(visitEntity.getId());
        visitTO.setTime(visitEntity.getTime());
        visitTO.setDescription(visitEntity.getDescription());

        if (visitEntity.getDoctor() != null) {
            visitTO.setDoctorName(visitEntity.getDoctor().getFirstName() + " " +
                    visitEntity.getDoctor().getLastName());
        }

        if (visitEntity.getMedicalTreatment() != null) {
            visitTO.setTreatmentTypes(List.of(visitEntity.getMedicalTreatment().getType().name()));
        }

        return visitTO;
    }

    public static PatientEntity mapToEntity(final PatientTO patientTO) {
        if (patientTO == null) {
            return null;
        }

        PatientEntity patientEntity = new PatientEntity();
        patientEntity.setId(patientTO.getId());
        patientEntity.setFirstName(patientTO.getFirstName());
        patientEntity.setLastName(patientTO.getLastName());
        patientEntity.setTelephoneNumber(patientTO.getTelephoneNumber());
        patientEntity.setEmail(patientTO.getEmail());
        patientEntity.setPatientNumber(patientTO.getPatientNumber());
        patientEntity.setDateOfBirth(patientTO.getDateOfBirth());
        patientEntity.setInsured(patientTO.isInsured());

        return patientEntity;
    }
}
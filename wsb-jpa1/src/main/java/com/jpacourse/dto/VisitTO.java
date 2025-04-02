package com.jpacourse.dto;

import java.time.LocalDateTime;
import java.util.List;

public class VisitTO {
    private Long id;
    private LocalDateTime time;
    private String description;
    private String doctorName;
    private List<String> treatmentTypes;

    //gettery i settery
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getTime() { return time; }
    public void setTime(LocalDateTime time) { this.time = time; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    public List<String> getTreatmentTypes() { return treatmentTypes; }
    public void setTreatmentTypes(List<String> treatmentTypes) { this.treatmentTypes = treatmentTypes; }
}
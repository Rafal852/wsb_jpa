package com.jpacourse.persistance.entity;

import com.jpacourse.persistance.enums.TreatmentType;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "MEDICAL_TREATMENT")
public class MedicalTreatmentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String description;

	@Enumerated(EnumType.STRING)
	private TreatmentType type;

	// Relacja One-to-Many z VisitEntity
	@OneToMany(mappedBy = "medicalTreatment", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<VisitEntity> visits; // Dwustronna relacja

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TreatmentType getType() {
		return type;
	}

	public void setType(TreatmentType type) {
		this.type = type;
	}

	public List<VisitEntity> getVisits() {
		return visits;
	}

	public void setVisits(List<VisitEntity> visits) {
		this.visits = visits;
	}

}

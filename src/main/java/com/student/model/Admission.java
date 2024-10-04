package com.student.model;

import java.time.LocalDate;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Admission {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "admission_id")
	private Long id;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate admissionDate;
	private String status;
	private String course;

	public Admission(Long id, LocalDate admissionDate, String status, String course) {
		super();
		this.id = id;
		this.admissionDate = admissionDate;
		this.status = status;
		this.course = course;

	}

	public Admission() {
		// Default constructor
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getAdmissionDate() {
		return admissionDate;
	}

	public void setAdmissionDate(LocalDate admissionDate) {
		this.admissionDate = admissionDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	@Override
	public int hashCode() {
		return Objects.hash(admissionDate, course, id, status);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Admission other = (Admission) obj;
		return Objects.equals(admissionDate, other.admissionDate) && Objects.equals(course, other.course)
				&& Objects.equals(id, other.id) && Objects.equals(status, other.status);
	}

}

package com.student.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.student.model.Admission;
import com.student.repositories.AdmissionRepository;

import jakarta.transaction.Transactional;

@Service
public class AdmissionServiceImpl implements AdmissionService {

	private AdmissionRepository admissionRepository;

	public AdmissionServiceImpl(AdmissionRepository admissionRepository) {
		this.admissionRepository = admissionRepository;
	}

	@Transactional
	public List<Admission> readAllExistingAdmissions() {
		return this.admissionRepository.findAll();
	}

	@Transactional
	public Admission findAdmissionById(long id) {
		return admissionRepository.findById(id).orElse(null);
	}

	@Transactional
	public Admission createNewAdmissionDetails(Admission admission) {
		admission.setId(null);
		return admissionRepository.save(admission);
	}

	@Transactional
	public Admission updateAdmissionInformation(long id, Admission replacement) {
		replacement.setId(id);
		return admissionRepository.save(replacement);
	}

	@Transactional
	public void deleteAdmissionById(Long admissionId) {
		// Check if the student details available
		Admission existingAdmission = admissionRepository.findById(admissionId)
				.orElseThrow(() -> new NoSuchElementException("Admission does not exist"));

		admissionRepository.delete(existingAdmission);
	}

}

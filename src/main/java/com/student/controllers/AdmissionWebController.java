package com.student.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.student.model.Admission;
import com.student.services.AdmissionService;

@Controller
public class AdmissionWebController {

	private static final String MESSAGE_ATTRIBUTE = "message";
	private static final String INDEX = "admission_index";
	private static final String EDIT = "edit_admission";

	private final AdmissionService admissionService;

	public AdmissionWebController(AdmissionService admissionService) {
		this.admissionService = admissionService;
	}

	@GetMapping("/admissions")
	public String index(Model model) {
		List<Admission> admissions = admissionService.readAllExistingAdmissions();
		model.addAttribute("admissions", admissions);
		model.addAttribute(MESSAGE_ATTRIBUTE, admissions.isEmpty() ? "No admission is presented" : "");
		return INDEX;
	}

	@GetMapping("/editAdmission/{id}")
	public String editStudent(@PathVariable long id, Model model) {
		Admission admissionById = admissionService.findAdmissionById(id);
		model.addAttribute("admission", admissionById);
		model.addAttribute(MESSAGE_ATTRIBUTE, admissionById == null ? "No admission found with id: " + id : "");
		return EDIT;
	}

	@GetMapping("/newAdmission")
	public String showNewAdmissionRecord(Model model) {
		model.addAttribute(MESSAGE_ATTRIBUTE, "");
		model.addAttribute("admission", new Admission());
		return EDIT;
	}

	@PostMapping("/saveAdmission")
	public String saveAdmission(Admission admission) {
		if (admission.getId() != null) {
			// If the ID is not null, update the existing admission
			admissionService.updateAdmissionInformation(admission.getId(), admission);
		} else {
			// If the ID is null, create a new admission
			admissionService.createNewAdmissionDetails(admission);
		}
		return "redirect:/admissions";
	}

	@GetMapping("/deleteAdmission/{id}")
	public String deleteAdmission(@PathVariable Long id, Model model) {
		admissionService.deleteAdmissionById(id);
		model.addAttribute(MESSAGE_ATTRIBUTE, "Admission with ID " + id + " has been deleted.");
		return "/delete_admission";
	}

}

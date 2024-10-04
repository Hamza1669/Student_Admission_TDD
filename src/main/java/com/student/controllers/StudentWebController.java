package com.student.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.student.model.Admission;
import com.student.model.Student;
import com.student.services.AdmissionService;
import com.student.services.StudentService;

@Controller
public class StudentWebController {

	private static final String MESSAGE_ATTRIBUTE = "message";
	private static final String INDEX = "index";
	private static final String EDIT = "edit";
	private static final String STUDENT = "student";

	private final StudentService studentService;
	private final AdmissionService admissionService;

	public StudentWebController(StudentService studentService, AdmissionService admissionService) {
		this.studentService = studentService;
		this.admissionService = admissionService;
	}

	@GetMapping("/")
	public String index(Model model) {
		List<Student> allStudents = studentService.readAllStudents();
		model.addAttribute("students", allStudents);
		model.addAttribute(MESSAGE_ATTRIBUTE, allStudents.isEmpty() ? "No student detail present" : "");
		return INDEX;

	}

	@GetMapping("/edit/{id}")
	public String showEditStudentForm(@PathVariable Long id, Model model) {
		Student student = studentService.findStudentById(id);
		List<Admission> admissions = admissionService.readAllExistingAdmissions();
		model.addAttribute("admissions", admissions);

		if (student == null) {
			model.addAttribute(MESSAGE_ATTRIBUTE, "No student found with id: " + id);
			model.addAttribute(STUDENT, null);
		} else {
			model.addAttribute(STUDENT, student);
			model.addAttribute(MESSAGE_ATTRIBUTE, "");
		}

		return EDIT;
	}

	@GetMapping("/new")
	public String newStudent(Model model) {
		List<Admission> admissions = admissionService.readAllExistingAdmissions();

		model.addAttribute("admissions", admissions);
		model.addAttribute(MESSAGE_ATTRIBUTE, "");
		Student student = new Student();
		model.addAttribute(STUDENT, student);
		return EDIT;
	}

	@PostMapping("/save")
	public String saveStudent(Student student) {
		final Long id = student.getId();
		if (id == null) {
			studentService.createNewStudentDetails(student);
		} else {
			studentService.updateStudentInformation(id, student);
		}
		return "redirect:/";
	}

	@GetMapping("/delete/{id}")
	public String deleteStudent(@PathVariable Long id, Model model) {
		studentService.deleteStudentById(id);
		model.addAttribute(MESSAGE_ATTRIBUTE, "Student with ID " + id + " has been deleted.");
		return "/delete";
	}

}

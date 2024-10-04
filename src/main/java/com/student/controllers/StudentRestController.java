package com.student.controllers;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.student.model.Student;
import com.student.services.StudentService;

@RestController
@RequestMapping("/api/students/")
public class StudentRestController {

	private final StudentService studentService;

	public StudentRestController(StudentService studentService) {
		this.studentService = studentService;
	}

	@GetMapping("allStudents")
	public List<Student> allStudents() {
		return studentService.readAllStudents();
	}

	@GetMapping("{id}")
	public Student student(@PathVariable long id) {
		return studentService.findStudentById(id);
	}

	@PostMapping("newStudent")
	public ResponseEntity<Student> newStudent(@RequestBody Student student) {
		Student createdStudent = studentService.createNewStudentDetails(student);
		return new ResponseEntity<>(createdStudent, HttpStatus.OK);
	}

	@PutMapping("updateStudent/{id}")
	public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
		Student updatedStudent = studentService.updateStudentInformation(id, student);
		return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
	}

	@DeleteMapping("deleteStudent/{id}")
	public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
		try {
			studentService.deleteStudentById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Success case
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Student not found
		}
	}

}

package com.student.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.student.model.Student;
import com.student.repositories.StudentRepository;

import jakarta.transaction.Transactional;

@Service
public class StudentServiceImpl implements StudentService {

	private StudentRepository studentRepository;

	public StudentServiceImpl(StudentRepository studentRepository) {
		this.studentRepository = studentRepository;
	}

	@Transactional
	public List<Student> readAllStudents() {
		return this.studentRepository.findAll();
	}

	@Transactional
	public Student findStudentById(long id) {
		return studentRepository.findById(id).orElse(null);
	}

	@Transactional
	public Student createNewStudentDetails(Student student) {
		student.setId(null);
		return studentRepository.save(student);
	}

	@Transactional
	public Student updateStudentInformation(long id, Student replacement) {
		replacement.setId(id);
		return studentRepository.save(replacement);
	}

	@Transactional
	public void deleteStudentById(Long studentId) {
		// Check if the student details available
		Student existingStudent = studentRepository.findById(studentId)
				.orElseThrow(() -> new NoSuchElementException("Student does not exist"));

		studentRepository.delete(existingStudent);

	}

}

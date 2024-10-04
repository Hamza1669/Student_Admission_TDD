package com.student.services;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.TransactionSystemException;

import com.student.model.Admission;
import com.student.model.Student;
import com.student.repositories.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

	@Mock
	private StudentRepository studentRepository;

	@InjectMocks
	private StudentServiceImpl studentService;

	@Test
	void test_ReadAllStudents() {
		Admission firstAdmission = new Admission(1L, LocalDate.of(2021, 02, 2), "pending", "bacehlors");
		Admission secondAdmission = new Admission(2L, LocalDate.of(2021, 10, 2), "approved", "masters");
		Student firstStudent = new Student(1L, "Hamza", "Khan", "Hamzakhan@gmail.com", firstAdmission);
		Student secondStudent = new Student(2L, "Hamza", "Khan", "Hamzakhan@gmail.com", secondAdmission);

		when(studentRepository.findAll()).thenReturn(asList(firstStudent, secondStudent));

		assertThat(studentService.readAllStudents()).containsExactly(firstStudent, secondStudent);

	}

	@Test
	void test_getStudentById_found() {
		Admission firstAdmission = new Admission(1L, LocalDate.of(2021, 02, 2), "pending", "bachelors");
		Student firstStudent = new Student(1L, "Hamza", "Khan", "Hamzakhan@gmail.com", firstAdmission);
		when(studentRepository.findById(1L)).thenReturn(Optional.of(firstStudent));
		// Test the method
		assertThat(studentService.findStudentById(1)).isSameAs(firstStudent);
	}

	@Test
	void test_getStudentById_notFound() {
		when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());
		assertThat(studentService.findStudentById(1)).isNull();
	}

	@Test
	void test_createNewStudent_Details() {
		Admission firstAdmission = new Admission(1L, LocalDate.of(2021, 02, 2), "pending", "bachelors");
		Student toSave = new Student(99L, "", "", "", firstAdmission);
		Student saved = new Student(1L, "Hamza", "Khan", "Hamzakhan@gmail.com", firstAdmission);

		when(studentRepository.save(any(Student.class))).thenReturn(saved);

		Student result = studentService.createNewStudentDetails(toSave);

		assertThat(result).isSameAs(saved);

		InOrder inOrder = Mockito.inOrder(studentRepository);
		inOrder.verify(studentRepository).save(toSave);
	}

	@Test
	void test_updateStudent_Information() {
		Admission firstAdmission = new Admission(1L, LocalDate.of(2021, 02, 2), "pending", "bachelors");
		Student replacement = spy(new Student(null, "Hamza", "Khan", "Hamza@gmail.com", firstAdmission));
		Student replaced = new Student(1L, "Hamza", "Khan", "Hamzakhan@gmail.com", firstAdmission);

		when(studentRepository.save(any(Student.class))).thenReturn(replaced);

		Student result = studentService.updateStudentInformation(1L, replacement);

		assertThat(result).isSameAs(replaced);

		InOrder inOrder = Mockito.inOrder(replacement, studentRepository);
		inOrder.verify(replacement).setId(1L);
		inOrder.verify(studentRepository).save(replacement);
	}

	@Test
	void test_deleteStudentDetail_found() {
		Admission firstAdmission = new Admission(1L, LocalDate.of(2021, 02, 2), "pending", "bachelors");
		Student existingStudentDetails = new Student(1L, "Hamza", "Khan", "Hamzakhan@gmail.com", firstAdmission);

		when(studentRepository.findById(1L)).thenReturn(Optional.of(existingStudentDetails));

		studentService.deleteStudentById(1L);

		verify(studentRepository, times(1)).delete(existingStudentDetails);
	}

	@Test
	void test_deleteStudentDetail_notFound() {
		Long studentId = 1L;
		when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

		Throwable exception = assertThrows(NoSuchElementException.class, () -> {
			studentService.deleteStudentById(studentId); // Call real service method
		});
		assertThat(exception.getMessage()).isEqualTo("Student does not exist");
		verify(studentRepository, never()).delete(any(Student.class));
	}

	@Test
	void test_deleteStudentDetail_TransactionalException() {
		Long studentId = 1L;
		when(studentRepository.findById(studentId)).thenThrow(new TransactionSystemException("Transaction failed"));

		Throwable exception = assertThrows(TransactionSystemException.class, () -> {
			studentService.deleteStudentById(studentId);
		});

		assertThat(exception.getMessage()).isEqualTo("Transaction failed");
		verify(studentRepository, times(1)).findById(studentId);
	}

	@Test
	void test_updateStudent_Information_TransactionalException() {
		Admission firstAdmission = new Admission(1L, LocalDate.of(2021, 02, 2), "pending", "bachelors");
		Student replacement = spy(new Student(null, "Hamza", "Khan", "Hamza@gmail.com", firstAdmission));

		when(studentRepository.save(any(Student.class)))
				.thenThrow(new TransactionSystemException("Transaction failed"));

		Throwable exception = assertThrows(TransactionSystemException.class, () -> {
			studentService.updateStudentInformation(1L, replacement);
		});

		assertThat(exception.getMessage()).isEqualTo("Transaction failed");
		InOrder inOrder = Mockito.inOrder(replacement, studentRepository);
		inOrder.verify(replacement).setId(1L);
		inOrder.verify(studentRepository).save(replacement);
	}

	@Test
	void test_createNewStudent_TransactionalException() {
		Admission firstAdmission = new Admission(1L, LocalDate.of(2021, 02, 2), "pending", "bachelors");
		Student toSave = new Student(99L, "", "", "", firstAdmission);

		when(studentRepository.save(any(Student.class)))
				.thenThrow(new TransactionSystemException("Transaction failed"));

		Throwable exception = assertThrows(TransactionSystemException.class, () -> {
			studentService.createNewStudentDetails(toSave);
		});

		assertThat(exception.getMessage()).isEqualTo("Transaction failed");
		verify(studentRepository, times(1)).save(toSave);
	}

	@Test
	void test_getStudentById_TransactionalException() {
		when(studentRepository.findById(anyLong())).thenThrow(new TransactionSystemException("Transaction failed"));

		Throwable exception = assertThrows(TransactionSystemException.class, () -> {
			studentService.findStudentById(1L);
		});

		assertThat(exception.getMessage()).isEqualTo("Transaction failed");
		verify(studentRepository, times(1)).findById(1L);
	}

	@Test
	void test_ReadAllStudents_TransactionalException() {
		when(studentRepository.findAll()).thenThrow(new TransactionSystemException("Transaction failed"));

		TransactionSystemException exception = assertThrows(TransactionSystemException.class,
				studentService::readAllStudents);

		assertThat(exception.getMessage()).isEqualTo("Transaction failed");
		verify(studentRepository, times(1)).findAll();
	}
}

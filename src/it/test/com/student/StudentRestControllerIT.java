package com.student;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.student.model.Admission;
import com.student.model.Student;
import com.student.repositories.StudentRepository;

import io.restassured.RestAssured;
import io.restassured.response.Response;

@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class StudentRestControllerIT {

	@Container
	public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine")
			.withUsername("hamza").withPassword("postgres").withDatabaseName("student_tdd");

	@Autowired
	private StudentRepository studentRepository;

	@LocalServerPort
	private int port;

	@DynamicPropertySource
	static void databaseProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
		registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.PostgreSQLDialect");
		registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
		registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
		registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
	}

	@BeforeEach
	public void setup() {
		RestAssured.port = port;
		studentRepository.deleteAll();
		studentRepository.flush();
	}

	@Test
	void test() {
		assertTrue(postgreSQLContainer.isRunning());
	}

	@Test
	void test_createStudent() {
		Admission admission = new Admission(null, LocalDate.of(2021, 02, 2), "pending", "bachelors");
		Response response = given().contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(new Student(null, "Hamza", "Khan", "Hamzakhan1@gmail.com", admission)).when()
				.post("/api/students/newStudent");

		Student saved = response.getBody().as(Student.class);
		assertThat(studentRepository.findById(saved.getId())).contains(saved);
	}

	@Test
	void test_DeleteStudent() {
		Admission admission = new Admission(null, LocalDate.of(2021, 02, 2), "pending", "bachelors");
		Student savedStudent = new Student(null, "Hamza", "Khan", "Hamzakhan1@gmail.com", admission);
		savedStudent = studentRepository.save(savedStudent);

		given().when().delete("/api/students/deleteStudent/" + savedStudent.getId()).then().statusCode(204);

		assertThat(studentRepository.findById(savedStudent.getId())).isEmpty();
	}

	@Test
	void test_UpdateStudent() throws Exception {

		Admission admission = new Admission(null, LocalDate.of(2021, 02, 2), "pending", "bachelors");
		Student savedStudent = new Student(null, "Hamza", "Khan", "Hamzakhan1@gmail.com", admission);
		savedStudent = studentRepository.save(savedStudent);

		Student updatedStudent = new Student(savedStudent.getId(), "Modified Name", "Khan", "Hamzakhan1@gmail.com",
				admission);

		Student responseBody = given().contentType(MediaType.APPLICATION_JSON_VALUE).body(updatedStudent).when()
				.put("/api/students/updateStudent/" + savedStudent.getId()).then().statusCode(200)

				.extract().as(Student.class);

		assertEquals(savedStudent.getId().intValue(), responseBody.getId().intValue());
		assertEquals("Modified Name", responseBody.getFirstName());
		assertEquals(savedStudent.getLastName(), responseBody.getLastName());
		assertEquals(savedStudent.getEmail(), responseBody.getEmail());
		assertEquals(savedStudent.getAdmission(), responseBody.getAdmission());
	}

}

package com.student;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static io.restassured.RestAssured.given;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
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
import org.springframework.http.MediaType;

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
		// always start with an empty database
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
				.body(new Student(null, "Hamza","Khan", "Hamzakhan1@gmail.com", admission)).when().post("/api/students/newStudent");

		Student saved = response.getBody().as(Student.class);
		assertThat(studentRepository.findById(saved.getId())).contains(saved);
	}



}
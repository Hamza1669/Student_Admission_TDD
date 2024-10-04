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
import com.student.repositories.AdmissionRepository;

import io.restassured.RestAssured;
import io.restassured.response.Response;

@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AdmissionRestControllerIT {

	@Container
	public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine")
			.withUsername("hamza").withPassword("postgres").withDatabaseName("student_tdd");

	@Autowired
	private AdmissionRepository admissionRepository;

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
		admissionRepository.deleteAll();
		admissionRepository.flush();
	}

	@Test
	void test() {
		assertTrue(postgreSQLContainer.isRunning());
	}

	@Test
	void test_NewAdmission() throws Exception {
		Response response = given().contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(new Admission(null, LocalDate.of(2021, 02, 2), "pending", "bachelors")).when()
				.post("/api/admissions/newAdmission");
		Admission saved = response.getBody().as(Admission.class);
		assertThat(admissionRepository.findById(saved.getId())).contains(saved);
	}

	@Test
	void test_UpdateAdmission() throws Exception {

		Admission admission = new Admission(null, LocalDate.of(2021, 02, 2), "pending", "bachelors");

		admission = admissionRepository.save(admission);

		Admission updatedAdmission = new Admission(admission.getId(), LocalDate.of(2021, 02, 2), "modified status",
				"bachelors");

		Admission responseBody = given().contentType(MediaType.APPLICATION_JSON_VALUE).body(updatedAdmission).when()
				.put("/api/admissions/updateAdmission/" + admission.getId()).then().statusCode(200)

				.extract().as(Admission.class);

		assertEquals(admission.getId().intValue(), responseBody.getId().intValue());
		assertEquals(admission.getAdmissionDate(), responseBody.getAdmissionDate());
		assertEquals("modified status", responseBody.getStatus());
		assertEquals(admission.getCourse(), responseBody.getCourse());
	}

	@Test
	void test_DeleteAdmission() {
		Admission admission = new Admission(null, LocalDate.of(2021, 02, 2), "pending", "bachelors");
		admission = admissionRepository.save(admission);

		given().when().delete("/api/admissions/deleteAdmission/" + admission.getId()).then().statusCode(204);

		assertThat(admissionRepository.findById(admission.getId())).isEmpty();
	}

}

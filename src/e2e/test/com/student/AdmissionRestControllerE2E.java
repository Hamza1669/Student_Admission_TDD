package com.student;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class AdmissionRestControllerE2E {

	private static final String BASE_URI = "http://localhost";
	private static final int PORT = 8080;
	private static final String ADMISSION_ENDPOINT = "/api/admissions";

	@BeforeAll
	public static void setup() {
		RestAssured.baseURI = BASE_URI;
		RestAssured.port = PORT;
	}

	@BeforeEach
	void setupTestData() {
		String newAdmissionJson = """
				{
				"id": 2,
				"admissionDate": "2024-01-01",
				"status": "Pending",
				"course": "Bachelors"
				}
				""";

		createAdmission(newAdmissionJson).then().statusCode(200);
	}

	private io.restassured.response.Response createAdmission(String admissionJson) {
		return given().contentType(ContentType.JSON).body(admissionJson).when()
				.post(ADMISSION_ENDPOINT + "/newAdmission");
	}

	@Test
	void test_CreateNewAdmission() {
		String newAdmissionJson = """
				{
				"admissionDate": "2024-02-11",
				"status": "Approved",
				"course": "Masters"
				}
				""";

		createAdmission(newAdmissionJson).then().statusCode(200).contentType(ContentType.JSON)
				.body("id", notNullValue()).body("admissionDate", equalTo("2024-02-11"))
				.body("status", equalTo("Approved")).body("course", equalTo("Masters"));
	}

}
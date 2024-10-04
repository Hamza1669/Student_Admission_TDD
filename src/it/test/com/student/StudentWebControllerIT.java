package com.student;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.student.model.Admission;
import com.student.model.Student;
import com.student.repositories.StudentRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class StudentWebControllerIT {

	@Autowired
	private StudentRepository studentRepository;

	@LocalServerPort
	private int port;

	private WebDriver driver;

	private String baseUrl;

	@BeforeEach
	public void setup() {
		baseUrl = "http://localhost:" + port;
		driver = new HtmlUnitDriver();
		// always start with an empty database
		studentRepository.deleteAll();
		studentRepository.flush();
	}

	@AfterEach
	public void teardown() {
		driver.quit();
	}

	@Test
	void test_HomePage() {
		Admission admission = new Admission(null, LocalDate.of(2021, 02, 2), "pending", "bachelors");
		Student testStudent = studentRepository
				.save(new Student(null, "Hamza", "Khan", "Hamzakhan1@gmail.com", admission));
		driver.get(baseUrl);
		// the table shows the test student
		assertThat(driver.findElement(By.id("student_record")).getText()).contains("Hamza", "Khan",
				"Hamzakhan1@gmail.com", "Edit", "Delete");

		driver.findElement(By.cssSelector("a[href*='/edit/" + testStudent.getId() + "']"));
	}

	@Test
	void test_ShowNewStudentForm() {
		driver.get(baseUrl + "/new");
		driver.findElement(By.name("firstName")).sendKeys("Hamza");
		driver.findElement(By.name("lastName")).sendKeys("Khan");
		driver.findElement(By.name("email")).sendKeys("Hamzakhan1@gmail.com");

		driver.findElement(By.name("btn_submit")).click();

		String currentUrl = driver.getCurrentUrl();
		assertThat(currentUrl).isEqualTo(baseUrl + "/save");

	}

	@Test
	void test_DeleteStudent() throws Exception {
		Admission admission = new Admission(null, LocalDate.of(2021, 02, 2), "pending", "bachelors");
		Student testStudent = studentRepository
				.save(new Student(null, "Hamza", "Khan", "Hamzakhan1@gmail.com", admission));

		driver.get(baseUrl + "/delete/" + testStudent.getId());

		assertFalse(studentRepository.findById(testStudent.getId()).isPresent(), "Student deleted");

	}

}

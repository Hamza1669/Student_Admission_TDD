package com.student;

import static org.assertj.core.api.Assertions.assertThat;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.JavascriptExecutor;


public class AdmissionWebControllerE2E {

	private static int port = Integer.parseInt(System.getProperty("server.port", "8080"));
	private static String baseUrl = "http://localhost:" + port;
	private static WebDriver driver;

	@BeforeAll
	public static void setupClass() {
		// setup Chrome Driver
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void setup() {
		baseUrl = "http://localhost:" + port;
		driver = new ChromeDriver();

	}

	@AfterAll
	public static void teardown() {
		if (driver != null) {
			driver.quit();
		}
	}

	@Test
	void test_HomePage() {
		driver.get(baseUrl);
		driver.findElement(By.cssSelector("a[href*='/admissions"));
	}

	@Test
	void test_CreateAdmission() {
		driver.get(baseUrl + "/newAdmission");

		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

		WebElement dateField = driver.findElement(By.name("admissionDate"));
		jsExecutor.executeScript("arguments[0].value='2024-02-20';", dateField);

		WebElement statusField = driver.findElement(By.name("status"));
		statusField.sendKeys("Approved");

		WebElement courseField = driver.findElement(By.name("course"));
		courseField.sendKeys("Masters");

		driver.findElement(By.name("btn_submit")).click();

		driver.get(baseUrl + "/admissions");

		WebElement admissionRecord = driver.findElement(By.id("admission_record"));
		assertThat(admissionRecord.getText()).contains("2024-02-20", "Approved", "Masters");
	}

}

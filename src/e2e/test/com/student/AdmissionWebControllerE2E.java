package com.student;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

class AdmissionWebControllerE2E {

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
	void test_CreateAdmission() {
		driver.get(baseUrl + "/newAdmission");

		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

		WebElement dateField = driver.findElement(By.name("admissionDate"));
		jsExecutor.executeScript("arguments[0].value='2024-02-20';", dateField);

		driver.findElement(By.name("status")).sendKeys("Approved");
		driver.findElement(By.name("course")).sendKeys("Masters");
		driver.findElement(By.name("btn_submit")).click();

		driver.get(baseUrl + "/admissions");

		WebElement admissionRecord = driver.findElement(By.id("admission_record"));
		assertThat(admissionRecord.getText()).contains("2024-02-20", "Approved", "Masters");
	}

	@Test
	void test_DeleteAdmission() {
		driver.get(baseUrl + "/newAdmission");

		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

		WebElement dateField = driver.findElement(By.name("admissionDate"));
		jsExecutor.executeScript("arguments[0].value='2024-02-22';", dateField);

		driver.findElement(By.name("status")).sendKeys("Approved");
		driver.findElement(By.name("course")).sendKeys("Masters");
		driver.findElement(By.name("btn_submit")).click();

		WebElement admissionRecord = driver.findElement(By.id("admission_record"));
		assertThat(admissionRecord.getText()).contains("2024-02-22", "Approved", "Masters");

		driver.findElement(By.cssSelector("a[href*='/deleteAdmission/']")).click();
	}

	@Test
	void test_EditAdmission() {
		driver.get(baseUrl + "/newAdmission");

		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

		WebElement dateField = driver.findElement(By.name("admissionDate"));
		jsExecutor.executeScript("arguments[0].value='2024-02-23';", dateField);

		driver.findElement(By.name("status")).sendKeys("Pending");
		driver.findElement(By.name("course")).sendKeys("Bachelors");
		driver.findElement(By.name("btn_submit")).click();

		driver.get(baseUrl + "/admissions");

		WebElement editButton = driver.findElement(By.cssSelector("a[href*='/editAdmission/']"));
		String editUrl = editButton.getAttribute("href");
		String admissionId = editUrl.substring(editUrl.lastIndexOf('/') + 1);

		driver.get(baseUrl + "/editAdmission/" + admissionId);

		WebElement editDateField = driver.findElement(By.name("admissionDate"));
		jsExecutor.executeScript("arguments[0].value='2024-03-01';", editDateField);

		driver.findElement(By.name("status")).sendKeys("Approved");
		driver.findElement(By.name("course")).sendKeys("Masters");
		driver.findElement(By.name("btn_submit")).click();

		driver.get(baseUrl + "/admissions");

		WebElement admissionRecord = driver.findElement(By.id("admission_record"));
		assertThat(admissionRecord.getText()).contains("2024-03-01", "Approved", "Masters");

	}

}

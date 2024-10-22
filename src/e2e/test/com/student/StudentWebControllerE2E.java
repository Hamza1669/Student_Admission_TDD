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

class StudentWebControllerE2E {

	private static int port = Integer.parseInt(System.getProperty("server.port", "8080"));
	private static String baseUrl = "http://localhost:" + port;
	private static WebDriver driver;

	@BeforeAll
	public static void setupClass() {
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
	void test_CreateStudent() {

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

		driver.get(baseUrl);
		driver.findElement(By.cssSelector("a[href*='/new']")).click();

		driver.findElement(By.name("firstName")).sendKeys("Hamza");
		driver.findElement(By.name("lastName")).sendKeys("Khan");
		driver.findElement(By.name("email")).sendKeys("Hamzakhan@gmail.com");

		WebElement admissionDropdown = driver.findElement(By.name("admission"));
		WebElement selectedOption = admissionDropdown.findElement(By.xpath(
				"//option[contains(text(),'2024-02-20') and contains(text(),'Approved') and contains(text(),'Masters')]"));
		selectedOption.click();

		driver.findElement(By.name("btn_submit")).click();

		driver.get(baseUrl + "/");

		WebElement studentRecord = driver.findElement(By.id("student_record"));
		assertThat(studentRecord.getText()).contains("Hamza", "Khan", "Hamzakhan@gmail.com", "Masters");
	}

	@Test
	void test_DeleteStudent() {
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

		driver.get(baseUrl);
		driver.findElement(By.cssSelector("a[href*='/new']")).click();

		driver.findElement(By.name("firstName")).sendKeys("Hamza");
		driver.findElement(By.name("lastName")).sendKeys("Khan");
		driver.findElement(By.name("email")).sendKeys("Hamzakhan@gmail.com");

		WebElement admissionDropdown = driver.findElement(By.name("admission"));
		WebElement selectedOption = admissionDropdown.findElement(By.xpath(
				"//option[contains(text(),'2024-02-20') and contains(text(),'Approved') and contains(text(),'Masters')]"));
		selectedOption.click();

		driver.findElement(By.name("btn_submit")).click();

		driver.get(baseUrl + "/");

		WebElement studentRecord = driver.findElement(By.id("student_record"));
		assertThat(studentRecord.getText()).contains("Hamza", "Khan", "Hamzakhan@gmail.com", "Masters");

		driver.findElement(By.cssSelector("a[href*='/delete/']")).click();
	}

}

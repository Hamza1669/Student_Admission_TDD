package com.student.controllers;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.student.model.Admission;
import com.student.model.Student;
import com.student.services.AdmissionService;
import com.student.services.StudentService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = StudentWebController.class)
class StudentWebControllerHtmlUnitTest {

	@Autowired
	private WebClient webClient;

	@MockBean
	private StudentService studentService;

	@MockBean
	private AdmissionService admissionService;

	@Test
	void test_HomePageTitle() throws Exception {
		HtmlPage page = webClient.getPage("/");
		assertThat(page.getTitleText()).isEqualTo("Students");
	}

	@Test
	void test_HomePageWithNoStudents() throws Exception {
		when(studentService.readAllStudents()).thenReturn(emptyList());
		HtmlPage page = this.webClient.getPage("/");
		assertThat(page.getBody().getTextContent()).contains("No student found");
	}

	@Test
	void test_HomePageWithStudents_ShouldShowThemInATable() throws Exception {
		Admission firstAdmission = new Admission(1L, LocalDate.of(2021, 2, 2), "pending", "bachelors");
		Admission secondAdmission = new Admission(1L, LocalDate.of(2021, 4, 2), "approved", "masters");
		when(studentService.readAllStudents())
				.thenReturn(asList(new Student(1L, "Hamza", "Khan", "Hamzakhan@gmail.com", firstAdmission),
						new Student(2L, "Hamza", "Khan", "Hamzakhan@gmail.com", secondAdmission)));

		HtmlPage page = this.webClient.getPage("/");
		assertThat(page.getBody().getTextContent()).doesNotContain("No student found");
		page.getAnchorByHref("/edit/1");
		page.getAnchorByHref("/edit/2");

	}

	@Test
	void test_EditNonExistentStudent() throws Exception {
		when(studentService.findStudentById(1L)).thenReturn(null);

		HtmlPage page = this.webClient.getPage("/edit/1");

		assertThat(page.getBody().getTextContent()).contains("No student found with id: 1");

	}

	@Test
	void test_DeleteStudent_ShouldDisplayConfirmationMessage() throws Exception {
		doNothing().when(studentService).deleteStudentById(1L);

		HtmlPage page = webClient.getPage("/delete/1");

		verify(studentService, times(1)).deleteStudentById(1L);

		String pageContent = page.getBody().getTextContent();
		assertThat(pageContent).contains("Student with ID 1 was deleted.");

		HtmlAnchor newStudentLink = page.getAnchorByHref("/new");
		assertThat(newStudentLink).isNotNull();
	}
}

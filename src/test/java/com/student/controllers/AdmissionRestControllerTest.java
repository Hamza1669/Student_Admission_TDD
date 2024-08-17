package com.student.controllers;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.student.model.Admission;
import com.student.services.AdmissionService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = AdmissionRestController.class)
class AdmissionRestControllerTest {
	
	@Autowired
	private MockMvc mvc;

	@MockBean
	private AdmissionService admissionService;
	
	@Test
	void test_AllAdmissionsEmpty() throws Exception {
		this.mvc.perform(get("/api/admissions/allAdmissions").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json("[]"));
		// the above checks that the content is an empty JSON list
	}

	@Test
	void test_AllAdmissionsNotEmpty() throws Exception {
		when(admissionService.readAllExistingAdmissions())
				.thenReturn(asList(new Admission(1L, LocalDate.of(2021, 02, 2), "pending"),
						new Admission (2L, LocalDate.of(2021, 10, 2), "approved")));
		this.mvc.perform(get("/api/admissions/allAdmissions").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id", is(1))).andExpect(jsonPath("$[0].admissionDate", is("2021-02-02")))
				.andExpect(jsonPath("$[0].status", is("pending")))
				.andExpect(jsonPath("$[1].id", is(2)))
				.andExpect(jsonPath("$[1].admissionDate", is("2021-10-02"))).andExpect(jsonPath("$[1].status", is("approved")));
	}

}
package com.student.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;

import com.student.model.Admission;
import com.student.services.AdmissionService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = AdmissionWebController.class)
class AdmissionWebControllerTest {
	
	@Autowired
	private MockMvc mvc;

	@MockBean
	private AdmissionService admissionService;

	@Test
	void test_Status200() throws Exception {
		mvc.perform(get("/admissions")).andExpect(status().is2xxSuccessful());
	}

	@Test
	void test_ReturnHomeView() throws Exception {
		ModelAndViewAssert.assertViewName(mvc.perform(get("/admissions")).andReturn().getModelAndView(), "admission_index");

	}

	@Test
	void test_HomeView_ShowAdmissions() throws Exception {
		Admission admission = new Admission(1L, LocalDate.of(2021, 2, 2), "pending");
		List<Admission> admissions = Arrays.asList(admission);

		when(admissionService.readAllExistingAdmissions()).thenReturn(admissions);

		mvc.perform(get("/admissions")).andExpect(view().name("admission_index")).andExpect(model().attribute("admissions", admissions))
				.andExpect(model().attribute("message", ""));
	}

}
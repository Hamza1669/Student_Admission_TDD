package com.student.controllers;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
		this.mvc.perform(get("/api/admissions/allAdmissions").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().json("[]"));
	}

	@Test
	void test_AllAdmissionsNotEmpty() throws Exception {
		when(admissionService.readAllExistingAdmissions())
				.thenReturn(asList(new Admission(1L, LocalDate.of(2021, 02, 2), "pending", "bachelors"),
						new Admission(2L, LocalDate.of(2021, 10, 2), "approved", "masters")));
		this.mvc.perform(get("/api/admissions/allAdmissions").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].admissionDate", is("2021-02-02")))
				.andExpect(jsonPath("$[0].status", is("pending"))).andExpect(jsonPath("$[0].course", is("bachelors")))
				.andExpect(jsonPath("$[1].id", is(2))).andExpect(jsonPath("$[1].admissionDate", is("2021-10-02")))
				.andExpect(jsonPath("$[1].status", is("approved"))).andExpect(jsonPath("$[1].course", is("masters")));
	}

	@Test
	void test_OneAdmissionById_WithExistingAdmission() throws Exception {
		when(admissionService.findAdmissionById(anyLong()))
				.thenReturn(new Admission(1L, LocalDate.of(2021, 02, 2), "pending", "bachelors"));
		this.mvc.perform(get("/api/admissions/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.admissionDate", is("2021-02-02")))
				.andExpect(jsonPath("$.status", is("pending"))).andExpect(jsonPath("$.course", is("bachelors")));
	}

	@Test
	void test_OneAdmissionById_WithNotFoundAdmission() throws Exception {
		when(admissionService.findAdmissionById(anyLong())).thenReturn(null);
		this.mvc.perform(get("/api/admissions/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().string(""));
	}

	@Test
	void test_CreateNewAdmission() throws Exception {
		when(admissionService.createNewAdmissionDetails(any(Admission.class)))
				.thenReturn(new Admission(1L, LocalDate.of(2021, 02, 2), "pending", "bachelors"));

		this.mvc.perform(post("/api/admissions/newAdmission").contentType(MediaType.APPLICATION_JSON)
				.content("{\"admissionDate\":\"2021-02-02\",\"status\":\"pending\"}}")).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.admissionDate", is("2021-02-02")))
				.andExpect(jsonPath("$.status", is("pending"))).andExpect(jsonPath("$.course", is("bachelors")));
	}

	@Test
	void test_DeleteAdmission() throws Exception {
		doNothing().when(admissionService).deleteAdmissionById(null);

		this.mvc.perform(delete("/api/admissions/deleteAdmission/1")).andExpect(status().isNoContent());
	}

	@Test
	void test_UpdateAdmission() throws Exception {
		Admission updatedAdmission = new Admission(1L, LocalDate.of(2021, 02, 2), "pending", "bachelors");
		when(admissionService.updateAdmissionInformation(anyLong(), any(Admission.class))).thenReturn(updatedAdmission);

		this.mvc.perform(put("/api/admissions/updateAdmission/1").contentType(MediaType.APPLICATION_JSON)
				.content("{\"admissionDate\":\"2021-02-02\",\"status\":\"pending\"}}")).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.admissionDate", is("2021-02-02")))
				.andExpect(jsonPath("$.status", is("pending"))).andExpect(jsonPath("$.course", is("bachelors")));
	}

}

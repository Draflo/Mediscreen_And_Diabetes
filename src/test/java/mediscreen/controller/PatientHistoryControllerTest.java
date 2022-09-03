package mediscreen.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import mediscreen.exceptions.AlreadyExistsException;
import mediscreen.exceptions.DoesNotExistsException;
import mediscreen.model.Notes;
import mediscreen.model.PatientsHistory;
import mediscreen.service.IdService;
import mediscreen.service.PatientsHistoryService;

@WebMvcTest(controllers = PatientHistoryController.class)
class PatientHistoryControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private PatientsHistoryService patientsHistoryService;
	
	@MockBean
	private IdService idService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private static PatientsHistory testPatientHistory;
	private static Notes testNotes;
	private static List<Notes> testListNotes;
	private static List<PatientsHistory> getAllPatientsHistories = new ArrayList<>();
	
	@BeforeAll
	public static void setUpTestPatientHistory() {

		LocalDate creationDate = LocalDate.of(2022, 8, 15);
		String notes = "Test Notes";
		
		testNotes = new Notes(creationDate, notes);
		
		testListNotes = new ArrayList<>();
		testListNotes.add(testNotes);
		
		testPatientHistory = new PatientsHistory(0L, "Test", "TestHistory", testListNotes);
		getAllPatientsHistories.add(testPatientHistory);
	}

	@Test
	final void testGetPatientsHistoryByFirstAndLastNameShouldReturnStatusOk() throws Exception {
		when(patientsHistoryService.getPatientsHistory("Test", "TestHistory")).thenReturn(testPatientHistory);
		mockMvc.perform(get("/patientHistory").param("firstName", "Test").param("lastName", "TestHistory")).andExpect(status().isOk());
	}

	@Test
	final void testGetPatientsHistoryByIdShouldReturnStatusOk() throws Exception {
		when(patientsHistoryService.getPatientsHistory("Test", "TestHistory")).thenReturn(testPatientHistory);
		mockMvc.perform(get("/patientHistory/{id}", 0L)).andExpect(status().isOk());	
	}

	@Test
	final void testGetAllPatientsHistoryShouldReturnStatusOk() throws Exception {
		when(patientsHistoryService.getAllPatientsHistories()).thenReturn(getAllPatientsHistories);
		mockMvc.perform(get("/allPatientHistory")).andExpect(status().isOk());
	}

	@Test
	final void testAddPatientHistoryShouldReturnStatusCreated() throws Exception {
		when(patientsHistoryService.createPatientsHistory(Mockito.any(PatientsHistory.class))).thenReturn(testPatientHistory);
		mockMvc.perform(post("/patientHistory/add").contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(testPatientHistory))).andExpect(status().isCreated());
	}
	
	@Test
	final void testAddPatientHistoryAlreadyExistShouldReturnError400() throws Exception {
		when(patientsHistoryService.createPatientsHistory(Mockito.any(PatientsHistory.class))).thenThrow(AlreadyExistsException.class);
		mockMvc.perform(post("/patientHistory/add").contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(testPatientHistory))).andExpect(status().isBadRequest());
	}

	@Test
	final void testUpdateOrAddNote() throws Exception {
		when(patientsHistoryService.updateOrCreateNote(Mockito.anyString(), Mockito.anyString(), Mockito.any(Notes.class))).thenReturn(testPatientHistory);
		mockMvc.perform(put("/patientHistory").param("firstName", "Test").param("lastName", "TestHistory").contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(testPatientHistory))).andExpect(status().isCreated());
	}
	
	@Test
	final void testUpdateOrAddNotePatientDoesNotExistShouldReturnError400() throws Exception {
		when(patientsHistoryService.updateOrCreateNote(Mockito.anyString(), Mockito.anyString(), Mockito.any(Notes.class))).thenThrow(DoesNotExistsException.class);
		mockMvc.perform(put("/patientHistory").param("firstName", "Test").param("lastName", "TestHistory").contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(testPatientHistory))).andExpect(status().isBadRequest());
	}

}

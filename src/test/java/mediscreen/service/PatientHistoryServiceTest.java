package mediscreen.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import mediscreen.exceptions.AlreadyExistsException;
import mediscreen.exceptions.DoesNotExistsException;
import mediscreen.model.Notes;
import mediscreen.model.PatientsHistory;
import mediscreen.repository.PatientsHistoryRepository;

@WebMvcTest(PatientsHistoryService.class)
class PatientHistoryServiceTest {
	
	private static PatientsHistory testPatientsHistory;
	private static Notes testNotes;
	private static List<Notes> testListNotes;
	private static Validator validator;
	
	@MockBean
	private PatientsHistoryRepository patientsHistoryRepository;
	
	@Autowired PatientsHistoryService patientsHistoryService;
	
	@BeforeAll
	public static void setUpTestPatientHistory() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
		
		LocalDate creationDate = LocalDate.of(2022, 8, 15);
		String notes = "Test Notes";
		
		testNotes = new Notes(creationDate, notes);
		
		testListNotes = new ArrayList<>();
		testListNotes.add(testNotes);
		
		testPatientsHistory = new PatientsHistory(0L, "Test", "TestHistory", testListNotes);
	}

	@Test
	final void testCreatePatientsHistoryShouldReturnNewPatientHistory() throws AlreadyExistsException {
		PatientsHistory patientsHistory = new PatientsHistory();
		
		patientsHistory.setFirstName("Jean");
		patientsHistory.setLastName("Michel");
		patientsHistory.setNotes(testListNotes);
		patientsHistory.setPatientId(0L);
		
		when(patientsHistoryRepository.findByFirstNameAndLastName(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.empty());
		when(patientsHistoryRepository.insert(patientsHistory)).thenReturn(patientsHistory);
		patientsHistoryService.createPatientsHistory(patientsHistory);
		verify(patientsHistoryRepository).insert(patientsHistory);
	}
	
	@Test
	final void createDuplicatedPatientTest_shouldThrowException() throws AlreadyExistsException {
		when(patientsHistoryRepository.findByFirstNameAndLastName("Test", "TestHistory")).thenReturn(Optional.of(testPatientsHistory));
		assertThrows(AlreadyExistsException.class, () -> patientsHistoryService.createPatientsHistory(testPatientsHistory));

	}

	@Test
	final void testGetPatientsHistory() throws DoesNotExistsException {
		when(patientsHistoryRepository.findByFirstNameAndLastName("Test", "TestHistory")).thenReturn(Optional.of(testPatientsHistory));
		PatientsHistory foundPatientsHistory = patientsHistoryService.getPatientsHistory("Test", "TestHistory");
		assertThat(foundPatientsHistory).isEqualTo(testPatientsHistory);
	}

	@Test
	final void testGetPatientsHistoryById() throws Exception {
		when(patientsHistoryRepository.findByPatientId(0L)).thenReturn(Optional.of(testPatientsHistory));
		PatientsHistory foundPatientsHistory = patientsHistoryService.getPatientsHistoryById(0L);
		assertThat(foundPatientsHistory).isEqualTo(testPatientsHistory);
	}
	
	@Test
	final void getPatientHistoryUnknown_shouldThrowException() throws DoesNotExistsException {
		assertThrows(DoesNotExistsException.class, () -> patientsHistoryService.getPatientsHistory("Test", "Test"));
	}
	
	@Test
	final void getPatientHistoryUnknownId_shouldThrowException() throws DoesNotExistsException {
		assertThrows(DoesNotExistsException.class, () -> patientsHistoryService.getPatientsHistoryById(5L));
	}

	@Test
	final void testGetAllPatientsHistories() {
		List<PatientsHistory> findAll = new ArrayList<>();
		findAll.add(testPatientsHistory);
		when(patientsHistoryRepository.findAll()).thenReturn(findAll);
		List<PatientsHistory> foundAll = patientsHistoryService.getAllPatientsHistories();
		assertThat(foundAll).isEqualTo(findAll);
	}

	@Test
	final void testGetNotesByCreationDate() throws Exception {
		LocalDate creationDate = LocalDate.of(2022, 8, 15);
		when(patientsHistoryRepository.findByFirstNameAndLastName("Test", "TestHistory")).thenReturn(Optional.of(testPatientsHistory));
		Notes foundNotes = patientsHistoryService.getNotesByCreationDate("Test", "TestHistory", creationDate);
		assertThat(foundNotes).isEqualTo(testNotes);
	}
	
	@Test
	final void testGetNotesByCreationDateDoesNotExist() throws Exception {
		when(patientsHistoryRepository.findByFirstNameAndLastName("Test", "TestHistory")).thenReturn(Optional.of(testPatientsHistory));
		Notes foundNotes = patientsHistoryService.getNotesByCreationDate("Test", "TestHistory", LocalDate.now());
		assertThat(foundNotes).isNull();
	}
	
	@Test
	final void testCreateNote() throws Exception {
		LocalDate creationDate = LocalDate.of(2022, 8, 25);
		String addedText = "Test Added Notes";
		Notes addedNotes = new Notes(creationDate, addedText);
		when(patientsHistoryRepository.findByFirstNameAndLastName("Test", "TestHistory")).thenReturn(Optional.of(testPatientsHistory));
		when(patientsHistoryRepository.save(testPatientsHistory)).thenReturn(testPatientsHistory);
		patientsHistoryService.updateOrCreateNote("Test", "TestHistory", addedNotes);
		assertThat(testPatientsHistory.getNotes().get(1).getNote()).isEqualTo(addedText);
		assertThat(testPatientsHistory.getNotes().get(0).getNote()).isEqualTo("Test Updated Notes");

	}

	@Test
	final void testUpdateNote() throws Exception {
		LocalDate creationDate = LocalDate.of(2022, 8, 15);
		String updatedText = "Test Updated Notes";
		Notes updatedNotes = new Notes(creationDate, updatedText);
		when(patientsHistoryRepository.findByFirstNameAndLastName("Test", "TestHistory")).thenReturn(Optional.of(testPatientsHistory));
		when(patientsHistoryRepository.save(testPatientsHistory)).thenReturn(testPatientsHistory);
		patientsHistoryService.updateOrCreateNote("Test", "TestHistory", updatedNotes);
		assertThat(testPatientsHistory.getNotes().get(0).getNote()).isEqualTo(updatedText);
		
	}
	
	@Test
	final void createPatientHistoryWithFieldsErrorTest_shouldThrowException() {
		
		Notes noDate = new Notes(null, "Test");
		Notes noText = new Notes(LocalDate.of(2022, 5, 5), null);
		
		List<Notes> noDateList = new ArrayList<>();
		List<Notes> noTextList = new ArrayList<>();
		
		noDateList.add(noDate);
		noTextList.add(noText);
		
		PatientsHistory patientsHistory1 = new PatientsHistory(0L, "", "Michel", null);
		Set<ConstraintViolation<PatientsHistory>> violations1 = validator.validate(patientsHistory1);
		assertFalse(violations1.isEmpty());
		
		PatientsHistory patientsHistory2 = new PatientsHistory(0L, "Jean", "", null);
		Set<ConstraintViolation<PatientsHistory>> violations2 = validator.validate(patientsHistory2);
		assertFalse(violations2.isEmpty());
				
		PatientsHistory patientsHistory3 = new PatientsHistory(0L, null, "Michel", null);
		Set<ConstraintViolation<PatientsHistory>> violations3 = validator.validate(patientsHistory3);
		assertFalse(violations3.isEmpty());
		
		PatientsHistory patientsHistory4 = new PatientsHistory(0L, "Jean", null, null );
		Set<ConstraintViolation<PatientsHistory>> violations4 = validator.validate(patientsHistory4);
		assertFalse(violations4.isEmpty());
		
		PatientsHistory patientsHistory5 = new PatientsHistory(0L, "", "Michel", noDateList);
		Set<ConstraintViolation<PatientsHistory>> violations5 = validator.validate(patientsHistory5);
		assertFalse(violations5.isEmpty());
		
		PatientsHistory patientsHistory6 = new PatientsHistory(0L, "", "Michel", noTextList);
		Set<ConstraintViolation<PatientsHistory>> violations6 = validator.validate(patientsHistory6);
		assertFalse(violations6.isEmpty());
		
	}

}

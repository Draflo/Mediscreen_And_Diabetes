package mediscreen.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mediscreen.model.Notes;
import mediscreen.model.PatientsHistory;
import mediscreen.service.IdService;
import mediscreen.service.PatientsHistoryService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class PatientHistoryController {
	
	@Autowired
	private PatientsHistoryService patientsHistoryService;
	
	@Autowired
	private IdService idService;
	
	public PatientHistoryController(PatientsHistoryService patientsHistoryService) {
		this.patientsHistoryService = patientsHistoryService;
	}
	
	@GetMapping("/patientHistory")
	public PatientsHistory getPatientsHistoryByFirstAndLastName(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName ) throws Exception {
		PatientsHistory patientsHistory = patientsHistoryService.getPatientsHistory(firstName, lastName);
		return patientsHistory;
	}
	
	@GetMapping("/patientHistory/{id}")
	public PatientsHistory getPatientsHistoryById(@PathVariable Long id) throws Exception {
		PatientsHistory patientsHistory = patientsHistoryService.getPatientsHistoryById(id);
		return patientsHistory;
	}
	
	@GetMapping("/allPatientHistory")
	public List<PatientsHistory> getAllPatientsHistory() {
		List<PatientsHistory> getAll = patientsHistoryService.getAllPatientsHistories();
		return getAll;
	}
	
	@PostMapping("/patientHistory/add")
	public ResponseEntity<Object> addPatientHistory(@RequestBody PatientsHistory patientsHistory) {
		try {
			patientsHistory.setPatientId(idService.generateId(PatientsHistory.IdAuto));
			PatientsHistory createdPatient = patientsHistoryService.createPatientsHistory(patientsHistory);
			return ResponseEntity.status(HttpStatus.CREATED).body(createdPatient);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@PutMapping("/patientHistory")
	public ResponseEntity<Object> updateOrAddNote(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName, @RequestBody Notes notes) throws Exception {
		try {
			PatientsHistory createdNotes = patientsHistoryService.updateOrCreateNote(firstName, lastName, notes);
			return ResponseEntity.status(HttpStatus.CREATED).body(createdNotes);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

}

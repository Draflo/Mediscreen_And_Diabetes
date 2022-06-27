package mediscreen.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mediscreen.model.Notes;
import mediscreen.model.PatientsHistory;
import mediscreen.repository.PatientsHistoryRepository;

@Service
public class PatientsHistoryService {
	
	@Autowired
	PatientsHistoryRepository patientsHistoryRepository;
	
	public PatientsHistory createPatientsHistory(PatientsHistory patientsHistory) throws Exception {
		if (patientsHistoryRepository.findByFirstNameAndLastName(patientsHistory.getFirstName(), patientsHistory.getLastName()).isPresent()) {
			throw new Exception("Already Exists");
		}
		return patientsHistoryRepository.insert(patientsHistory);
	}
	
	public PatientsHistory getPatientsHistory(String firstName, String lastName) throws Exception {
		Optional<PatientsHistory> patientHistory = patientsHistoryRepository.findByFirstNameAndLastName(firstName, lastName);
		if (! patientHistory.isPresent()) {
			throw new Exception();
		}
		return patientHistory.get();
	}
	
	public PatientsHistory addNotes(String firstName, String lastName, Notes note) throws Exception {
		Optional<PatientsHistory> patientsHistory = patientsHistoryRepository.findByFirstNameAndLastName(firstName, lastName);
		patientsHistory.get().getNotes().add(note);
		return patientsHistoryRepository.save(patientsHistory.get());
	}

}

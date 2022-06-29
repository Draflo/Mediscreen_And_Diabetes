package mediscreen.service;

import java.time.LocalDate;
import java.util.List;
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
	
	public List<PatientsHistory> getAllPatientsHistories() {
		List<PatientsHistory> getAll = patientsHistoryRepository.findAll();
		return getAll;
	}
	
	public Notes getNotesByCreationDate(String firstName, String lastName, LocalDate creationDate) throws Exception {
		Optional<PatientsHistory> patientsHistory = patientsHistoryRepository.findByFirstNameAndLastName(firstName, lastName);
		for (Notes notes : patientsHistory.get().getNotes()) {
			if (notes.getCreationDate().equals(creationDate)) {
				return notes;
			}
		}
		return null;
	}
	
	public PatientsHistory updateOrCreateNote(String firstName, String lastName, Notes noteUpdated) throws Exception {
		Optional<PatientsHistory> patientsHistory = patientsHistoryRepository.findByFirstNameAndLastName(firstName, lastName);
		for (Notes notes : patientsHistory.get().getNotes()) {
			if (notes.getCreationDate().equals(noteUpdated.getCreationDate())) {
				notes.setNote(noteUpdated.getNote());
				return patientsHistoryRepository.save(patientsHistory.get());
			}
		}
		patientsHistory.get().getNotes().add(noteUpdated);
		return patientsHistoryRepository.save(patientsHistory.get());
	}

}

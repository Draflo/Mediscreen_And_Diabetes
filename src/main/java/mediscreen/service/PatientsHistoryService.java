package mediscreen.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mediscreen.exceptions.AlreadyExistsException;
import mediscreen.exceptions.DoesNotExistsException;
import mediscreen.model.Notes;
import mediscreen.model.PatientsHistory;
import mediscreen.repository.PatientsHistoryRepository;

@Service
public class PatientsHistoryService {
	
	@Autowired
	PatientsHistoryRepository patientsHistoryRepository;
	
	public PatientsHistory createPatientsHistory(PatientsHistory patientsHistory) throws AlreadyExistsException {
		if (patientsHistoryRepository.findByFirstNameAndLastName(patientsHistory.getFirstName(), patientsHistory.getLastName()).isPresent()) {
			throw new AlreadyExistsException(patientsHistory.getFirstName() + " " + patientsHistory.getLastName() + "already Exists");
		}
		return patientsHistoryRepository.insert(patientsHistory);
	}
	
	public PatientsHistory getPatientsHistory(String firstName, String lastName) throws DoesNotExistsException {
		Optional<PatientsHistory> patientHistory = patientsHistoryRepository.findByFirstNameAndLastName(firstName, lastName);
		if (! patientHistory.isPresent()) {
			throw new DoesNotExistsException(firstName + " " + lastName + " does not exists");
		}
		return patientHistory.get();
	}
	
	public PatientsHistory getPatientsHistoryById(Long id) throws DoesNotExistsException {
		Optional<PatientsHistory> patientHistory = patientsHistoryRepository.findByPatientId(id);
		if (! patientHistory.isPresent()) {
			throw new DoesNotExistsException("Patient with id of " + id + "does not exists");
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

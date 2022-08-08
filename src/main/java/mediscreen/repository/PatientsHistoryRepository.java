package mediscreen.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import mediscreen.model.PatientsHistory;

@Repository
public interface PatientsHistoryRepository extends MongoRepository<PatientsHistory, String>{

	Optional<PatientsHistory> findByFirstNameAndLastName(String firstName, String lastName);
	Optional<PatientsHistory> findByPatientId(Long patientId);
}

package mediscreen;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import mediscreen.model.PatientsHistory;
import mediscreen.repository.PatientsHistoryRepository;

@SpringBootApplication
public class MediscreenHistoryAndDiabetesApplication implements CommandLineRunner {
	
	private final Logger logger = LoggerFactory.getLogger(MediscreenHistoryAndDiabetesApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MediscreenHistoryAndDiabetesApplication.class, args);
	}
	
	@Autowired
	PatientsHistoryRepository patientsNoteRepository;

	@Override
	public void run(String... args) throws Exception {
		logger.info("Start !");
		Optional<PatientsHistory> p = patientsNoteRepository.findById("1");
		if (p.isPresent()) {
			logger.info(p.get().getNotes().toString());
		} else {
			logger.info("Patient not found");
		}
	}

}

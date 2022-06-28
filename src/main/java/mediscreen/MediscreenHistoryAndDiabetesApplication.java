package mediscreen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MediscreenHistoryAndDiabetesApplication implements CommandLineRunner {
	
	private final Logger logger = LoggerFactory.getLogger(MediscreenHistoryAndDiabetesApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MediscreenHistoryAndDiabetesApplication.class, args);
	}
	
	

	@Override
	public void run(String... args) throws Exception {
		
	}

}

package mediscreen;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;


@DataMongoTest(properties = {"spring.mongodb.embedded.version=4.0.2"})
class MediscreenHistoryAndDiabetesApplicationTests {

	@Test
	void contextLoads() {
	}

}

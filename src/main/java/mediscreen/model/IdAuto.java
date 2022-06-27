package mediscreen.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "Id")
@Getter
@Setter
public class IdAuto {
	
	@Id
	private String id;
	
	private long seq;
	
	

}

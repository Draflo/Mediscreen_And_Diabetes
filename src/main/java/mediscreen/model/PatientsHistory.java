package mediscreen.model;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "patientHistory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientsHistory {
	
	@Transient
	public static final String IdAuto = "PatientsId";
	
	@Id
	private Long patientId;
	
	@NotBlank
	private String firstName;
	
	@NotBlank
	private String lastName;
	
	private List<Notes> notes;
	

}

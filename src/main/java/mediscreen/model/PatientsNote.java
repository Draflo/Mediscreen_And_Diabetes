package mediscreen.model;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientsNote {
	
	@Id
	private String patientId;
	
	@NotBlank
	private String firstName;
	
	@NotBlank
	private String lastName;
	
	private LocalDateTime creation;
	
	@NotBlank
	private String note;
	

}

package mediscreen.model;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Notes {
	
	private LocalDate creation;
	
	@NotBlank
	private String note;

}

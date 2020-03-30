package lfs.server.mortuary;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.Nullable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonProperty;

import lfs.server.branch.District;
import lfs.server.persistence.IdGenerator;
import lfs.server.transport.Transport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
@GenericGenerator(
        name = "corpse_id",          
        strategy = "lfs.server.persistence.IdGenerator",
        parameters = {
	            @Parameter(name = IdGenerator.ID_TYPE_PARAM, value = IdGenerator.ID_TYPE_STRING)
})
public class Corpse {
	public enum Gender {MALE, FEMALE}
	
	@Id
	@GeneratedValue(generator = "corpse_id")
	@Column(columnDefinition = "CHAR(10)")
	private String tagNo;
	
	@Column(length = 60)
	@Size(min = 2, message = "Corpse's name is too short")
	@Nullable
	private String names;
	
	@Column(length = 50)
	@Size(min = 2, message = "Corpse's surname is too short")
	@Nullable
	private String surname;
	
	@Enumerated(EnumType.STRING)
	@Column(name="gender", columnDefinition="ENUM('MALE','FEMALE')")
	private Gender gender;
	
	@PastOrPresent
	private LocalDate dateOfBirth;
	
	@Column(length = 150)
	private String phycialAddress;
	
	@Column(columnDefinition = "TINYINT UNSIGNED")
	private District district;
	
	@Column(length = 50)
	private String chief;
	
	@OneToMany(mappedBy="corpse", 
			cascade=CascadeType.ALL, 
			orphanRemoval=true)
	private List<NextOfKin> nextOfKins;
	
	@PastOrPresent
	private LocalDate dateOfDeath;
	
	@PastOrPresent
	private LocalDateTime arrivalDate;
	
	@Column(length = 100)
	private String causeOfDeath;
	
	@Column(length = 100)
	private String placeOfDeath;
	
	@Column(length = 25)
	private String fridgeNumber;
	
	@Column(length = 25)
	private String shelfNumber;
	
	@Column(length = 50)
	private String receivedBy;
	
	@ManyToOne(cascade=CascadeType.ALL,
			fetch = FetchType.LAZY)
	@JoinColumn(name="transport_id")
	private Transport transport;
	
	@Column(length = 80)
	private String specialRequirements;
	
	private String otherNotes;
	
	private boolean released;
	
	@OneToMany(mappedBy="corpse", 
			cascade=CascadeType.ALL,
			fetch = FetchType.LAZY,
			orphanRemoval=true)
	private List<ProductSales> productSales;
	
	@ManyToOne(fetch = FetchType.LAZY,
			cascade = CascadeType.PERSIST)
	@JoinColumn(name="from_other_mortuary_id")
	@JsonProperty("transferredFrom")
	private OtherMortuary transferredFrom;
}

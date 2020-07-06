package com.breakoutms.lfs.server.mortuary.corpse.model;

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
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import com.breakoutms.lfs.server.audit.AuditableEntity;
import com.breakoutms.lfs.server.core.enums.District;
import com.breakoutms.lfs.server.core.enums.Gender;
import com.breakoutms.lfs.server.persistence.IdGenerator;
import com.breakoutms.lfs.server.transport.Transport;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Audited
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor @NoArgsConstructor
@GenericGenerator(
        name = "corpse_id",          
        strategy = "com.breakoutms.lfs.server.persistence.IdGenerator",
        parameters = {
	            @Parameter(name = IdGenerator.ID_TYPE_PARAM, value = IdGenerator.ID_TYPE_STRING)
})
@Table(indexes = {
        @Index(columnList = "names", name = "index_corpse_names"),
        @Index(columnList = "surname", name = "index_corpse_surname")
})
@SQLDelete(sql = "UPDATE corpse SET deleted=true WHERE id=?")
@Where(clause = AuditableEntity.CLAUSE)
public class Corpse extends AuditableEntity<String> {
	
	@Id
	@GeneratedValue(generator = "corpse_id")
	@Column(columnDefinition = "CHAR(10)")
	private String tagNo;
	
	@Column(length = 60)
	@Size(min = 2, max = 60)
	@Nullable
	private String names;
	
	@Column(length = 50)
	@Size(min = 2, max = 50)
	@Nullable
	private String surname;
	
	@Enumerated(EnumType.STRING)
	@Column(columnDefinition="ENUM('MALE','FEMALE')")
	private Gender gender;
	
	@PastOrPresent
	private LocalDate dateOfBirth;
	
	@Column(length = 150)
	private String phycialAddress;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 50)
	private District district;
	
	@Column(length = 50)
	private String chief;
	
	@OneToMany(mappedBy="corpse", 
			cascade=CascadeType.ALL,
			fetch = FetchType.LAZY,
			orphanRemoval=true)
	private List<NextOfKin> nextOfKins;
	
	private LocalDate dateOfDeath;

	@NotNull
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
	private Transport transport;
	
	@Column(length = 80)
	private String specialRequirements;
	
	private String otherNotes;
	
	private boolean released;
	
	@ManyToOne(fetch = FetchType.LAZY,
			cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name="from_other_mortuary_id")
	@JsonProperty("transferredFrom")
	private OtherMortuary transferredFrom;

	@Override
	public String getId() {
		return tagNo;
	}
}

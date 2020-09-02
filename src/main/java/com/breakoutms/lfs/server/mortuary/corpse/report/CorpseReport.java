package com.breakoutms.lfs.server.mortuary.corpse.report;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.breakoutms.lfs.server.reports.utils.Format;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CorpseReport {
	
	private String tagNo;
	@JsonIgnore private String names;
	@JsonIgnore private String surname;
	@JsonIgnore private LocalDateTime arrivalDate;
	@JsonIgnore private LocalDate dateOfDeath;
	private String causeOfDeath;
	
	public String getFullName() {
	    return Stream.of(names, surname)
	    		.filter(it -> it != null && !it.isBlank())
	    		.collect(Collectors.joining(" "));
	}
	
	@JsonProperty("arrivalDate")
	public String getFormartedArrivalDate() {
		return Format.date(arrivalDate);
	}
	
	@JsonProperty("dateOfDeath")
	public String getFormartedDateOfDeath() {
		return Format.date(dateOfDeath);
	}
}

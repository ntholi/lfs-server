package com.breakoutms.lfs.server.mortuary.corpse.report;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class CorpseReport {

	private String tagNo;
	@JsonIgnore
	private String names;
	@JsonIgnore
	private String surname;
	private LocalDateTime arrivalDate;
	private LocalDate dateOfDeath;
	private String causeOfDeath;
	
	public String getFullName() {
	    return Stream.of(names, surname)
	    		.filter(it -> it != null && !it.isBlank())
	    		.collect(Collectors.joining(" "));
	}
}

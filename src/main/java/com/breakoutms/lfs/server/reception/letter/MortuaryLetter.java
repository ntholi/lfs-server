package com.breakoutms.lfs.server.reception.letter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MortuaryLetter {

	private static final String FORMAT = "dd MMMM yyyy";
	
	private String corpseNames;
	@JsonIgnore
	private LocalDateTime _arrivalDate;
	@JsonIgnore
	private LocalDateTime _releaseDate;
	
	public String getCorpseNames() {
		return corpseNames;
	}
	public String getArrivalDate() {
		return _arrivalDate == null? null: _arrivalDate.format(DateTimeFormatter.ofPattern(FORMAT));
	}
	public String getReleaseDate() {
		return _releaseDate == null? null: _releaseDate.format(DateTimeFormatter.ofPattern(FORMAT));
	}
}

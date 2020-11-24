package com.breakoutms.lfs.server.mortuary.corpse.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.breakoutms.lfs.common.enums.Gender;

public interface CorpseProjection {

	public String getTagNo();

	public String getNames();

	public String getSurname();

	public Gender getGender();

	public LocalDate getDateOfBirth();

	public String getPhycialAddress();

	public LocalDate getDateOfDeath();

	public LocalDateTime getArrivalDate();

	public String getCauseOfDeath();

	public String getFridgeNumber();

	public String getShelfNumber();

	public String getReceivedBy();
}

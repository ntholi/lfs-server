package com.breakoutms.lfs.server.core.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum District {
	BEREA("Berea"),
	BUTHA_BUTHE("Butha-Buthe"),
	LERIBE("Leribe"),
	MAFETENG("Mafeteng"),
	MASERU("Maseru"),
	MOHALES_HOEK("Mohale's Hoek"),
	MOKHOTLONG("Mokhotlong"),
	QHACHA("Qhacha"),
	QUTHING("Quthing"),
	THABA_TSEKA("Thaba-Tseka"),
	OTHER("Other");

	private String value;

	private District(final String name){
		this.value = name;
	}

	@Override
	public String toString() {
		return value;
	}

	@JsonCreator
	public static District fromString(final String text) {
		for (final District en : District.values()) {
			if (en.name().equals(text) || en.value.equalsIgnoreCase(text)) {
				return en;
			}
		}
		throw new IllegalArgumentException("No constant with text " + text + " found");
	}
}
package com.breakoutms.lfs.server.core.enums;

public enum District {
	Berea("Berea"),
	Butha_Buthe("Butha-Buthe"),
	Leribe("Leribe"),
	Mafeteng("Mafeteng"),
	Maseru("Maseru"),
	Mohale_Hoek("Mohale's Hoek"),
	Mokhotlong("Mokhotlong"),
	Qhacha("Qhacha"),
	Quthing("Quthing"),
	Thaba_Tseka("Thaba-Tseka"),
	Other("Other");

	private String districtName;

	private District(String name){
		districtName = name;
	}

	@Override
	public String toString() {
		return districtName;
	}

	public static District fromString(String text) {
		for (District en : District.values()) {
			if (en.districtName.equalsIgnoreCase(text)) {
				return en;
			}
		}
		throw new IllegalArgumentException("No constant with text " + text + " found");
	}
}
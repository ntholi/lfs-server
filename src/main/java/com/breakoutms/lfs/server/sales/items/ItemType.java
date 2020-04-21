/**
 * 
 */
package com.breakoutms.lfs.server.sales.items;

/**
 * @author Ntholi Nkhatho
 *
 */
public enum ItemType {

	COFFIN("Coffin/Casket"),
	CREMATION,
	ERECTION,
	LETTERS,
	MORTUARY,
	REFRIGERATION,
	PROGRAMS,
	SERVICES,
	TOMBSTONE,
	TRANSPORT,
	WREATHS,
	OTHER;

	private String value;

	ItemType(){}
	ItemType(String value){
		this.value = value;
	}
	@Override
	public String toString() {
		if(value != null){
			return value;
		}
		else return name();
	}
}


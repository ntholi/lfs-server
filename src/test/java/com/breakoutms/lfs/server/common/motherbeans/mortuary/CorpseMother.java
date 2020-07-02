package com.breakoutms.lfs.server.common.motherbeans.mortuary;

import com.breakoutms.lfs.server.common.motherbeans.AuditableMother;
import com.breakoutms.lfs.server.mortuary.model.Corpse;

public class CorpseMother extends AuditableMother<Corpse, String>{

	public static Corpse selloLebese() {
		return new CorpseMother()
				.names("Sello")
				.surname("Lebese")
				.id("101")
				.build();
	}
	
	public CorpseMother names(String names) {
		entity.setNames(names);
		return this;
	}
	
	public CorpseMother surname(String surname) {
		entity.setSurname(surname);
		return this;
	}
	
	@Override
	public CorpseMother removeIDs() {
		entity.setTagNo(null);
		entity.getTransferredFrom().setId(null);
		entity.getTransport().setId(null);
		if(entity.getNextOfKins() != null) {
			entity.getNextOfKins().forEach(it -> it.setId(null));
		}
		return this;
	}
}

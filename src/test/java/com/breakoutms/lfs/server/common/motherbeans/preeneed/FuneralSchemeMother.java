package com.breakoutms.lfs.server.common.motherbeans.preeneed;

import com.breakoutms.lfs.server.common.motherbeans.BaseMother;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;

public class FuneralSchemeMother extends BaseMother<FuneralScheme>{

	public FuneralSchemeMother name(String name) {
		entity.setName(name);
		return this;
	}

	public FuneralSchemeMother id(Integer id) {
		entity.setId(id);
		return this;
	}
}
